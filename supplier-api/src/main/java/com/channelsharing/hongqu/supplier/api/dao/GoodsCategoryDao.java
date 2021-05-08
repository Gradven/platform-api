/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.dao;

import com.channelsharing.hongqu.supplier.api.entity.GoodsCategory;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类Dao接口
 * @author liuhangjun
 * @version 2018-06-07
 */
@Mapper
public interface GoodsCategoryDao extends CrudDao<GoodsCategory> {

}
