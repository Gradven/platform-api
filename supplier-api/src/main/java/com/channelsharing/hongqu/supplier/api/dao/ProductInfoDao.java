/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.dao;

import com.channelsharing.hongqu.supplier.api.entity.ProductInfo;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 产品信息Dao接口
 * @author liuhangjun
 * @version 2018-06-07
 */
@Mapper
public interface ProductInfoDao extends CrudDao<ProductInfo> {
    
    Integer sumGoodsStore(@Param("goodsId") Long goodsId);

}
