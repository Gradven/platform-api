/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.product;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 产品信息Entity
 * @author liuhangjun
 * @version 2018-06-07
 */
@Data
public class ProductInfoModifyRequestEntity {
	
	@ApiModelProperty(value = "产品id", example = "1")
	@NotNull
	public Long id;
	
	@ApiModelProperty(value = "商品Id", example = "1")
	@NotNull
	public Long goodsId;		// 商品Id
	
	@ApiModelProperty(value = "产品图片", example = "http://www.pic.com")
	@Length(min = 0, max = 255, message = "产品图片长度必须介于 0 和 64 之间")
	public String picUrl; // 产品图片
	
	@ApiModelProperty(value = "库存", example="100")
	public Integer storeNumber;		// 库存
	
	@ApiModelProperty(value = "零售价格", example="90.11")
	public BigDecimal retailPrice;		// 零售价格
	
	@ApiModelProperty(value = "单价", example="70.11")
	public BigDecimal unitPrice;		// 价格
	
	@ApiModelProperty(value = "单价", example="10.11")
	public BigDecimal profit;		// 利润
	
	@ApiModelProperty(value = "删除标记，0：未删除，1：已删除", example="0")
	public Integer delFlag;
	
	@ApiModelProperty(value = "利润百分比", example = "10.15")
	@Range(min = 0, max = 100, message = "利润百分比介于0%和100%之间")
	public BigDecimal percent;

}
