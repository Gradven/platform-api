/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 系统配置项Entity
 * @author liuhangjun
 * @version 2018-06-17
 */
@Data
public class ConfigParam extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String configKey;		// 配置项的key
	private String configValue;		// 配置项的value
	private Integer moduleId;		// 系统模块 参考数据字典（sys_modules）
	private String description;		// 描述
	private String remark;		// 备注


	public ConfigParam() {
		super();
	}


}
