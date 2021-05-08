/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.dao;

import com.channelsharing.hongqu.supplier.api.entity.SpecificationInfo;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 规格维度接口Dao接口
 * @author liuhangjun
 * @version 2018-06-07
 */
@Mapper
public interface SpecificationInfoDao extends CrudDao<SpecificationInfo> {

}
