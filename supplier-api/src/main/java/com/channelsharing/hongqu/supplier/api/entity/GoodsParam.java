/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.entity;


import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 商品参数介绍Entity
 * @author liuhangjun
 * @version 2018-07-29
 */
@Data
public class GoodsParam extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long goodsId;		// 商品id
	private String name;		// 参数名
	private String value;		// 参数值
	private Integer sort;		// 排序


	public GoodsParam() {
		super();
	}


}