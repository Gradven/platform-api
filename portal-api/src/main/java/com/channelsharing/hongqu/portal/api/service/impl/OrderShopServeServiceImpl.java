/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.common.entity.Paging;
import com.channelsharing.hongqu.portal.api.dao.OrderShopServeDao;
import com.channelsharing.hongqu.portal.api.entity.OrderShopServe;
import com.channelsharing.hongqu.portal.api.entity.ShopInfo;
import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.hongqu.portal.api.weixin.WeixinPayUtil;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.common.exception.DataNotFoundException;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import com.channelsharing.common.utils.AmountUtils;
import com.channelsharing.common.utils.RandomUtil;
import com.channelsharing.hongqu.portal.api.constant.ConfigParamConstant;
import com.channelsharing.pub.enums.CancelType;
import com.channelsharing.pub.enums.OrderStatus;
import com.channelsharing.pub.enums.PayStatus;
import com.channelsharing.hongqu.portal.api.service.ConfigParamService;
import com.channelsharing.hongqu.portal.api.service.OrderShopServeService;
import com.channelsharing.hongqu.portal.api.service.ShopInfoService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

/**
 * 店铺技术服务费订单Service
 * @author liuhangjun
 * @version 2018-06-18
 */
@Service
public class OrderShopServeServiceImpl extends CrudServiceImpl<OrderShopServeDao, OrderShopServe> implements OrderShopServeService {
   
    @Autowired
    private ConfigParamService configParamService;
    
    @Autowired
    private WeixinPayUtil weixinPayUtil;
    
    @Autowired
    private ShopInfoService shopInfoService;
    
    @Value("${weixin.pay.notifyUrl.shop}")
    private String shopNotifyUrl;
    
    
    @Override
    public OrderShopServe findOne(Long id) {
        OrderShopServe entity = new OrderShopServe();
        entity.setId(id);
        
        return super.findOne(entity);
    }
    
    /**
     * 发起订单
     * @param entity
     * @param userInfo
     * @return
     */
    @Transactional
    @Override
    public  Map<String, String> addOrder(@NotNull OrderShopServe entity, @NotNull UserInfo userInfo){
    
        // 支付技术费用
        String feeAmountStr = configParamService.findOne(ConfigParamConstant.PORTAL_SHOP_YEAR_SERVICE_FEE);
        int feeAmount = NumberUtils.toInt(AmountUtils.changeY2F(feeAmountStr), 9999);
        String sn = this.generateSn();
        entity.setSn(sn);
        entity.setFee(new BigDecimal(feeAmountStr));
        super.add(entity);
    
        Map map = weixinPayUtil.unifiedOrder(sn, feeAmount, userInfo.getThirdPartyUserId(), shopNotifyUrl);
       
       return map;
    }
    
    /**
     * 1、支付订单
     * 2、把店铺信息改为已经缴费状态
     * 3、若是初次付费，填写上店铺过期时间
     * 4、如果是续费的话，将店铺过期时间延长
     * @param orderShopServePay
     */
    @Transactional
    @Override
    public void payOrder(@NotNull OrderShopServe orderShopServePay){
    
        String sn = orderShopServePay.getSn();
    
        OrderShopServe entity = new OrderShopServe();
        entity.setSn(sn);
        OrderShopServe orderShopServe = super.findOne(entity);
        if (orderShopServe == null){
            throw new DataNotFoundException("没有此订单信息");
        }
    
        if (!orderShopServe.getPayStatus().equals(PayStatus.unPay.getCode())){
            throw new SystemInnerBusinessException("订单不处于未支付状态");
        }
    
        int expiredSecond = Math.toIntExact(ExpireTimeConstant.HALF_AN_HOUR);   // 订单超时时间定义为30分钟
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(orderShopServe.getCreateTime() );
        calendar.add(Calendar.SECOND , + expiredSecond);
        Date expiredTime = calendar.getTime();
        Date currentTime = new Date();
        if (currentTime.after(expiredTime) ){  // 订单超时阀值处理
    
            this.cancelOrder(sn, CancelType.outTimeCancel, orderShopServe.getUserId(), null);
            
            throw new SystemInnerBusinessException("订单支付超时");
        }
    
        // 将订单信息改为已支付状态
        OrderShopServe orderShopServiceUpdate = new OrderShopServe();
        orderShopServiceUpdate.setSn(sn);
        orderShopServiceUpdate.setPayType(orderShopServePay.getPayType());
        orderShopServiceUpdate.setPayTime(new Date());
        orderShopServiceUpdate.setPayNo(orderShopServePay.getPayNo());
        orderShopServiceUpdate.setPayMoney(orderShopServePay.getPayMoney());
        orderShopServiceUpdate.setPayStatus(PayStatus.paid.getCode());
        orderShopServiceUpdate.setStatus(OrderStatus.paid.getCode());
        super.modify(orderShopServiceUpdate);
        
        
        // 以下是店主缴纳技术服务费的业务逻辑
        ShopInfo shopInfoQuery = new ShopInfo();
        shopInfoQuery.setId(orderShopServe.getShopId());
        ShopInfo shopInfo = shopInfoService.findOne(shopInfoQuery);
    
        ShopInfo shopInfoUpdate = new ShopInfo();
        shopInfoUpdate.setId(orderShopServe.getShopId());
    
        calendar = Calendar.getInstance();
        Integer payFeeFlag = shopInfo.getPayFeeFlag();
        if((payFeeFlag == null || payFeeFlag.equals(BooleanEnum.no.getCode())) || shopInfoUpdate.getExpireTime() == null){ // 若是初次付费，填写上店铺过期时间
            
            calendar.setTime(new Date());
            calendar.add(Calendar.YEAR, + 1); // 当前时间加一年
            
        }else {// 如果是续费的话，将店铺过期时间延长
            calendar.setTime(shopInfoUpdate.getExpireTime());
            calendar.add(Calendar.YEAR, + 1);  // 在之前的过期时间后加一年
        }
    
        shopInfoUpdate.setUserId(shopInfo.getUserId());
        shopInfoUpdate.setExpireTime(calendar.getTime());
        shopInfoUpdate.setPayFeeFlag(BooleanEnum.yes.getCode());  // 把店铺信息改为已经缴费状态
        shopInfoService.modify(shopInfoUpdate);
    
    }
    
