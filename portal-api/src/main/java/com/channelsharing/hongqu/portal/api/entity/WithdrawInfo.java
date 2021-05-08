/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;

import java.math.BigDecimal;

import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 提现记录Entity
 *
 * @author liuhangjun
 * @version 2018-06-26
 */
@Data
public class WithdrawInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long userId; // 提现人
	private Long shopId; // 店铺id
	private BigDecimal amount; // 提现金额
	private Integer status; // 状态：1:提现成功，2：提现中，99:提现失败
	private Integer type; // 提现类型：1:提现到微信，2:提现到银行卡
	private String remark; // 备注
	private String tradeNo; // 交易流水号

	private String openId; // 微信提现用到openId
	private String clientIp; // 微信提现用到ip
	private String paymentNo; // 微信订单号
	private String paymentTime; // 微信企业付款成功时间

	public WithdrawInfo() {
		super();
	}
}
