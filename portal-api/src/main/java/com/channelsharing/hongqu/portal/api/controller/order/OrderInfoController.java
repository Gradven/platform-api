/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.entity.InvoiceInfo;
import com.channelsharing.hongqu.portal.api.entity.OrderInvoice;
import com.channelsharing.hongqu.portal.api.service.CartInfoService;
import com.channelsharing.hongqu.portal.api.service.OrderGoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.entity.Paging;
import com.channelsharing.common.lock.method.CacheLock;
import com.channelsharing.common.lock.method.CacheParam;
import com.channelsharing.common.utils.DateUtils;
import com.channelsharing.pub.enums.PayStatus;
import com.channelsharing.pub.enums.ShippingStatus;
import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.OrderGoods;
import com.channelsharing.hongqu.portal.api.entity.OrderInfo;
import com.channelsharing.hongqu.portal.api.service.OrderInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 订单信息Controller
 *
 * @author liuhangjun
 * @version 2018-06-20
 */
@Api(tags = "订单信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/orderInfo")
public class OrderInfoController extends BaseController {

	@Resource
	private OrderInfoService orderInfoService;

	@Autowired
	private OrderGoodsService orderGoodsService;

	@Autowired
	private CartInfoService cartInfoService;

	@ApiOperation(value = "获取单条订单信息")
	@GetMapping("/{sn}")
	public OrderInfo findOne(@PathVariable String sn) {

		OrderInfo orderInfo = orderInfoService.findOne(sn, super.currentUserId());
		if (orderInfo == null)
			orderInfo = new OrderInfo();

		return orderInfo;
	}

	@ApiOperation(value = "获取订单信息列表")
	@GetMapping
	public Paging<OrderInfo> findPaging(@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit,
			@ApiParam(value = "付款状态，0:未支付，1:已支付，99：已取消", example = "0") @RequestParam(required = false) Integer payStatus) {

		OrderInfo entity = new OrderInfo();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setPayStatus(payStatus);
		entity.setUserId(super.currentUserId());

		// 如果查询未支付的订单，那么设置查询生效时间（半小时内）的订单
		if (payStatus != null && payStatus.equals(PayStatus.unPay.getCode())) {
			Date currentTime = new Date();
			Date beginCreateTime = DateUtils.minusSecond(ExpireTimeConstant.HALF_AN_HOUR);

			entity.setBeginCreateTime(beginCreateTime);
			entity.setEndCreateTime(currentTime);
		}

		return orderInfoService.findPagingWithCalculate(entity);

	}

	@ApiOperation(value = "根据付款和运送状态统计数量")
	@GetMapping("/countStatus")
	public Map<String, Integer> countStatus() {
		Map<String, Integer> map = new HashMap<>();

		// 待付款订单数
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setPayStatus(PayStatus.unPay.getCode());
		orderInfo.setUserId(super.currentUserId());
		Date currentTime = new Date();
		Date beginCreateTime = DateUtils.minusSecond(ExpireTimeConstant.HALF_AN_HOUR);
		orderInfo.setBeginCreateTime(beginCreateTime);
		orderInfo.setEndCreateTime(currentTime);
		int unPayCount = orderInfoService.findCount(orderInfo);
		map.put("unPay", unPayCount);

		/// 待收货商品数
		OrderGoods orderGoods = new OrderGoods();
		orderGoods.setShippingStatusArray(
				new Integer[] { ShippingStatus.unShipped.getCode(), ShippingStatus.shipped.getCode() });
		orderGoods.setPayStatus(PayStatus.paid.getCode());
		orderGoods.setUserId(super.currentUserId());
		int shippedCount = orderGoodsService.findCount(orderGoods);
		map.put("waiting", shippedCount);

		return map;
	}

	/**
	 * @CacheLock 主要锁住方法，避免用户重复提交
	 * @CacheLock 一般和 @CacheParam 配合使用
	 * @param orderInfoAddRequestEntity
	 */
	@CacheLock(prefix = "portal:orderInfoController:add:")
	@ApiOperation(value = "提交一条订单信息")
	@PostMapping
	public Map<String, String> add(
			@CacheParam(name = "orderInfoAddRequestEntity") @RequestBody @Validated OrderInfoAddRequestEntity orderInfoAddRequestEntity) {
		OrderInfo entity = new OrderInfo();
		BeanUtils.copyProperties(orderInfoAddRequestEntity, entity);
		entity.setUserId(super.currentUserId());
		

		List<OrderGoodsAddRequestEntity> orderGoodsAddRequestEntityList = orderInfoAddRequestEntity.orderGoodsAddRequestEntityList;
		List<OrderGoods> orderGoodsList = new ArrayList<>();
		for (OrderGoodsAddRequestEntity orderGoodsAddRequestEntity : orderGoodsAddRequestEntityList) {
			OrderGoods orderGoods = new OrderGoods();
			BeanUtils.copyProperties(orderGoodsAddRequestEntity, orderGoods);
			
			// 发票信息
			OrderInvoice orderInvoice = new OrderInvoice();
			if (orderGoodsAddRequestEntity.invoiceInfoAddRequestEntity != null){
				BeanUtils.copyProperties(orderGoodsAddRequestEntity.invoiceInfoAddRequestEntity, orderInvoice);
				orderGoods.setOrderInvoice(orderInvoice);
			}
			
			
			orderGoodsList.add(orderGoods);
		}

		Map<String, String> map = orderInfoService.addOrder(entity, orderGoodsList);

		// 如果是购物车提交的订单，则将该购物车的数据删除
		Long[] cartIds = orderInfoAddRequestEntity.getCartIds();
		cartInfoService.batchDelete(cartIds, super.currentUserId());

		return map;
	}
	
	@CacheLock(prefix = "portal:orderInfoController:retry:")
	@ApiOperation(value = "重新支付订单")
	@PostMapping("/retry")
	public Map<String, String> retryAdd(@CacheParam(name = "sn") @RequestParam String sn){
		
		Map<String, String> map = orderInfoService.retryAddOrder(sn, super.currentUser());
		
		return map;
	}

	@ApiOperation(value = "修改一条订单信息")
	@PutMapping
	public void cancelOrder(@RequestBody @Validated OrderInfoModifyRequestEntity orderInfoModifyRequestEntity) {
		OrderInfo entity = new OrderInfo();
		BeanUtils.copyProperties(orderInfoModifyRequestEntity, entity);

		orderInfoService.modify(entity);
	}

	@ApiOperation(value = "删除一条订单信息")
	// @DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		OrderInfo entity = new OrderInfo();
		entity.setId(id);
		orderInfoService.delete(entity);
	}

}
