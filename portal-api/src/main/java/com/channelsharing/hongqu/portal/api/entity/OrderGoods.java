/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 订单的商品信息Entity
 * @author liuhangjun
 * @version 2018-06-20
 */
@Data
public class OrderGoods extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String orderSn;		// 订单编号
	private Long goodsId;		// 商品id
	private String goodsName;		// 商品名称
	private String goodsSn;		// 商品序列号
	private Long productId;		// 产品Id
	private Integer goodsNumber;		// 商品数量
	private String goodsSpecificationNameValue;		// 商品规格详情
	private String goodsSpecificationIds;		// 商品规格Ids
	private BigDecimal retailPrice;		// 单价
	private BigDecimal unitPrice;		// 市场价
	private BigDecimal profit;		// 利润
	private BigDecimal amountProfit;  // 利润总价
	private BigDecimal amountRetail;      // 产品零售总价
	private Integer payStatus;   // 支付状态
	private Long userId;		// 购买用户
	private Long supplierId;		// 供应商id
	private Long shopId;		// 店铺id
	private String picUrl;		// 图片链接
	private Integer shippingCode;		// 快递公司id
	private String shippingName;		// shipping_name
	private BigDecimal shippingFee;		// 运费
	private String shippingNo;		// 运单号
	private Integer shippingStatus;  // 运送状态，0:未发货，1:已发货，2:已签收
	private Date confirmTime;      // 确认收货时间
	private Date deliveryTime;     // 发货时间
	
	private Date beginDeliveryTime;		// 开始 发货时间
	private Date endDeliveryTime;		// 结束 发货时间
	
	private Date beginCreateTime;		// 开始 创建时间
	private Date endCreateTime;		// 结束 创建时间
	
	private Integer[] shippingStatusArray;  // 状态数组
	
	private Long supplierServiceId;  // 售后id
	private Integer applyServiceFlag; // 是否可以申请售后， 0：不可以，1：可以
	
	// 订单发票
	private OrderInvoice orderInvoice;


	public OrderGoods() {
		super();
	}


}
