/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.goods;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * 商品规格值Entity
 * @author liuhangjun
 * @version 2018-06-07
 */
@Data
public class GoodsSpecificationAddRequestEntity {


	@NotNull(message="商品id不能为空")
	public Long goodsId;		// 商品id

	@NotNull(message="规格id不能为空")
	public Long specificationId;		// 规格id

	@Length(min=0, max=64, message="规格值长度必须介于 0 和 64 之间")
	public String value;		// 规格值

	@Length(min=0, max=255, message="规格图长度必须介于 0 和 255 之间")
	public String picUrl;		// 规格图



}
