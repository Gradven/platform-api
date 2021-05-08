/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 店铺收益Entity
 * @author liuhangjun
 * @version 2018-07-17
 */
@Data
public class ShopProfit extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Integer type;		// 收益类型，1：佣金收入，2:拉新返现
	private BigDecimal profit;		// 收益
	private Long shopId;		// 店铺id
	private Long orderGoodsId;  // 订单商品id
	private String sn;		// 订单号
	private String remark;		// 备注
	private Integer confirmFlag;  // 用户确认收货：0：未确认，1:已确认
	private Date confirmTime; // 用户确认收货时间
	private Integer availableFlag; // 可提现标记，0:不可提现，1:可提现
	
	private Date beginConfirmTime;   // 查询收货开始时间
	private Date endConfirmTime;     // 查询收货结束时间


	public ShopProfit() {
		super();
	}


}
