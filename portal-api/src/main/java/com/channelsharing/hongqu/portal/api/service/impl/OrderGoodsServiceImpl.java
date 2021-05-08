/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.entity.ShopProfit;
import com.channelsharing.hongqu.portal.api.entity.SupplierService;
import com.channelsharing.hongqu.portal.api.service.OrderGoodsService;
import com.channelsharing.common.entity.Paging;
import com.channelsharing.common.exception.BadRequestException;
import com.channelsharing.hongqu.portal.api.service.ShopProfitService;
import com.channelsharing.hongqu.portal.api.service.SupplierServiceService;
import com.channelsharing.pub.enums.OrderStatus;
import com.channelsharing.pub.enums.ShippingStatus;
import com.channelsharing.hongqu.portal.api.entity.OrderInfo;
import com.channelsharing.hongqu.portal.api.service.OrderInfoService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.entity.OrderGoods;
import com.channelsharing.hongqu.portal.api.dao.OrderGoodsDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单的商品信息Service
 * @author liuhangjun
 * @version 2018-06-20
 */
@Service
public class OrderGoodsServiceImpl extends CrudServiceImpl<OrderGoodsDao, OrderGoods> implements OrderGoodsService {
    
    @Autowired
    private OrderInfoService orderInfoService;
    
    @Autowired
    private ShopProfitService shopProfitService;
    
    @Autowired
    private SupplierServiceService supplierServiceService;
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;
    
    @Override
    public OrderGoods findOne(Long id) {
        OrderGoods entity = new OrderGoods();
        entity.setId(id);
        return super.findOne(entity);
    }
    
    @Override
    public OrderGoods findOneByOrderSn(String orderSn) {
        OrderGoods entity = new OrderGoods();
        entity.setOrderSn(orderSn);
        return super.findOne(entity);
    }
    
    @Override
    public List<OrderGoods> findListByOrderSn(String orderSn) {
        OrderGoods entity = new OrderGoods();
        entity.setOrderSn(orderSn);
        entity.setLimit(Constant.MAX_LIMIT);
        Paging<OrderGoods> orderGoodsPaging = super.findPaging(entity);
       
        if (orderGoodsPaging != null){
            return orderGoodsPaging.getRows();
        }else {
            return new ArrayList<>();
        }
        
    }
    
    @Override
    public Integer findCount(OrderGoods entity){
        return super.dao.findAllCount(entity);
    }
    
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "goodsInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsInfo:id:' + #entity.goodsId")
    @Transactional
    @Override
    public void modify(@NonNull OrderGoods entity){
        
        if (entity.getOrderSn() == null && entity.getId() == null){
            throw new BadRequestException("订单商品id和订单编号不能同时为空值");
        }
        
        super.modify(entity);
    }
    
    /**
     *  确认签收,
     *  如果全部的商品都被签收了，那么订单状态修改为已完成
     * @param entity
     */
    @Transactional
    @Override
    public void confirm(@NonNull OrderGoods entity){
    
        entity.setConfirmTime(new Date());
        this.modify(entity);
    
        // 用户确认收货后，将店铺收益设置为确认状态
        ShopProfit shopProfitUpdate = new ShopProfit();
        shopProfitUpdate.setOrderGoodsId(entity.getId());
        shopProfitUpdate.setConfirmFlag(BooleanEnum.yes.getCode());
        shopProfitUpdate.setConfirmTime(new Date());
        shopProfitService.modify(shopProfitUpdate);
    
    
        //  --start--   如果全部的商品都被签收了，那么订单状态修改为已完成
        OrderGoods query = new OrderGoods();
        query.setOrderSn(entity.getOrderSn());
        query.setLimit(Constant.MAX_LIMIT);
        
        Integer orderStatus = null;
        String orderSn = null;
        
        List<OrderGoods> orderGoodsList = super.findPaging(query).getRows();
        for (OrderGoods orderGoods : orderGoodsList){
            if (!orderGoods.getShippingStatus().equals(ShippingStatus.signed.getCode())){
                orderStatus = null;
                break;
            }else {
                orderStatus = ShippingStatus.signed.getCode();
                orderSn = orderGoods.getOrderSn();
            }
        }
    
        
        if (orderStatus != null ){
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setSn(orderSn);
            orderInfo.setStatus(OrderStatus.completed.getCode());
            orderInfoService.modify(orderInfo);
        }
        //  --end--   如果全部的商品都被签收了，那么订单状态修改为已完成
    }
    
    
    @Override
    public Paging<OrderGoods> findPaging(OrderGoods entity){
    
        Paging<OrderGoods> orderGoodsPaging = super.findPaging(entity);
    
        // 加上此订单是否有售后服务记录
        List<OrderGoods> orderGoodsList = new ArrayList<>();
        for (OrderGoods orderGoods : orderGoodsPaging.getRows()){
    
            SupplierService supplierService = supplierServiceService.findOneByOrderGoodsId(orderGoods.getId());
            if (supplierService != null && supplierService.getId() != null){
                
                orderGoods.setSupplierServiceId(supplierService.getId());
            }
    
            orderGoodsList.add(orderGoods);
        }
    
        orderGoodsPaging.setRows(orderGoodsList);
        
        return orderGoodsPaging;
    }
    
    /**
     * 超时自动确认收货时间
     */
    @Override
    public void autoConfirm(OrderGoods entity){
        super.dao.autoConfirm(entity);
    }
}
