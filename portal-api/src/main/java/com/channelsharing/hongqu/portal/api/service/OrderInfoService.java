/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.entity.Paging;
import com.channelsharing.hongqu.portal.api.entity.OrderGoods;
import com.channelsharing.hongqu.portal.api.entity.OrderInfo;
import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.pub.enums.CancelType;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;


/**
 * 订单信息Service
 * @author liuhangjun
 * @version 2018-06-20
 */
public interface OrderInfoService extends CrudService<OrderInfo>{
    
    Map<String, String> addOrder(OrderInfo orderInfo, List<OrderGoods> orderGoodsList);
    
    Map<String, String> retryAddOrder(String sn, UserInfo userInfo);
    
    OrderInfo findOne(String sn, Long userId);
    
    void payOrder(@NotNull OrderInfo orderInfo);
    
    void cancelOrder(@NotNull String sn, @NotNull CancelType cancelType, Long userId, String remark);
    
    Integer findCount(OrderInfo orderInfo);
    
    Paging<OrderInfo> findPagingWithCalculate(OrderInfo entity);
    
    
}
