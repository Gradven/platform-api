/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service;

import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.supplier.api.entity.OrderInfo;


/**
 * 订单信息Service
 * @author liuhangjun
 * @version 2018-07-01
 */
public interface OrderInfoService extends CrudService<OrderInfo>{
    
    OrderInfo findOneBySn(String sn);
    
}
