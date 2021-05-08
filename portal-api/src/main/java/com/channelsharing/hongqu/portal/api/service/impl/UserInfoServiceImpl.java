/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;


import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.hongqu.portal.api.enums.UserStatus;
import com.channelsharing.hongqu.portal.api.service.UserInfoService;
import com.channelsharing.cloud.sms.SmsSenderFactory;
import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.exception.BadRequestException;
import com.channelsharing.common.exception.ForbiddenException;
import com.channelsharing.common.exception.NotFoundException;
import com.channelsharing.common.exception.OccupiedException;
import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.common.utils.DateUtils;
import com.channelsharing.common.utils.EmailUtil;
import com.channelsharing.common.utils.IdGen;
import com.channelsharing.common.utils.RandomUtil;
import com.channelsharing.hongqu.portal.api.dao.UserInfoDao;
import com.channelsharing.hongqu.portal.api.enums.AccountType;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 用户信息Service
 * @author liuhangjun
 * @version 2017-06-15
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class UserInfoServiceImpl extends CrudServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;

    @Resource
    private EmailUtil emailUtil;

    private static final String MSG_CONTENT = "{verificationCode}";

    /**
     *
     * @param userId
     * @return
     */
    @Cacheable(value =  PORTAL_CACHE_PREFIX + "userInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'userInfo:id:' + #userId", unless = "#result == null")
    public UserInfo findOne(Long userId){

        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
    
        UserInfo userInfoResult = super.findOne(userInfo);
    
        if (userInfoResult == null){
            return null;
        }

       return userInfoResult;

    }



    public UserInfo findOne(Long id, Long currentUserId){
        UserInfo model = new UserInfo();
        model.setId(id);
        UserInfo userInfo = super.findOne(model);

        if (userInfo == null)
            return new UserInfo();
    
        userInfo.setActivationCode(null);

        return userInfo;
    }


    /**
     * 检查昵称是否被占用
     * @param userInfo
     * @param nickname
     */
    public void isNicknameOccupied(UserInfo userInfo, String nickname){

        if (super.dao.isNicknameOccupied(userInfo != null ? userInfo.getId() : null, nickname)) {
            throw new OccupiedException("昵称", nickname);
        }

    }

    /**
     * 检查email是否被占用
     * @param userInfo
     * @param email
     */
    public void isEmailOccupied(UserInfo userInfo, String email){
        if (super.dao.isEmailOccupied(userInfo != null ? userInfo.getId() : null, email)) {
            throw new OccupiedException("邮箱地址", email);
        }
    }
    

    /**
     * 检查此邮箱是否有注册
     * @param email
     * @return
     */
    public boolean isExistEmail(String email){
        return super.dao.isEmailOccupied(null, email);
    }

    /**
     * 检查此手机号码是否有注册
     * @param mobile
     * @return
     */
    public boolean isExistMobile(String mobile){
        return super.dao.isMobileOccupied(null, mobile);
    }



	/**
	 * 检查mobile是否被占用
	 *
	 * @param userInfo
	 * @param mobile
	 */
	public void isMobileOccupied(UserInfo userInfo, String mobile) {
		if (super.dao.isMobileOccupied(userInfo != null ? userInfo.getId() : null, mobile)) {
			throw new OccupiedException("手机号码", mobile);
		}
	}

	/**
	 * 以邮件的方式发送激活码
	 *
	 * @param userInfo
	 */
    @Cacheable(value =  PORTAL_CACHE_PREFIX + "userInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'userInfo:id:' + #userInfo.id", unless = "#result == null")
	@Transactional
	public void sendActivationCode(@NotNull UserInfo userInfo) {
		if (userInfo.getEmail() != null) {
			this.isEmailOccupied(null, userInfo.getEmail());
			UserInfo entity = new UserInfo();
			entity.setEmail(userInfo.getEmail());
			entity.setAccountType(AccountType.email.getCode());
			entity.setStatus(UserStatus.forbidden.getCode());

			String activationCode = RandomUtil.getRandomNumString(6);
			entity.setActivationCode(activationCode);
			this.add(entity);

			String today = DateUtils.getDate();
			String content = "亲爱的" + Constant.APP_NAME + "用户，您好！\n" + "您本次操作的激活码是" + activationCode
					+ "，请输入后继续操作。\n" + "如果您没有进行过操作，请忽略此邮件。此邮件为自动发布，无需回复。\n" + "\n" + Constant.APP_NAME + "\n" + today;

			emailUtil.sendSimpleMail(userInfo.getEmail(), Constant.APP_NAME + "激活码", content);
		} else {
			this.isMobileOccupied(null, userInfo.getMobile());
			UserInfo entity = new UserInfo();
			entity.setMobile(userInfo.getMobile());
			entity.setAccountType(AccountType.mobile.getCode());
			entity.setStatus(UserStatus.forbidden.getCode());
			entity.setActivationCode(RandomUtil.getRandomNumString(6));
			this.add(entity);

            SmsSenderFactory.getSmsSender().sendSms(entity.getMobile(),
                    StringUtils.replace(MSG_CONTENT, "{verificationCode}", entity.getActivationCode()));

		}
	}


	/**
	 * 找回密码时，发送验证码
	 *
	 * @param userInfo
	 */
    @Cacheable(value =  PORTAL_CACHE_PREFIX + "userInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'userInfo:id:' + #userInfo.id", unless = "#result == null")
    public void sendVerifyCode(@NotNull UserInfo userInfo) {
		if (userInfo.getEmail() != null) {
			if (!this.isExistEmail(userInfo.getEmail()))
				throw new NotFoundException("邮箱不存在，请注册此邮箱");

			UserInfo entity = new UserInfo();
			entity.setEmail(userInfo.getEmail());
			entity.setAccountType(AccountType.email.getCode());

			String activationCode = RandomUtil.getRandomNumString(6);
			entity.setActivationCode(activationCode);

			super.dao.update(entity);

			String today = DateUtils.getDate();
			String content = "亲爱的" + Constant.APP_NAME + "用户，您好！\n" + "您本次操作的验证码是" + activationCode
					+ "，请输入后继续操作。\n" + "如果您没有进行过操作，请忽略此邮件。此邮件为自动发布，无需回复。\n" + "\n" + Constant.APP_NAME + "\n" + today;

			emailUtil.sendSimpleMail(userInfo.getEmail(), Constant.APP_NAME + "验证码", content);
		} else {
			if (!this.isExistMobile(userInfo.getMobile()))
				throw new NotFoundException("手机号不存在，请注册此手机号");

			UserInfo entity = new UserInfo();
			entity.setMobile(userInfo.getMobile());
			entity.setAccountType(AccountType.mobile.getCode());
			entity.setActivationCode(RandomUtil.getRandomNumString(6));
			super.dao.update(entity);

            SmsSenderFactory.getSmsSender().sendSms(entity.getMobile(),
                    StringUtils.replace(MSG_CONTENT, "{verificationCode}", entity.getActivationCode()));

        }
	}

    /**
     * 激活用户
     * @param userInfo
     */
    @Cacheable(value =  PORTAL_CACHE_PREFIX + "userInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'userInfo:id:' + #result.id", unless = "#result == null")
    @Transactional
	public UserInfo activateUser(@NotNull UserInfo userInfo) {
		UserInfo entity = new UserInfo();
		entity.setEmail(userInfo.getEmail());
		entity.setMobile(userInfo.getMobile());

		UserInfo retEntity = super.dao.findOne(entity);

		if (retEntity == null) {
			throw new BadRequestException("此用户不存在");
		}

		if (retEntity.getStatus().equals(UserStatus.activated.getCode())) {
			throw new BadRequestException("此用户已被激活,可重置密码");
		}

		if (retEntity != null) {
			String activationCode = retEntity.getActivationCode();
			if (StringUtils.isBlank(activationCode))
				throw new ForbiddenException("请先获取验证码！");

			if (!StringUtils.equals(activationCode, userInfo.getActivationCode()))
				throw new BadRequestException("验证码不正确，请重新输入！");

		}

		entity.setStatus(UserStatus.activated.getCode());
		super.dao.update(entity);

		return retEntity;
	}

    /**
     * 验证用户
     * @param userInfo
     */
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "userInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'userInfo:id:' + #result.id")
    @Transactional
    public UserInfo verifyUser(@NotNull UserInfo userInfo){
		UserInfo entity = new UserInfo();
		entity.setEmail(userInfo.getEmail());
		entity.setMobile(userInfo.getMobile());

        UserInfo retEntity = super.dao.findOne(entity);

        if (retEntity != null){
            String verifyCode = retEntity.getActivationCode();
            if (StringUtils.isBlank(verifyCode))
                throw new ForbiddenException("请先获取验证码！");

            if (!StringUtils.equals(verifyCode, userInfo.getActivationCode()))
                throw new BadRequestException("验证码不正确，请重新输入！");
        }

        entity.setActivationCode("");
        entity.setStatus(UserStatus.activated.getCode());
        super.dao.update(entity);

        return retEntity;
    }

    /**
     * 用户账户登录逻辑
     * 1、用户名不存在，则提醒用户名密码错误
     * 2、用户未激活，则提醒先激活用户
     * 3、用户登录错误次数过多，请找回密码
     * 4、登录成功，返回用户信息
     * 5、登录错误次数归零
     * @param userInfo
     */
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "userInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'userInfo:id:' + #result.id")
    @Transactional
    public UserInfo login(@NotNull UserInfo userInfo){

        UserInfo retUserInfo = null;

        if (userInfo.getEmail() != null || userInfo.getMobile() != null ) {
            retUserInfo = super.findOne(userInfo);
        }

        if (retUserInfo == null)
            throw new BadRequestException("用户名或密码错误");

        if (retUserInfo.getStatus() == UserStatus.forbidden.getCode())
            throw new BadRequestException("未激活");

        //允许的最大错误登录次数
        int maxLoginErrorTimes = 5;
        if (retUserInfo.getLoginErrorTimes() >= maxLoginErrorTimes)
            throw new ForbiddenException("密码错误次数已超过" + maxLoginErrorTimes + "次，请选择找回密码");

        //比较密码是否可以对应上
        String password = retUserInfo.getPassword();
        String reqPassword = DigestUtils.sha512Hex(userInfo.getPassword());

        logger.debug("Request password is: [{}]; digest password is: [{}] ", userInfo.getPassword(), reqPassword);

        if (!StringUtils.equals(reqPassword, password)) {
            UserInfo updateErrorTimes = new UserInfo();
            updateErrorTimes.setLoginErrorTimes(retUserInfo.getLoginErrorTimes() + 1);
            updateErrorTimes.setId(retUserInfo.getId());
            super.dao.update(updateErrorTimes);
            throw new BadRequestException("用户名或密码错误");
        }


        super.dao.clearLoginErrorTimes(userInfo);

        return retUserInfo;
    }


    /**
     * 第三方用户登录处理逻辑
     * 先根据第三方的id判断数据库中是否有此用户，
     * 如果有那么直接返回用户信息，
     * 如果没有，那么插入一条用户信息到表中，然后返回此用户信息
     *
     * @param userInfo
     * @return
     */
    @Transactional
    public UserInfo thirdLogin(@NotNull UserInfo userInfo){
        UserInfo retUserInfo;

        UserInfo thirdUser = new UserInfo();
        thirdUser.setThirdPartyUserId(userInfo.getThirdPartyUserId());
        //首先查询是否有此用户
        retUserInfo = super.findOne(thirdUser);

        if (retUserInfo == null){

            //如果昵称重复，那么在昵称后面加个4位长的随机数
            String nickname = userInfo.getNickname();
            if (super.dao.isNicknameOccupied(null, nickname)){
                nickname = nickname + "_" + StringUtils.substring(IdGen.uuid(), 0, 4);
                userInfo.setNickname(nickname);
            }

            //首次登录为激活状态
            userInfo.setStatus(UserStatus.activated.getCode());

            //新用户插入用户表
            super.add(userInfo);

            retUserInfo = super.findOne(thirdUser);
        }


        return retUserInfo;
    }

    /**
     * 更新用户信息
     * @param userInfo
     */
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "userInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'userInfo:id:' + #userInfo.id")
    @Transactional
    @Override
    public void modify(@NotNull UserInfo userInfo){

        this.isEmailOccupied(userInfo, userInfo.getEmail());
        this.isNicknameOccupied(userInfo, userInfo.getNickname());

        super.dao.update(userInfo);
    }

    /**
     * 初始化用户，只要昵称和密码
     * @param userInfo
     */
    @Transactional
    public void initUserInfo(UserInfo userInfo){
        this.isNicknameOccupied(userInfo, userInfo.getNickname());
        String password = DigestUtils.sha512Hex(userInfo.getPassword());
        userInfo.setPassword(password);
        super.dao.update(userInfo);

    }

    /**
     * 修改用户密码
     * 先判断旧密码是否可以对应上
     * @param userId
     * @param oldPassword
     * @param newPassword
     */
    @Transactional
    public void modifyPassword(Long userId, String oldPassword, String newPassword){
        UserInfo entity = new UserInfo();
        entity.setId(userId);

        UserInfo retUserInfo = super.dao.findOne(entity);

        if (!DigestUtils.sha512Hex(oldPassword).equals(retUserInfo.getPassword()))
            throw new BadRequestException("原始密码错误");
        else {
            entity.setPassword(DigestUtils.sha512Hex(newPassword));
            super.dao.update(entity);
        }
    }


    /**
     * 重置密码功能，
     * 说明：验证码的数据库字段与激活码是同一个字段
     * 更新密码，并把验证码（激活码）字段设置为null
     * 验证码可以请求多次，这里没有做次数限制，以后再补充
     * @param userInfo
     */
    @Transactional
	public void resetPassword(UserInfo userInfo) {
		userInfo.setPassword(DigestUtils.sha512Hex(userInfo.getPassword()));
		userInfo.setActivationCode(null);
		super.dao.update(userInfo);
	}

    /**
     * 根据ids获取到用户数据
     * @param ids
     * @return
     */
    public List<UserInfo> findByIds(List<Long> ids){

        return super.dao.findByIds(ids);
    }

}
