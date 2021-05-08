/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.portal.api.entity.ConfigParam;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统配置项Dao接口
 * @author liuhangjun
 * @version 2018-06-17
 */
@Mapper
public interface ConfigParamDao extends CrudDao<ConfigParam> {

}
