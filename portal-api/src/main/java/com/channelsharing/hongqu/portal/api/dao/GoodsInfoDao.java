/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.hongqu.portal.api.entity.GoodsInfo;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品信息Dao接口
 * @author liuhangjun
 * @version 2018-06-11
 */
@Mapper
public interface GoodsInfoDao extends CrudDao<GoodsInfo> {
    
    /**
     * 查询数据量
     * @param entity
     * @return
     */
    Integer findAllCountByShop(@Param("entity") GoodsInfo entity, @Param("shopId") Long ShopId);
    
    /**
     * 分页查询数据列表
     *
     * @param entity
     * @return
     */
    List<GoodsInfo> findListByShop(@Param("entity") GoodsInfo entity, @Param("shopId") Long ShopId);
    
    /**
     * 增加销量
     * @param addNum
     * @param id 商品id
     */
    void addSalesVolume(@Param("addNum")  Integer addNum, @Param("id")  Long id);
    
    /**
     * 增加库存
     * @param addNum
     * @param id
     */
    void addStoreNumber(@Param("addNum")  Integer addNum, @Param("id")  Long id);
}
