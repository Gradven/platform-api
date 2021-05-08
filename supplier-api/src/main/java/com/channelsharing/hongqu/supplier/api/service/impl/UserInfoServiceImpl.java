/**
 * Copyright &copy; 2016-2022 liuahangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service.impl;

import com.channelsharing.hongqu.supplier.api.dao.UserInfoDao;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.supplier.api.service.UserInfoService;
import com.channelsharing.hongqu.supplier.api.entity.UserInfo;

/**
 * 用户信息Service
 * @author liuahangjun
 * @version 2018-03-12
 */
@Service
public class UserInfoServiceImpl extends CrudServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {
    
    @Override
    public UserInfo findOne(Long id) {
        UserInfo entity = new UserInfo();
        entity.setId(id);
        
        return super.findOne(entity);
    }

}
