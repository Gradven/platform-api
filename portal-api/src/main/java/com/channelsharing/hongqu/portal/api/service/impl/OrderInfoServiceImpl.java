/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.xml.crypto.Data;

import com.channelsharing.hongqu.portal.api.entity.*;
import com.channelsharing.hongqu.portal.api.service.*;
import com.channelsharing.pub.enums.CancelType;
import com.channelsharing.pub.enums.OrderStatus;
import com.channelsharing.pub.enums.PayStatus;
import com.channelsharing.pub.enums.ShippingStatus;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.entity.Paging;
import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.common.exception.DataNotFoundException;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.common.utils.AmountUtils;
import com.channelsharing.common.utils.RandomUtil;
import com.channelsharing.hongqu.portal.api.dao.OrderInfoDao;
import com.channelsharing.hongqu.portal.api.enums.ProfitType;
import com.channelsharing.hongqu.portal.api.weixin.WeixinPayUtil;


/**
 * 订单信息Service
 *
 * @author liuhangjun
 * @version 2018-06-20
 */
@Service
public class OrderInfoServiceImpl extends CrudServiceImpl<OrderInfoDao, OrderInfo> implements OrderInfoService {

	@Autowired
	private ProductInfoService productInfoService;

	@Autowired
	private GoodsInfoService goodsInfoService;

	@Autowired
	private OrderGoodsService orderGoodsService;

	@Autowired
	private ShopWalletService shopWalletService;

	@Autowired
	private ShopProfitService shopProfitService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private WeixinPayUtil weixinPayUtil;

	@Resource
	private ShopSalesDataService shopSalesDataService;
	
	@Autowired
	private OrderInvoiceService orderInvoiceService;

	@Value("${weixin.pay.notifyUrl.goods}")
	private String goodsNotifyUrl;
	
	@Autowired
	private Environment env;
	
	private Set<String> envSet;
	
	@PostConstruct
	private void init() {
		envSet = Sets.newHashSet(env.getActiveProfiles());
	}

