package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.common.service.CrudService;

/**
 * Created by liuhangjun on 2018/6/29.
 */
public interface UserBindMobileService extends CrudService<UserInfo> {
    
    void sendCode(Long userId, String mobile, String invitationCode);
    
    UserInfo verifyCode(Long userId, String mobile, String code);

}
