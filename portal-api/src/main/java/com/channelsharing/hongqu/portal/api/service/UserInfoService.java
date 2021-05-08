/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;


import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.common.service.CrudService;

import java.util.List;


/**
 * 用户信息Service
 * @author liuhangjun
 * @version 2017-06-15
 */
public interface UserInfoService extends CrudService<UserInfo> {

    void isNicknameOccupied(UserInfo userInfo, String nickname);

    void isEmailOccupied(UserInfo userInfo, String email);
    
    void isMobileOccupied(UserInfo userInfo, String mobile);

    void sendActivationCode(UserInfo userInfo);

    UserInfo activateUser(UserInfo userInfo);

    UserInfo verifyUser(UserInfo userInfo);

    UserInfo login(UserInfo userInfo);

    UserInfo thirdLogin(UserInfo userInfo);

    void initUserInfo(UserInfo userInfo);

    void modifyPassword(Long userId, String oldPassword, String newPassword);

    void resetPassword(UserInfo userInfo);

    void sendVerifyCode(UserInfo userInfo);

    List<UserInfo> findByIds(List<Long> ids);

    UserInfo findOne(Long id, Long currentUserId);

    UserInfo findOne(Long id);



}
