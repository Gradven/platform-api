/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.supplier.api.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单信息Dao接口
 * @author liuhangjun
 * @version 2018-07-01
 */
@Mapper
public interface OrderInfoDao extends CrudDao<OrderInfo> {

}
