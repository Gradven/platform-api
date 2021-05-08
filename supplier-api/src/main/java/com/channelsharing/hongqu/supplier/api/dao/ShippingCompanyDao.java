/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.dao;

import com.channelsharing.hongqu.supplier.api.entity.ShippingCompany;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 物流公司信息Dao接口
 * @author liuhangjun
 * @version 2018-07-03
 */
@Mapper
public interface ShippingCompanyDao extends CrudDao<ShippingCompany> {

}
