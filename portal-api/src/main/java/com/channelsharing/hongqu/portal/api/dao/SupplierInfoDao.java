/**
 * Copyright &copy; 2016-2022 供应商信息 All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.portal.api.entity.SupplierInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 供应商信息Dao接口
 * @author 供应商信息
 * @version 2018-08-08
 */
@Mapper
public interface SupplierInfoDao extends CrudDao<SupplierInfo> {

}