    /**
     * 取消订单
     * 支付状态修改为取消状态
     * 有三种情形：(1, "用户主动取消"), (2, "支付超时自动取消"), (3, "支付失败系统取消")
     * @param sn
     */
    @Transactional
    @Override
    public void cancelOrder(@NotNull String sn, @NotNull CancelType cancelType, Long userId, String remark){
        
        
        OrderShopServe orderShopServiceQuery = new OrderShopServe();
        orderShopServiceQuery.setSn(sn);
        OrderShopServe orderShopServe = super.findOne(orderShopServiceQuery);
        
        if (orderShopServe == null){
            throw new DataNotFoundException("没有此订单信息");
        }
    
        if (orderShopServe.getStatus().equals(OrderStatus.paid.getCode())){
            throw new SystemInnerBusinessException("该订单已经支付，不能取消");
        }
    
        if (orderShopServe.getStatus().equals(OrderStatus.cancel.getCode())){
            throw new SystemInnerBusinessException("该订单已经取消，不能再次取消");
        }
        orderShopServe.setCancelTime(new Date());
        orderShopServe.setSn(sn);
        orderShopServe.setUserId(userId);
        orderShopServe.setCancelType(cancelType.getCode());
        orderShopServe.setStatus(OrderStatus.cancel.getCode());  // 取消订单
        orderShopServe.setRemark(remark);
        
        super.modify(orderShopServe);
    }
    
    
    /**
     * 此列表自带订单超时处理功能
     * @param entity
     * @return
     */
    @Override
    public Paging<OrderShopServe> findPagingWithCalculate(OrderShopServe entity){
    
    
        Paging<OrderShopServe> orderShopServePaging = super.findPaging(entity);
    
        List<OrderShopServe> orderShopServeList = orderShopServePaging.getRows();
        List<OrderShopServe> orderInfoListNew = new ArrayList<>();
        for (OrderShopServe orderShopServe : orderShopServeList) {
            
            // 当订单为未付款时，计算订单的过期时间
            orderShopServe = this.setExpireSecond(orderShopServe);
        
            orderInfoListNew.add(orderShopServe);
        }
    
        orderShopServePaging.setRows(orderInfoListNew);
    
        return orderShopServePaging;
        
    
    }
    
    
    
    
    /**
     * 设置过期时长
     *
     * @return
     */
    private OrderShopServe setExpireSecond(OrderShopServe orderShopServe) {
        // 当订单为未付款时，计算订单的过期时间
        if (orderShopServe.getPayStatus().equals(PayStatus.unPay.getCode())) {
            Date expireTime = this.getExpireTime(orderShopServe.getCreateTime());
            Date currentTime = new Date();
            long expiredSecond = expireTime.getTime() - currentTime.getTime();
            orderShopServe.setExpiredSecond(expiredSecond / 1000);
            
            // 如果已经过期且订单状态不是取消状态, 那么设置订单为取消状态
            if (expiredSecond <= 0 && !orderShopServe.getStatus().equals(OrderStatus.cancel.getCode())) {
                orderShopServe.setStatus(OrderStatus.cancel.getCode());
                orderShopServe.setCancelType(CancelType.outTimeCancel.getCode());
                orderShopServe.setCancelTime(new Date());
                
                super.modify(orderShopServe);
            }
            
        }
        
        return orderShopServe;
        
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
    
    
    /**
     * 由时间戳加上5位随机数组成订单号
     * 订单号加一个前缀，表示是技术服务年费的订单
     * @return
     */
    private String generateSn(){
        
        // 设置一个前缀表示是用户付费技术年费的订单
        String prefix = "a";
        String sn = Long.toString(System.currentTimeMillis()) + RandomUtil.getRandomNumString(5);
        sn = prefix + sn;
        
        return sn;
    }

}
