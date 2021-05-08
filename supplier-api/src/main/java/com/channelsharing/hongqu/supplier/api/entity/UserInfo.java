/**
 * Copyright &copy; 2016-2022 liuahangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.channelsharing.common.entity.BaseEntity;
import lombok.Data;

/**
 * 用户信息Entity
 * @author liuahangjun
 * @version 2018-03-12
 */
@Data
public class UserInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String nickname;		// 用户昵称
	private String mobile;		// 手机号码
	private String email;		// 电子邮箱
	private String address;		// 地址
	private String company;		// 工作单位
	private String contact;		// 联系方式
	@JsonIgnore
	private String password;		// 密码
	private Integer loginErrorTimes;		// 登录错误次数
	private Integer sex;		// 性别(1:男，2:女)
	private String avatar;		// 头像地址
	private Integer status;		// 状态(0:待激活,1:已激活)
	private String activationCode;		// 激活码
	private String thirdPartyUserId;		// 第三方账号
	private Integer accountType;		// 账号类型（1:邮箱，2:手机号码，3:微信，4:微博，5:qq,）
	private String country;         // 国家
	private String province;		// 省份地区
	private String city;            // 城市
	private String description;		// 用户简介
	
	private Long shopId;


	public UserInfo() {
		super();
	}


}
