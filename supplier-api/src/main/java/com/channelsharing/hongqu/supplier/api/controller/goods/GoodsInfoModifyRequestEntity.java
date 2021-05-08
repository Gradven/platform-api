/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.goods;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 商品信息接口Entity
 * @author liuhangjun
 * @version 2018-06-06
 */
@Data
public class GoodsInfoModifyRequestEntity {
	
	
	@ApiModelProperty(value = "商品id", example="1")
	@NotNull
	public Long id;
	
	@Length(min=0, max=255, message="封面图片长度必须介于 0 和 255 之间")
	public String cover;		// 封面图片
	
	@ApiModelProperty(value = "图片", example="[]")
	@Length(min=0, max=4000, message="图片长度必须介于 0 和 4000 之间")
	public String picUrls;		// 图片
	
	@ApiModelProperty(value = "分类id", example="1")
	public Long categoryId;		// 分类id
	
	@ApiModelProperty(value = "商品名称", example="商品名称")
	@Length(min=0, max=128, message="商品名称长度必须介于 1 和 128 之间")
	public String name;		// 商品名称
	
	@ApiModelProperty(value = "商品关键字", example="商品关键字")
	@Length(min=0, max=255, message="商品关键字长度必须介于 0 和 255 之间")
	public String keywords;		// 商品关键字
	
	@ApiModelProperty(value = "零售价", example="99.55")
	public BigDecimal retailPrice;		// 零售价
	
	@ApiModelProperty(value = "单价", example="80.01")
	public BigDecimal unitPrice;		// 单价
	
	
	@ApiModelProperty(value = "商品单位", example="台")
	@Length(min=0, max=24, message="商品单位长度必须介于 0 和 24 之间")
	public String unit;		// 商品单位
	
	@ApiModelProperty(value = "是否上架：0:下架，1:上架", example="0")
	public Integer onSaleFlag;		// 是否上架：0:下架，1:上架
	
	@Length(min=0, max=64, message="基础服务长度必须介于 0 和 64 之间")
	@ApiModelProperty(value = "基础服务,1:7天无理由退换货", example="1")
	public String service;		// 基础服务,1:7天无理由退换货
	
	@ApiModelProperty(value = "商品短简介", example="商品短简商品短简商品短简商品短简")
	@Length(min=0, max=128, message="商品短简单位长度必须介于 0 和 128 之间")
	public String shortDescription;		// 商品短简
	
	@ApiModelProperty(value = "商品介绍", example="商品介绍商品介绍商品介绍")
	@Length(min=0, max=100000, message="商品单位长度必须介于 0 和 100000 之间")
	public String description;		// 商品介绍
	
	@ApiModelProperty(value = "发票类型，1：普通发票，2：增值税发票。示例：1,2 (以逗号分割)", example="1,2")
	@Length(min=0, max=8, message="发票类型长度必须介于 0 和 8 之间")
	private String invoiceType; // 发票类型，1：普通发票，2：增值税发票。示例：1,2 (以逗号分割)


}
