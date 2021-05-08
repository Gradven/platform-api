/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.portal.api.entity.ShopGoods;
import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺代理商品信息Dao接口
 * @author liuhangjun
 * @version 2018-06-12
 */
@Mapper
public interface ShopGoodsDao extends CrudDao<ShopGoods> {

}
