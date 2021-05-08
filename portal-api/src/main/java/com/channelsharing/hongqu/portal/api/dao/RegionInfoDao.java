/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.portal.api.entity.RegionInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 国家地区信息Dao接口
 * @author liuhangjun
 * @version 2018-07-16
 */
@Mapper
public interface RegionInfoDao extends CrudDao<RegionInfo> {

}
