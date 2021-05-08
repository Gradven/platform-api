/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;

import java.math.BigDecimal;

import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 店铺钱包Entity
 *
 * @author liuhangjun
 * @version 2018-06-26
 */
@Data
public class ShopWallet extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long shopId; // 店铺id
	private BigDecimal balance; // 余额
	private BigDecimal withdraw; // 提现金额
	private BigDecimal profitAmount; // 店铺总收益
	private Long userId; // 操作用户
	
	private BigDecimal allProfit; // 待入账收益
	private BigDecimal unAvailableProfit; // 待入账收益
	private ShopSalesData shopSalesData; // 店铺销售数据

	public ShopWallet() {
		super();
	}
}
