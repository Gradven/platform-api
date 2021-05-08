/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.goods;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品参数介绍Entity
 * @author liuhangjun
 * @version 2018-07-29
 */
@Data
public class GoodsParamAddRequestEntity {


	@ApiModelProperty(value = "商品id", example = "1")
	@NotNull(message="商品id不能为空")
	public Long goodsId;		// 商品id

	@ApiModelProperty(value = "参数名", example = "长度")
	@Length(min=1, max=16, message="参数名长度必须介于 1 和 16 之间")
	public String name;		// 参数名

	@ApiModelProperty(value = "参数值", example = "12*13*111")
	@Length(min=1, max=128, message="参数值长度必须介于 1 和 128 之间")
	public String value;		// 参数值

	@ApiModelProperty(value = "排序", example = "1")
	public Integer sort;		// 排序



}
