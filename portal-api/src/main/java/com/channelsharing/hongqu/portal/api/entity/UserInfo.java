/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.channelsharing.common.entity.BaseEntity;
import lombok.Data;

/**
 * 供应商用户Entity
 * @author liuhangjun
 * @version 2018-02-02
 */
@Data
public class UserInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String nickname;		// 用户姓名
	@JsonIgnore
	private String password;		// 登录密码
	private Integer age;		// 年龄
	private Integer sex;		// 性别
	private String account;		// 登录名
	private String mobile;		// 手机号码
	private String email;		// 邮箱
	private Integer status;		// 用户状态
	private Integer accountType;
	private String country;
	private String province;
	private String city;
	private String description;
	private String avatar;
	private Integer loginErrorTimes;
	private String activationCode;
	private String thirdPartyUserId;
	private String address;
	private String company;
	private String contact;
	private Long shopId;        // 店铺id
	
	private Integer shopRightFlag;   // 店主权益标记，0：没有权益，1：有权益
	
	

	public UserInfo() {
		super();
	}



}
