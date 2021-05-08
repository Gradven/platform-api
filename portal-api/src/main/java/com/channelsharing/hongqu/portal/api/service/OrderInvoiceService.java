/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.portal.api.entity.OrderInvoice;


/**
 * 订单发票Service
 * @author liuhangjun
 * @version 2018-07-29
 */
public interface OrderInvoiceService extends CrudService<OrderInvoice>{
    
    OrderInvoice findOne(Long id , Long userId);

}
