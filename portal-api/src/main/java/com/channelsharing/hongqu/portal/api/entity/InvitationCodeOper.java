/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import com.channelsharing.common.entity.BaseEntity;
import lombok.Data;

/**
 * 运营邀请码Entity
 * @author liuhangjun
 * @version 2018-07-16
 */
@Data
public class InvitationCodeOper extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long userId;		// 用户id
	private String code;		// 国家


	public InvitationCodeOper() {
		super();
	}


}
