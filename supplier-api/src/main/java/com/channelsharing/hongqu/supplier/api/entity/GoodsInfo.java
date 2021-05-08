/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.entity;

import java.math.BigDecimal;

import com.channelsharing.common.entity.BaseEntity;
import lombok.Data;

/**
 * 商品信息接口Entity
 * @author liuhangjun
 * @version 2018-06-06
 */
@Data
public class GoodsInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	// DO 字段
	private String sn;		// 商品编号
	private String cover;   // 封面图片
	private String picUrls;		// 图片
	private Long categoryId;		// 分类id
	private String categoryName;		// 分类名
	private Long supplierId;		// 供应商id
	private String name;		// 商品名称
	private String keywords;		// 商品关键字
	private Integer salesVolume;		// 销售量
	private BigDecimal retailPrice;		// 零售价
	private BigDecimal unitPrice;		// 单价
	private String unit;		// 商品单位
	private BigDecimal profit;		// 利润
	private Integer onSaleFlag;		// 是否上架：0:下架，1:上架
	private String service;       // 基础服务,1:7天无理由退换货
	private Integer approveStatus; // 审核状态，1:审核中，2:审核通过，3：审核不通过
	private String shortDescription;		// 商品短介绍
	private Long createSuId;		// 供应商创建者id
	private Long updateSuId;		// 供应商修改者id
	private Integer storeNumber;  // 库存
	private String invoiceType; // 发票类型，1：普通发票，2：增值税发票。示例：1,2 (以逗号分割)
	
	// 详情表的字段
	private String description; // 商品介绍



	public GoodsInfo() {
		super();
	}


}
