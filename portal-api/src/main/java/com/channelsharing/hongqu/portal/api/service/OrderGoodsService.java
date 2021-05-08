/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.portal.api.entity.OrderGoods;
import lombok.NonNull;

import java.util.List;


/**
 * 订单的商品信息Service
 * @author liuhangjun
 * @version 2018-06-20
 */
public interface OrderGoodsService extends CrudService<OrderGoods>{
    
    List<OrderGoods> findListByOrderSn(String orderSn);
    
    Integer findCount(OrderGoods entity);
    
    OrderGoods findOneByOrderSn(String orderSn);
    
    void confirm(@NonNull OrderGoods entity);
    
    void autoConfirm(OrderGoods entity);

}
