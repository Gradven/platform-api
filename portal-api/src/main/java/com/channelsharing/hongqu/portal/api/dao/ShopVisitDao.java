/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.hongqu.portal.api.entity.ShopVisit;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺访问记录Dao接口
 * @author liuhangjun
 * @version 2018-07-08
 */
@Mapper
public interface ShopVisitDao extends CrudDao<ShopVisit> {

}
