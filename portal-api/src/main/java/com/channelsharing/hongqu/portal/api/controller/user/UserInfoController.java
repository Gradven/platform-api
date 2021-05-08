/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.user;


import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.common.exception.ArgumentNotValidException;
import com.channelsharing.common.validator.Validator;
import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户信息Controller
 *
 * @author liuhangjun
 * @version 2017-06-15
 */
@Api(tags = "用户注册、激活、个人信息接口")
@Validated
@RestController
@RequestMapping(value = "/v1/user")
public class UserInfoController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserInfoService userInfoService;


    @ApiOperation(value="获取用户个人信息")
    @GetMapping("/self")
    public UserInfo findOne() {
        UserInfo query = new UserInfo();
        query.setId(super.currentUserId());
        UserInfo entity = userInfoService.findOne(query);
    
        if (entity == null)
            return new UserInfo();
        
        entity.setActivationCode(null);
        

        return entity;
    }

    @ApiOperation(value="获取其他用户个人信息")
    @GetMapping("/other/{id}")
    public UserInfo getOther(@PathVariable Long id) {

        Long currentUserId;
        if (super.currentUserWithoutException() == null){
            currentUserId = null;
        }else {
            currentUserId = super.currentUserWithoutException().getId();
        }

        UserInfo entity = userInfoService.findOne(id, currentUserId);

        if (entity == null)
            entity = new UserInfo();

        //去掉一些不必要展示给其他人的字段
        entity.setLoginErrorTimes(null);
        entity.setMobile(null);
        entity.setEmail(null);
        entity.setActivationCode(null);
        entity.setThirdPartyUserId(null);


        return entity;
    }
    

    /**
     *
     * @param account
     */
    @ApiOperation(value="用户注册，发送验证码到邮箱或手机")
    @GetMapping("/send/{account}/activationCode")
    public void sendActivationCode(@PathVariable String account) {

        UserInfo userInfo = new UserInfo();
        this.validateAccount(account, userInfo);

        userInfoService.sendActivationCode(userInfo);
    }

    /**
     *
     * @param account
     */
    @ApiOperation(value="用户找回密码时，发送验证码到用户的邮箱或手机")
    @GetMapping("/send/{account}/verifyCode")
    public void sendVerifyCode(@PathVariable String account){
        UserInfo userInfo = new UserInfo();
        this.validateAccount(account, userInfo);

        userInfoService.sendVerifyCode(userInfo);
    }

    @ApiOperation(value="昵称判重")
    @GetMapping("/check/nickname")
    public void checkNickname(@RequestParam String nickname) {   // 需要对昵称做 敏感词过滤代码未完成
        userInfoService.isNicknameOccupied(super.currentUserWithoutException(), nickname);

    }

    @ApiOperation(value="邮箱判重")
    @GetMapping("/check/email")
    public void checkEmail(@RequestParam @Email(message = "请输入正确的邮箱格式") String email) {
        userInfoService.isEmailOccupied(super.currentUserWithoutException(), email);

    }

    /**
     * 个人空间更新资料
     *
     * @param updateUserInfoRequestEntity
     */
    @ApiOperation(value="个人空间更新资料")
    @PostMapping
    public UserInfo update(@RequestBody @Validated UserInfoModifyRequestEntity updateUserInfoRequestEntity) {

        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(updateUserInfoRequestEntity, userInfo);
        userInfo.setId(super.currentUserId());

        userInfoService.modify(userInfo);

        return userInfoService.findOne(super.currentUserId());
    }

    /**
     *
     * @param activationCodeRequestEntity
     */
    @ApiOperation(value="用激活码激活用户")
    @PostMapping("/activate")
    public UserInfo activateUser(@RequestBody @Validated UserActivateRequestEntity activationCodeRequestEntity) {

        UserInfo userInfo = new UserInfo();

        String account = activationCodeRequestEntity.getAccount();
        this.validateAccount(account, userInfo);

        BeanUtils.copyProperties(activationCodeRequestEntity, userInfo);
        UserInfo retUserInfo = userInfoService.activateUser(userInfo);

        super.addUserToSession(retUserInfo);

        return retUserInfo;

    }

    /**
     *
     * @param verifyUserRequestEntity
     */
    @ApiOperation(value="用验证码重置用户")
    @PatchMapping("/verify")
    public UserInfo verifyUser(@RequestBody @Validated UserVerifyRequestEntity verifyUserRequestEntity) {

        UserInfo userInfo = new UserInfo();

        String account = verifyUserRequestEntity.getAccount();
        this.validateAccount(account, userInfo);

        BeanUtils.copyProperties(verifyUserRequestEntity, userInfo);
        userInfo.setActivationCode(verifyUserRequestEntity.getVerifyCode());
        UserInfo retUserInfo = userInfoService.verifyUser(userInfo);


        super.addUserToSession(retUserInfo);

        return retUserInfo;

    }

    @ApiOperation(value="修改密码")
    @PutMapping("/password/modify")
    public void modifyPassword(@RequestBody PasswordModifyRequestEntity modifyPasswordRequestEntity) {

        userInfoService.modifyPassword(super.currentUserId(), modifyPasswordRequestEntity.getOldPassword(), modifyPasswordRequestEntity.getNewPassword());

    }

    @ApiOperation(value="重置密码")
    @PutMapping("/password/reset")
    public void resetPassword(@RequestBody @Validated PasswordResetRequestEntity resetPasswordRequestEntity) {

        UserInfo userInfo = new UserInfo();

        userInfo.setId(super.currentUserId());
        userInfo.setPassword(resetPasswordRequestEntity.getPassword());

        userInfoService.resetPassword(userInfo);

    }

    /**
     * 用户初始化，填写昵称和密码
     *
     * @param clientAgent
     * @param initUserRequestEntity
     */
    @ApiOperation(value="用户初始化，填写昵称和密码")
    @PatchMapping("/init")
    public void initUserInfo(@RequestBody @Validated UserInitRequestEntity initUserRequestEntity) {

        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(initUserRequestEntity, userInfo);
        userInfo.setId(super.currentUserId());

        logger.info("Current user id is : {}", super.currentUserId());

        userInfoService.initUserInfo(userInfo);
    }

    /**
     * 校验账户的有效性
     *
     * @param account
     * @param userInfo
     */
    public void validateAccount(String account, UserInfo userInfo) {
        if (Validator.isEmail(account))
            userInfo.setEmail(account);
        else if (Validator.isMobile(account))
            userInfo.setMobile(account);
        else
            throw new ArgumentNotValidException("用户账户格式不正确！");

    }


}
