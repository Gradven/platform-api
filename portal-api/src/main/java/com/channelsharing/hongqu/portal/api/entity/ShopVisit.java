/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 店铺访问记录Entity
 * @author liuhangjun
 * @version 2018-07-08
 */
@Data
public class ShopVisit extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long userId;		// 用户id
	private Long shopId;		// 店铺id


	public ShopVisit() {
		super();
	}


}
