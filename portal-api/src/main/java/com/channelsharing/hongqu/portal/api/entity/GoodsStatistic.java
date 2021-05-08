/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 商品实时数据统计Entity
 * @author liuhangjun
 * @version 2018-07-27
 */
@Data
public class GoodsStatistic extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long goodsId;		// 商品id
	private Integer likeCount;		// 点赞数


	public GoodsStatistic() {
		super();
	}


}
