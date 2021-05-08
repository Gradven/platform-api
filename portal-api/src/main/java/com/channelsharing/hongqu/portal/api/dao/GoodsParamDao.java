/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.portal.api.entity.GoodsParam;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品参数介绍Dao接口
 * @author liuhangjun
 * @version 2018-07-29
 */
@Mapper
public interface GoodsParamDao extends CrudDao<GoodsParam> {

}