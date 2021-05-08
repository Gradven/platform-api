/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.portal.api.entity.OrderShopServe;
import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺技术服务费订单Dao接口
 * @author liuhangjun
 * @version 2018-06-18
 */
@Mapper
public interface OrderShopServeDao extends CrudDao<OrderShopServe> {

}
