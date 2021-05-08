/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;

import com.channelsharing.common.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingCompany extends BaseEntity {
	private static final long serialVersionUID = 6378106895156750303L;

	private Integer code;
	private String name;
	private String kuaidi100Com;
}
