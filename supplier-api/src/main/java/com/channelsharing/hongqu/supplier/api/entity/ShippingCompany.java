/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.entity;

import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 物流公司信息Entity
 *
 * @author liuhangjun
 * @version 2018-07-03
 */
@Data
public class ShippingCompany extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Integer code; // 代码编号
	private String name; // 物流公司
	private String kuaidi100Com; // 快递100编码

	public ShippingCompany() {
		super();
	}
}
