/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 店铺技术服务费订单Entity
 * @author liuhangjun
 * @version 2018-06-18
 */
@Data
public class OrderShopServe extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String sn;		// 订单序列号
	private Long shopId;		// 店铺id
	private Long userId;		// 支付者
	private BigDecimal fee;		// 支付费用
	private Integer status;     // 订单状态
	private Integer payType;    // 1：微信，2：支付宝
	private Integer payStatus;		// 技术服务费支付状态，0:未支付，1:已支付
	private BigDecimal payMoney; // 实际支付费用
	private Date payTime; // 实际支付费用
	private String payNo; // 第三方支付订单号
	private Integer cancelType;  // 取消类型
	private Date cancelTime;    // 取消时间
	private String remark;     // 备注
	
	private Long expiredSecond; // 过期时间按照秒算
	
	// 查询条件
	private Date beginCreateTime;
	private Date endCreateTime;


	public OrderShopServe() {
		super();
	}


}
