/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 商品点赞Entity
 * @author liuhangjun
 * @version 2018-07-27
 */
@Data
public class GoodsLike extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long userId;		// 用户ID
	private Long goodsId;		// 商品id


	public GoodsLike() {
		super();
	}


}