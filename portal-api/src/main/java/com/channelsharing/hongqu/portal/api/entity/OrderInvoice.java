/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 订单发票Entity
 * @author liuhangjun
 * @version 2018-07-29
 */
@Data
public class OrderInvoice extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long orderGoodsId;
	private String orderSn;		// 订单编号
	private Long userId;		// 用户id
	private Integer type;		// 发票类型，1:普通发票，2:增值税发票
	private String title;		// 抬头
	private String taxNo;		// 税号
	private String bankName;		// 开户银行
	private String bankNo;		// 银行账号
	private String address;		// 企业地址
	private String telephone;		// 电话


	public OrderInvoice() {
		super();
	}


}
