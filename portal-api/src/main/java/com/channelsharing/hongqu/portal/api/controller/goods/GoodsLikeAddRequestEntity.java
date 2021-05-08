/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.goods;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品点赞Entity
 * @author liuhangjun
 * @version 2018-07-27
 */
@Data
public class GoodsLikeAddRequestEntity {
	

	@ApiModelProperty(value = "商品id", example = "1")
	@NotNull(message="商品id不能为空")
	public Long goodsId;		// 商品id



}
