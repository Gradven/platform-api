/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.supplier.api.service.OrderInvoiceService;
import com.channelsharing.hongqu.supplier.api.entity.OrderInvoice;
import com.channelsharing.hongqu.supplier.api.dao.OrderInvoiceDao;

/**
 * 订单发票Service
 * @author liuhangjun
 * @version 2018-07-29
 */
@Service
public class OrderInvoiceServiceImpl extends CrudServiceImpl<OrderInvoiceDao, OrderInvoice> implements OrderInvoiceService {

    @Override
    public OrderInvoice findOne(Long id) {
        OrderInvoice entity = new OrderInvoice();
        entity.setId(id);

        return super.findOne(entity);
    }
    
    @Override
    public OrderInvoice findOne(String  orderSn) {
        OrderInvoice entity = new OrderInvoice();
        entity.setOrderSn(orderSn);
        
        return super.findOne(entity);
    }

}
