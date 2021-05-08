/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.entity.Paging;
import com.channelsharing.hongqu.portal.api.entity.OrderShopServe;
import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.common.service.CrudService;
import com.channelsharing.pub.enums.CancelType;

import javax.validation.constraints.NotNull;
import java.util.Map;


/**
 * 店铺技术服务费订单Service
 * @author liuhangjun
 * @version 2018-06-18
 */
public interface OrderShopServeService extends CrudService<OrderShopServe>{
    
    Map<String, String> addOrder(OrderShopServe entity, UserInfo userInfo);
    
    void payOrder(@NotNull OrderShopServe orderShopServePay);
    
    void cancelOrder(@NotNull String sn, @NotNull CancelType cancelType, Long userId, String remark);
    
    Paging<OrderShopServe> findPagingWithCalculate(OrderShopServe entity);
}
