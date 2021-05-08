/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;


import com.channelsharing.common.exception.BadRequestException;
import com.channelsharing.common.exception.DataNotFoundException;
import com.channelsharing.common.exception.ForbiddenException;
import com.channelsharing.hongqu.portal.api.constant.ConfigParamConstant;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.dao.UserInfoDao;
import com.channelsharing.hongqu.portal.api.entity.InvitationCodeOper;
import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.hongqu.portal.api.service.ConfigParamService;
import com.channelsharing.hongqu.portal.api.service.InvitationCodeOperService;
import com.channelsharing.hongqu.portal.api.service.UserBindMobileService;
import com.channelsharing.hongqu.portal.api.service.UserInfoService;
import com.channelsharing.cloud.sms.SmsSenderFactory;
import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.common.utils.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 用户信息Service
 * @author liuhangjun
 * @version 2017-06-15
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class UserBindMobileServiceImpl extends CrudServiceImpl<UserInfoDao, UserInfo> implements UserBindMobileService {
    
    @Autowired
    private UserInfoService userInfoService;
    
    @Autowired
    private InvitationCodeOperService invitationCodeOperService;
    
    @Autowired
    private ConfigParamService configParamService;
    
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;
    
    private static final String MSG_CONTENT = "{verificationCode}";
    
    
    @Override
    public UserInfo findOne(Long id) {
        return null;
    }
    
    /**
     * 需要判断此手机号码是否已经被其他用户绑定
     * 需要判断本用户是否已经绑定
     * @param userId
     * @param mobile
     */
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "userInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'userInfo:id:' + #userId")
    @Transactional
    @Override
    public void sendCode(Long userId, String mobile, String invitationCode) {
    
        UserInfo userInfo = userInfoService.findOne(userId);
        if (userInfo.getMobile() != null){
            throw new BadRequestException("您已绑定了手机号码");
        }
    
        // 运营邀请码
        if (StringUtils.length(invitationCode) == Constant.INVITATION_CODE_OPER_LENGTH){
            InvitationCodeOper invitationCodeOper = new InvitationCodeOper();
            invitationCodeOper.setCode(invitationCode);
            InvitationCodeOper result = invitationCodeOperService.findOne(invitationCodeOper);
            if (result != null){
                if (result.getUserId() != null){
                    throw new BadRequestException("此邀请码已经被使用");
                }else {
                    invitationCodeOper.setUserId(userId);
                    invitationCodeOperService.modify(invitationCodeOper);
                }
            }else{
                throw new BadRequestException("邀请码不正确");
            }
        }else if (StringUtils.length(invitationCode) == Constant.INVITATION_CODE_VIP_LENGTH){  // VIP邀请码，在运营管理平台配置
            String vipCode = configParamService.findOne(ConfigParamConstant.PORTAL_INVITATION_VIP_CODE);
            if (vipCode == null || !StringUtils.equalsIgnoreCase(vipCode, invitationCode)){
                throw new BadRequestException("邀请码不正确");
            }
        }else {
            throw new BadRequestException("邀请码不正确");
        }
        
        
    
        userInfoService.isMobileOccupied(userInfo, mobile) ;
    
        String code = RandomUtil.getRandomNumString(6);
        
        SmsSenderFactory.getSmsSender().sendSms(mobile, code);
    
        UserInfo userInfoUpdate = new UserInfo();
        userInfoUpdate.setId(userId);
        userInfoUpdate.setMobile(mobile);
        userInfoUpdate.setActivationCode(code);
        super.dao.bindMobileSendCode(userInfoUpdate);
        
    }
    
    
    /**
     * TODO 需要判断验证码错误次数
     * @param userId
     * @param mobile
     * @param code
     */
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "userInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'userInfo:id:' + #userId")
    @Override
    public UserInfo verifyCode(Long userId, String mobile, String code) {
    
        UserInfo userInfo = userInfoService.findOne(userId);
        
        if (userInfo == null) {
            throw new DataNotFoundException("查不到此用户信息");
        }
    
        String verifyCode = userInfo.getActivationCode();
        if (StringUtils.isBlank(verifyCode))
            throw new ForbiddenException("请先获取验证码！");
    
        if (!StringUtils.equals(verifyCode, userInfo.getActivationCode())){
            throw new BadRequestException("验证码不正确，请重新输入！");
        }else{
            userInfo.setActivationCode("");
            userInfoService.modify(userInfo);
        }
        
        return userInfo;
        

    }
}
