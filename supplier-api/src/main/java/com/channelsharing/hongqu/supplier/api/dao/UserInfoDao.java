/**
 * Copyright &copy; 2016-2022 liuahangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.dao;

import com.channelsharing.hongqu.supplier.api.entity.UserInfo;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息Dao接口
 * @author liuahangjun
 * @version 2018-03-12
 */
@Mapper
public interface UserInfoDao extends CrudDao<UserInfo> {

}
