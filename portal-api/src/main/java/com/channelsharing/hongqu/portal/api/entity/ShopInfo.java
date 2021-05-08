/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

import java.util.Date;

/**
 * 店铺信息Entity
 * @author liuhangjun
 * @version 2018-06-11
 */
@Data
public class ShopInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String name;		// 店铺名称
	private String logo;		// 店铺logo
	private Long userId;		// 创建人
	private Integer certificateFlag;		// 是否认证,0:未认证，1:已认证
	private String description;  // 描述
	private Integer payFeeFlag;  // 是否缴费，0：未缴费，1：已经缴费
	private Date expireTime;   // 过期时间
	private String backgroundUrl; // 背景图
	
	private String exactMatchName; // 精确匹配名字
	
	private UserInfo userInfo;


	public ShopInfo() {
		super();
	}


}
