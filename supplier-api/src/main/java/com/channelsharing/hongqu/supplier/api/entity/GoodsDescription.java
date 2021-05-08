/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.entity;


import com.channelsharing.common.entity.BaseEntity;
import lombok.Data;

/**
 * 商品介绍内容Entity
 * @author liuhangjun
 * @version 2018-06-07
 */
@Data
public class GoodsDescription extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String content;		// 商品介绍内容详情


	public GoodsDescription() {
		super();
	}


}
