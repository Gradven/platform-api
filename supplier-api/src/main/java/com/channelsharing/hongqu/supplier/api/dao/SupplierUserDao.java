/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.supplier.api.entity.SupplierUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 供应商用户Dao接口
 * @author liuhangjun
 * @version 2018-02-02
 */
@Mapper
public interface SupplierUserDao extends CrudDao<SupplierUser> {

}
