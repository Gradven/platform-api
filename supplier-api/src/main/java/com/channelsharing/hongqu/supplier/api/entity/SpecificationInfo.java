/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.entity;

import java.util.Date;

import com.channelsharing.common.entity.BaseEntity;
import lombok.Data;

/**
 * 规格维度接口Entity
 * @author liuhangjun
 * @version 2018-06-07
 */
@Data
public class SpecificationInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String name;		// 规格维度名称
	private Integer sortOrder;		// 排序
	private Date createTime;		// 创建时间
	private Date updateTime;		// 修改时间


	public SpecificationInfo() {
		super();
	}


}
