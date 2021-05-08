/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 商品规格值Entity
 * @author liuhangjun
 * @version 2018-06-20
 */
@Data
public class GoodsSpecification extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long goodsId;		// 商品id
	private Long specificationId;		// 规格id
	private String value;		// 规格值
	private String picUrl;		// 规格图


	public GoodsSpecification() {
		super();
	}


}
