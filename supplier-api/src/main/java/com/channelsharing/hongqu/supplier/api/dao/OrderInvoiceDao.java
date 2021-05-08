/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.supplier.api.entity.OrderInvoice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单发票Dao接口
 * @author liuhangjun
 * @version 2018-07-29
 */
@Mapper
public interface OrderInvoiceDao extends CrudDao<OrderInvoice> {

}