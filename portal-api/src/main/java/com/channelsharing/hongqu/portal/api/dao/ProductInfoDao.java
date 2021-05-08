/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.portal.api.entity.ProductInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

/**
 * 商品的产品信息Dao接口
 * @author liuhangjun
 * @version 2018-06-20
 */
@Mapper
public interface ProductInfoDao extends CrudDao<ProductInfo> {
    
    void addSalesVolume(@Param("addNum")  Integer addNum, @Param("id")  Long id);
    
    void addStoreNumber(@Param("addNum")  Integer addNum, @Param("id")  Long id);
    
    Integer sumGoodsStore(@Param("goodsId") Long goodsId);

}
