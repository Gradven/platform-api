/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 用户认证信息Entity
 * @author liuhangjun
 * @version 2018-07-12
 */
@Data
public class UserCertificate extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long userId;		// 用户id
	private String realName;		// 真实姓名
	private String idCard;		// 身份证号
	private String cardFront;		// 身份证正面
	private String cardBack;		// 身份证背面
	private Integer approveStatus;		// 审核状态：0:未审核，1:审核通过，2:审核不通过
	private String address;		// 地址
	private Integer sex;		// 1:男，2:女
	private String birth;		// 生日，例如20001211
	private String nationality;		// 名族，例如：汉
	private String startDate;		// 有效期起始时间
	private String endDate;		// 有效期结束时间
	private String issue;		// 签发机关


	public UserCertificate() {
		super();
	}


}