	/**
	 * 用户发起订单业务逻辑
	 *
	 * @param orderInfo
	 * @param orderGoodsList
	 */
	@Transactional
	@Override
	public Map<String, String> addOrder(OrderInfo orderInfo, List<OrderGoods> orderGoodsList) {

		String orderSn = this.generateSn();
		orderInfo.setSn(orderSn);

		BigDecimal amount = BigDecimal.ZERO;

		// 处理产品订购业务逻辑：锁定库存，计算商品价格
		for (OrderGoods orderGoods : orderGoodsList) {
			// 查询产品信息
			ProductInfo productInfoQuery = new ProductInfo();
			productInfoQuery.setId(orderGoods.getProductId());
			ProductInfo productInfo = productInfoService.findOne(productInfoQuery);

			if (productInfo == null || productInfo.getId() == null) {
				throw new DataNotFoundException("没有您要订购的产品id为【" + orderGoods.getProductId() + "】的产品");
			}

			Long goodsId = productInfo.getGoodsId();

			// 查询商品信息
			GoodsInfo goodsInfo = goodsInfoService.findOne(productInfo.getGoodsId());

			if (goodsInfo == null || goodsInfo.getId() == null) {
				throw new DataNotFoundException("没有您要订购的商品id为【" + orderGoods.getGoodsId() + "】的商品");
			}

			String goodsName = goodsInfo.getName();

			if (goodsInfo.getOnSaleFlag().equals(BooleanEnum.no.getCode())) {
				throw new SystemInnerBusinessException("此商品【" + goodsName + "】已经下架");
			}

			if (productInfo.getStoreNumber().equals(0)) {
				throw new SystemInnerBusinessException("您选购的此规格的商品【" + goodsName + "】已经售罄");
			}

			// 转换为BigDecimal类型才能与价格做乘法
			BigDecimal goodsNumber = new BigDecimal(Double.valueOf(orderGoods.getGoodsNumber()));

			if (orderGoods.getGoodsNumber() > productInfo.getStoreNumber()) {
				throw new SystemInnerBusinessException("您正购买的此规格的商品【" + goodsName + "】库存不足");
			}

			BigDecimal retailPrice = productInfo.getRetailPrice();
			BigDecimal profit = productInfo.getProfit();

			// 订单的商品的产品信息入库到order_goods表中
			orderGoods.setOrderSn(orderSn);
			orderGoods.setGoodsId(goodsId);
			orderGoods.setGoodsSn(productInfo.getGoodsSn());
			orderGoods.setGoodsName(goodsName);
			orderGoods.setProfit(profit);
			orderGoods.setRetailPrice(retailPrice);
			orderGoods.setUnitPrice(productInfo.getUnitPrice());
			orderGoods.setSupplierId(goodsInfo.getSupplierId());
			orderGoods.setUserId(orderInfo.getUserId());
			orderGoods.setGoodsSpecificationIds(productInfo.getGoodsSpecificationIds());
			orderGoods.setAmountProfit(profit.multiply(goodsNumber));
			orderGoods.setAmountRetail(retailPrice.multiply(goodsNumber));
			orderGoods.setShippingStatus(ShippingStatus.unShipped.getCode());

			// 取出产品中的图片作为订单商品的封面
			orderGoods.setPicUrl(productInfo.getPicUrl());
			OrderGoods orderGoodsResult = orderGoodsService.addWithResult(orderGoods);

			// 产品减库存(可以理解为锁定库存)操作, (商品支付后销量需要增加对应的数量)
			ProductInfo productInfoUpdate = new ProductInfo();
			productInfoUpdate.setId(orderGoods.getProductId());
			productInfoUpdate.setGoodsId(goodsId);  // 为了清除缓存使用
			productInfoUpdate.setStoreNumber(productInfo.getStoreNumber() - orderGoods.getGoodsNumber());
			productInfoService.modify(productInfoUpdate);

			if (goodsInfo.getStoreNumber() < orderGoods.getGoodsNumber()) {
				throw new SystemInnerBusinessException("【" + goodsName + "】商品库存少于产品库存，库存数据不对应，请维护商品库存");
			}

			// 商品库存改为产品库存相加（对应商品也需要减掉库存, 商品支付后销量需要增加对应的数量）
			Integer goodsStoreNumber = productInfoService.sumGoodsStore(goodsId);
			GoodsInfo goodsInfoUpdate = new GoodsInfo();
			goodsInfoUpdate.setId(goodsId);
			goodsInfoUpdate.setStoreNumber(goodsStoreNumber);
			goodsInfoService.modify(goodsInfoUpdate);

			// 计算订单总额
			amount = amount.add(orderGoods.getRetailPrice().multiply(goodsNumber));
			
			// 如果发票信息不为空，增新增订单发票信息
			OrderInvoice orderInvoice = orderGoods.getOrderInvoice();
			if (orderInvoice != null && orderInvoice.getTitle() != null){
				orderInvoice.setOrderGoodsId(orderGoodsResult.getId());
				orderInvoice.setOrderSn(orderSn);
				orderInvoice.setUserId(orderInfo.getUserId());
				orderInvoiceService.add(orderInvoice);
			}

		}

		orderInfo.setStatus(OrderStatus.unPay.getCode());
		orderInfo.setPayStatus(PayStatus.unPay.getCode());
		orderInfo.setAmount(amount);
		orderInfo.setCancelType(CancelType.unCancel.getCode());
		super.add(orderInfo);
		

		UserInfo userInfo = userInfoService.findOne(orderInfo.getUserId());
		int amountFen = AmountUtils.changeY2F(amount);
		
		Map map = null;
		if (!envSet.contains("dev")) { // 非开发环境，才调用微信的支付接口
			map = weixinPayUtil.unifiedOrder(orderSn, amountFen, userInfo.getThirdPartyUserId(), goodsNotifyUrl);
		}
		
		return map;
	}
	
	/**
	 * 用户重新发起支付
	 * @param sn
	 * @param userInfo
	 * @return
	 */
	@Transactional
	@Override
	public Map<String, String> retryAddOrder(String sn, UserInfo userInfo){
	
		OrderInfo orderInfo = this.findOne(sn, userInfo.getId());
		if (orderInfo == null){
		   throw new DataNotFoundException("查无此订单号");
		}
		
		if (orderInfo.getPayStatus().equals(PayStatus.paid.getCode())){
			throw new SystemInnerBusinessException("订单已支付");
		}
		
		if (orderInfo.getStatus().equals(OrderStatus.cancel.getCode())){
			throw new SystemInnerBusinessException("订单已取消");
		}
		
		if (orderInfo.getExpiredSecond() < 0L){
		    throw new SystemInnerBusinessException("订单支付已超时");
		}
		
		int amount = AmountUtils.changeY2F(orderInfo.getAmount());
		
		logger.debug("Pay order amount is {}", amount);
		
		Map map = weixinPayUtil.unifiedOrder(sn, amount, userInfo.getThirdPartyUserId(), goodsNotifyUrl);
		
	
		return map;
	
	}

