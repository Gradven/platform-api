/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 商品信息Entity
 *
 * @author liuhangjun
 * @version 2018-06-11
 */
@Data
public class GoodsInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	// DO 字段
	private String sn; // 商品编号
	private String cover;  // 封面图片
	private String picUrls; // 图片
	private Long categoryId; // 分类id
	private Long supplierId; // 供应商id
	private String name; // 商品名称
	private String keywords; // 商品关键字
	private Integer salesVolume; // 销售量
	private Integer storeNumber; // 库存
	private String retailPrice; // 零售价
	private String unitPrice; // 单价
	private String unit; // 商品单位
	private String profit; // 利润
	private Integer onSaleFlag; // 是否上架：0:下架，1:上架
	private String service; // 基础服务：1：7天无理由退换货
	private Integer approveStatus; // 审核状态，1:审核中，2:审核通过，3：审核不通过
	private String shortDescription; // 商品短简介
	private String invoiceType; // 发票类型，1：普通发票，2：增值税发票。示例：1,2 (以逗号分割)

	// 其他业务字段
	private String recommend; // 推荐语
	private Integer roleType; // 用户角色，1：用户，2：店主
	private Integer agentFlag; // 是否代理，0： 未代理，1：已代理, -1: 曾经代理
	private Integer likeFlag; // 是否点赞，0：未点赞，1：已经点赞
	private Integer likeCount; // 点赞数

	// 地址信息
	private AddressInfo addressInfo;
	
	// 详情信息
	private GoodsDescription goodsDescription;
	
	// 商品参数介绍
	private List<GoodsParam> goodsParamList;

	// 用来做搜索的字段
	private String orderBy;
	private String direction;
	private BigDecimal minRetailPrice;
	private BigDecimal maxRetailPrice;

	public GoodsInfo() {
		super();
	}
}
