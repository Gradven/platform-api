/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.supplier.api.entity.GoodsSpecification;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品规格值Dao接口
 * @author liuhangjun
 * @version 2018-06-07
 */
@Mapper
public interface GoodsSpecificationDao extends CrudDao<GoodsSpecification> {

}