	@Override
	public OrderInfo findOne(Long id) {
		OrderInfo entity = new OrderInfo();
		entity.setId(id);

		OrderInfo orderInfo = super.findOne(entity);
		// 当订单为未付款时，计算订单的过期时间
		orderInfo = this.setExpireSecond(orderInfo);

		return orderInfo;
	}

	@Override
	public OrderInfo findOne(String sn, Long userId) {
		OrderInfo entity = new OrderInfo();
		entity.setSn(sn);
		entity.setUserId(userId);
		OrderInfo orderInfo = super.findOne(entity);

		if (orderInfo == null) {
			throw new DataNotFoundException("无此订单信息");
		}

		// 当订单为未付款时，计算订单的过期时间
		orderInfo = this.setExpireSecond(orderInfo);

		return orderInfo;
	}

	/**
	 * 支付订单 支付需要处理的业务： 1、将订单信息改为已支付状态 2、增加商品和产品的销量 3、记录店铺的分账信息 4、记录店铺的收益
	 *
	 * @param orderInfoPay
	 */
	@Transactional
	@Override
	public void payOrder(@NotNull OrderInfo orderInfoPay) {

		String sn = orderInfoPay.getSn();

		OrderInfo entity = new OrderInfo();
		entity.setSn(sn);
		OrderInfo orderInfo = super.findOne(entity);
		if (orderInfo == null) {
			throw new DataNotFoundException("没有此订单信息");
		}

		if (!orderInfo.getPayStatus().equals(PayStatus.unPay.getCode())) {
			throw new SystemInnerBusinessException("订单不处于未支付状态");
		}

		Date expiredTime = this.getExpireTime(orderInfo.getCreateTime());
		Date currentTime = new Date();
		if (currentTime.after(expiredTime)) { // 订单超时阀值处理

			this.cancelOrder(sn, CancelType.outTimeCancel, orderInfo.getUserId(), null);

			throw new SystemInnerBusinessException("订单支付超时");
		}

		// 将订单信息改为已支付状态
		OrderInfo orderInfoUpdate = new OrderInfo();
		orderInfoUpdate.setSn(sn);
		orderInfoUpdate.setPayType(orderInfoPay.getPayType());
		orderInfoUpdate.setPayTime(orderInfoPay.getPayTime());
		orderInfoUpdate.setPayNo(orderInfoPay.getPayNo());
		orderInfoUpdate.setPayMoney(orderInfoPay.getPayMoney());
		orderInfoUpdate.setPayStatus(PayStatus.paid.getCode());
		orderInfoUpdate.setStatus(OrderStatus.paid.getCode());
		super.modify(orderInfoUpdate);

		// 修改order_Goods表的支付状态为已支付
		OrderGoods orderGoodsPaid = new OrderGoods();
		orderGoodsPaid.setOrderSn(sn);
		orderGoodsPaid.setPayStatus(PayStatus.paid.getCode());
		orderGoodsService.modify(orderGoodsPaid);

		// 增加商品和产品的销量和商铺的分账信息
		OrderGoods orderGoodsQuery = new OrderGoods();
		orderGoodsQuery.setOrderSn(sn);
		List<OrderGoods> orderGoodsList = orderGoodsService.findPaging(orderGoodsQuery).getRows();
		Map<Long, BigDecimal> salesDataMap = new HashMap<>();

		for (OrderGoods orderGoods : orderGoodsList) {

			Long shopId = orderGoods.getShopId();
			Long goodsId = orderGoods.getGoodsId();
			Long productId = orderGoods.getProductId();
			int goodsNumber = orderGoods.getGoodsNumber();
			
			// 单件商品收益
			BigDecimal goodsAmountProfit = orderGoods.getAmountProfit();

			// 销量更改
			goodsInfoService.addSalesVolume(goodsNumber, goodsId);
			productInfoService.addSalesVolume(goodsNumber, productId, goodsId);
			

			// 计算订单商品的收益，增加一条收益记录
			ShopProfit shopProfit = new ShopProfit();
			shopProfit.setShopId(shopId);
			shopProfit.setOrderGoodsId(orderGoods.getId());
			shopProfit.setConfirmFlag(BooleanEnum.no.getCode());
			shopProfit.setAvailableFlag(BooleanEnum.no.getCode());
			shopProfit.setProfit(goodsAmountProfit);
			shopProfit.setType(ProfitType.goodsProfit.getCode());
			shopProfit.setSn(sn);
			shopProfitService.add(shopProfit);

			// 计算店铺销售数据
			BigDecimal salesAmount = salesDataMap.get(shopId);
			if (salesAmount == null) {
				salesDataMap.put(shopId, orderGoods.getAmountRetail());
			}else {
				salesDataMap.put(shopId, salesAmount.add(orderGoods.getAmountRetail()));
			}
		}

		salesDataMap.entrySet()
				.forEach(entry -> shopSalesDataService.add(new ShopSalesData(entry.getKey(), entry.getValue())));
	}

