/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service.impl;

import com.channelsharing.common.entity.Paging;
import com.channelsharing.hongqu.supplier.api.dao.OrderGoodsDao;
import com.channelsharing.hongqu.supplier.api.entity.OrderGoods;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.supplier.api.service.OrderGoodsService;


/**
 * 订单的商品信息Service
 * @author liuhangjun
 * @version 2018-07-01
 */
@Service
public class OrderGoodsServiceImpl extends CrudServiceImpl<OrderGoodsDao, OrderGoods> implements OrderGoodsService {
    

    @Override
    public OrderGoods findOne(Long id) {
        OrderGoods entity = new OrderGoods();
        entity.setId(id);

        return super.findOne(entity);
    }
    

}
