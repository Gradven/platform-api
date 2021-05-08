/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.hongqu.portal.api.entity.GoodsCategory;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类Dao接口
 * @author liuhangjun
 * @version 2018-06-27
 */
@Mapper
public interface GoodsCategoryDao extends CrudDao<GoodsCategory> {

}
