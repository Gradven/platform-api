/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;

import java.math.BigDecimal;

import com.channelsharing.common.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopSalesData extends BaseEntity {
	private static final long serialVersionUID = -1976686990307922332L;

	private Long shopId;
	private Integer orderCount;
	private BigDecimal salesAmount;

	public ShopSalesData() {
	}

	public ShopSalesData(Long shopId, BigDecimal salesAmount) {
		this.shopId = shopId;
		this.salesAmount = salesAmount;
		this.orderCount = 1;
	}

	public ShopSalesData(Long shopId, BigDecimal salesAmount, Integer orderCount) {
		this.shopId = shopId;
		this.salesAmount = salesAmount;
		this.orderCount = orderCount;
	}
}