	/**
	 * 取消订单 有三种情形：(1, "用户主动取消"), (2, "支付超时自动取消"), (3, "支付失败系统取消") TODO
	 *
	 * @param sn
	 * @param cancelType
	 * @param userId
	 */
	@Transactional
	@Override
	public void cancelOrder(@NotNull String sn, @NotNull CancelType cancelType, Long userId, String remark) {

		// 取消订单逻辑
		OrderInfo entity = new OrderInfo();
		entity.setCancelType(cancelType.getCode());
		entity.setSn(sn);
		entity.setUserId(userId);
		entity.setRemark(remark);
		entity.setCancelTime(new Date());
		super.modify(entity);

		// 回退商品和产品的库存逻辑
		OrderGoods orderGoodsQuery = new OrderGoods();
		orderGoodsQuery.setOrderSn(sn);
		List<OrderGoods> orderGoodsList = orderGoodsService.findPaging(orderGoodsQuery).getRows();
		for (OrderGoods orderGoods : orderGoodsList) {
			long goodsId = orderGoods.getGoodsId();
			long productId = orderGoods.getProductId();
			int goodsNumber = orderGoods.getGoodsNumber();

			goodsInfoService.addStoreNumber(goodsNumber, goodsId);
			productInfoService.addStoreNumber(goodsNumber, productId, goodsId);

		}

	}

	/**
	 * 查看订单，并给出订单的商品信息
	 * 同时计算订单的过期时间，进行超时取消订单
	 *
	 * @param entity
	 * @return
	 */
	@Override
	public Paging<OrderInfo> findPagingWithCalculate(OrderInfo entity) {

		Paging<OrderInfo> orderInfoPaging = super.findPaging(entity);

		List<OrderInfo> orderInfoList = orderInfoPaging.getRows();
		List<OrderInfo> orderInfoListNew = new ArrayList<>();
		for (OrderInfo orderInfo : orderInfoList) {

			List<OrderGoods> orderGoodsList = orderGoodsService.findListByOrderSn(orderInfo.getSn());

			orderInfo.setOrderGoodsList(orderGoodsList);

			// 当订单为未付款时，计算订单的过期时间
			orderInfo = this.setExpireSecond(orderInfo);
			
			orderInfoListNew.add(orderInfo);
		}

		orderInfoPaging.setRows(orderInfoListNew);

		return orderInfoPaging;

	}

	public Integer findCount(OrderInfo orderInfo) {

		return super.dao.findAllCount(orderInfo);
	}

	/**
	 * 在时间戳加上5位随机数组成订单号
	 *
	 * @return
	 */
	private String generateSn() {
		String sn = Long.toString(System.currentTimeMillis()) + RandomUtil.getRandomNumString(5);

		return sn;
	}

	/**
	 * 设置过期时长
	 *
	 * @return
	 */
	private OrderInfo setExpireSecond(OrderInfo orderInfo) {
		// 当订单为未付款时，计算订单的过期时间
		if (orderInfo.getPayStatus().equals(PayStatus.unPay.getCode())) {
			Date expireTime = this.getExpireTime(orderInfo.getCreateTime());
			Date currentTime = new Date();
			long expiredSecond = expireTime.getTime() - currentTime.getTime();
			orderInfo.setExpiredSecond(expiredSecond / 1000);

			// 如果已经过期且订单状态不是取消状态, 那么设置订单为取消状态
			if (expiredSecond <= 0 && !orderInfo.getStatus().equals(OrderStatus.cancel.getCode())) {
				orderInfo.setStatus(OrderStatus.cancel.getCode());
				orderInfo.setCancelType(CancelType.outTimeCancel.getCode());
				orderInfo.setCancelTime(new Date());

				super.modify(orderInfo);
			}

		}

		return orderInfo;

	}

	/**
	 * 获取过期时间
	 *
	 * @param createTime
	 * @return
	 */
	private Date getExpireTime(Date createTime) {
		int expiredSecond = Math.toIntExact(ExpireTimeConstant.HALF_AN_HOUR); // 订单超时时间定义为30分钟
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(createTime);
		calendar.add(Calendar.SECOND, +expiredSecond);
		Date expiredTime = calendar.getTime();

		return expiredTime;
	}
}
