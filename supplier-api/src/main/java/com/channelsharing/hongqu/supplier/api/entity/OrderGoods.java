/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.channelsharing.common.entity.BaseEntity;
import lombok.Data;


/**
 * 订单的商品信息Entity
 * @author liuhangjun
 * @version 2018-07-01
 */
@Data
public class OrderGoods extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String orderSn;		// 订单编号
	private Long goodsId;		// 商品id
	private String goodsName;		// 商品名称
	private String goodsSn;		// 商品序列号
	private Long productId;		// 产品Id
	private String goodsNumber;		// 商品数量
	private String goodsSpecificationNameValue;		// 商品规格详情
	private String goodsSpecificationIds;		// 商品规格Ids
	private BigDecimal retailPrice;		// 单价
	private BigDecimal unitPrice;		// 市场价
	private BigDecimal profit;		// 利润
	private BigDecimal amountProfit;		// 利润总价
	private Long userId;		// 购买用户
	private String userNickname;    // 用户昵称
	private BigDecimal amountRetail;		// 产品总价
	private Long supplierId;		// 供应商id
	private Integer payStatus;		// 支付状态,0:未支付，1:已支付
	private Long shopId;		// 店铺id
	private String picUrl;		// 图片链接
	private Integer shippingCode;		// 快递公司id
	private String shippingName;		// 快递公司名
	private BigDecimal shippingFee;		// 运费
	private Integer shippingStatus;   // 运送状态，0:未发货，1:已发货，2:已签收
	private String shippingNo;		// 运单号
	private Date deliveryTime;      // 发货时间
	
	
	private Date beginCreateTime;		// 开始 创建时间
	private Date endCreateTime;		// 结束 创建时间
	
	private String shopName;      // 店铺名称


	public OrderGoods() {
		super();
	}


}
