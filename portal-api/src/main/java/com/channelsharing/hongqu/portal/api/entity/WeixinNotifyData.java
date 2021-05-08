/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 微信回调数据记录Entity
 * @author liuhangjun
 * @version 2018-03-29
 */
@Data
public class WeixinNotifyData extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String orderSn;		// 订单号
	private String content;		// 内容


	public WeixinNotifyData() {
		super();
	}


}
