/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.shop;

import javax.validation.constraints.NotNull;

import com.channelsharing.hongqu.portal.api.validations.Sensitive;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 店铺代理商品信息Entity
 * @author liuhangjun
 * @version 2018-06-12
 */
@Data
public class ShopGoodsAddRequestEntity {
	

	@ApiModelProperty(value = "商品id", example = "1")
	@NotNull(message="商品id不能为空")
	public Long goodsId;		// 商品id

	@ApiModelProperty(value = "状态，1：添加到代理，0:移除代理", example = "1")
	public Integer status;		// 状态，1：添加到代理，2:移除代理
	
	@Sensitive(message = "推荐语包含了敏感词")
	@ApiModelProperty(value = "推荐语", example = "有一种草叫确认过眼神就够了！别忍了，动手吧！-- from swagger")
	public String recommend;		// 推荐语



}
