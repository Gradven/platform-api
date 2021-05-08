/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 订单信息Entity
 * @author liuhangjun
 * @version 2018-06-20
 */
@Data
public class OrderInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String sn;		// 订单编号
	private Long userId;		// 购买用户id
	private String consignee;		// 收货人
	private String country;		// 国家
	private String province;		// 省份
	private String city;		// 城市
	private String district;		// 街区
	private String address;		// 地址
	private String mobile;		// 手机号码
	private BigDecimal amount;  // 订单总额
	private Integer status;		// 订单状态，0,:未支付, 1:已支付, 2:已完成 99:已取消;
	private Integer payStatus;		// 支付状态
	private Integer payType;		// 支付类型：1:微信支付，2:支付宝支付
	private String payNo;		// 支付流水号
	private Date payTime;		// 支付时间
	private BigDecimal payMoney;  // 支付金额
	private Integer cancelType;		// 取消支付原因：0,未取消，1:用户主动取消，2:支付超时取消，3:微信支付失败，系统自动取消
	private Date cancelTime;		// 取消时间
	private String remark;		// 备注
	
	
	private Long expiredSecond; // 过期时间按照秒算
	
	// 查询条件
	private Date beginCreateTime;
	private Date endCreateTime;
	
	// 插入信息
	private List<OrderGoods> orderGoodsList;
	
	


	public OrderInfo() {
		super();
	}


}
