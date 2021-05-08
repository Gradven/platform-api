/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 规格维度接口Entity
 * @author liuhangjun
 * @version 2018-06-20
 */
@Data
public class SpecificationInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String name;		// 规格维度名称
	private Integer sortOrder;		// 排序


	public SpecificationInfo() {
		super();
	}


}
