/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.hongqu.portal.api.entity.ShopInfo;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 店铺信息Dao接口
 * @author liuhangjun
 * @version 2018-06-11
 */
@Mapper
public interface ShopInfoDao extends CrudDao<ShopInfo> {
    
    ShopInfo findByName(@Param("name") String name);
}
