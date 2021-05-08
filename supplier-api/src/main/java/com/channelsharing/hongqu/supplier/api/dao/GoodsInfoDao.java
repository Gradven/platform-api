/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.supplier.api.entity.GoodsInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品信息接口Dao接口
 * @author liuhangjun
 * @version 2018-06-06
 */
@Mapper
public interface GoodsInfoDao extends CrudDao<GoodsInfo> {


}
