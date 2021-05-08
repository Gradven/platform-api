/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.hongqu.portal.api.entity.OrderGoods;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单的商品信息Dao接口
 * @author liuhangjun
 * @version 2018-06-20
 */
@Mapper
public interface OrderGoodsDao extends CrudDao<OrderGoods> {

    void autoConfirm(OrderGoods entity);

}
