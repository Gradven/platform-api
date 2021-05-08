/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;


import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 供应商用户Dao接口
 * @author liuhangjun
 * @version 2018-02-02
 */
@Mapper
public interface UserInfoDao extends CrudDao<UserInfo> {


    boolean isNicknameOccupied(@Param("userId") Long userId, @Param("nickname") String nickname);

    boolean isEmailOccupied(@Param("userId") Long userId, @Param("email") String email);

    boolean isMobileOccupied(@Param("userId") Long userId, @Param("mobile") String mobile);
    
    void clearLoginErrorTimes(UserInfo userInfo);

    List<UserInfo> findByIds(@Param("ids")List<Long> ids);
    
    
    void bindMobileSendCode(UserInfo userInfo);

}
