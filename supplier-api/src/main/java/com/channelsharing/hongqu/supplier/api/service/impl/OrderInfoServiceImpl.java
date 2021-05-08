/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service.impl;

import com.channelsharing.hongqu.supplier.api.dao.OrderInfoDao;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.supplier.api.service.OrderInfoService;
import com.channelsharing.hongqu.supplier.api.entity.OrderInfo;

/**
 * 订单信息Service
 * @author liuhangjun
 * @version 2018-07-01
 */
@Service
public class OrderInfoServiceImpl extends CrudServiceImpl<OrderInfoDao, OrderInfo> implements OrderInfoService {

    @Override
    public OrderInfo findOne(Long id) {
        OrderInfo entity = new OrderInfo();
        entity.setId(id);

        return super.findOne(entity);
    }
    
    @Override
    public OrderInfo findOneBySn(String sn) {
        OrderInfo entity = new OrderInfo();
        entity.setSn(sn);
        
        return super.findOne(entity);
    }

}
