/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.hongqu.portal.api.entity.GoodsDescription;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品介绍内容Dao接口
 * @author liuhangjun
 * @version 2018-06-12
 */
@Mapper
public interface GoodsDescriptionDao extends CrudDao<GoodsDescription> {

}
