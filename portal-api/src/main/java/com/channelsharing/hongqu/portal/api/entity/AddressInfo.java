/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 用户地址信息Entity
 * @author liuhangjun
 * @version 2018-07-16
 */
@Data
public class AddressInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long userId;		// 用户id
	private String consignee;   // 收货人
	private String country;		// 国家
	private String province;		// 省份
	private String city;		// 城市
	private String district;		// 街道
	private String address;		// 地址
	private String mobile;		// 手机号码
	private Integer timeType;   // 收货时间类型
	private Integer defaultFlag;		// 是否默认地址：0:否，1:是


	public AddressInfo() {
		super();
	}


}
