/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 国家地区信息Entity
 * @author liuhangjun
 * @version 2018-07-16
 */
@Data
public class RegionInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String code;		// 编码
	private String parentCode;		// 父编码
	private String codePath;		// 地区路径
	private Integer grade;		// 级别
	private String name;		// 地区名称


	public RegionInfo() {
		super();
	}


}
