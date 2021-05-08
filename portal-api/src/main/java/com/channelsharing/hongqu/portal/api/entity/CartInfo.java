/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 购物车Entity
 * @author liuhangjun
 * @version 2018-06-22
 */
@Data
public class CartInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long userId;		// 用户id
	private Long shopId;		// 店铺id
	private Long goodsId;		// 商品id
	private Long productId;		// 产品id
	private Integer goodsNumber;		// 商品数量

	private ShopInfo shopInfo;
	private GoodsInfo goodsInfo;
	private ProductInfo productInfo;

	public CartInfo() {
		super();
	}


}
