/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;

import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 店铺的商品分类Entity
 * 
 * @author liuhangjun
 * @version 2018-06-27
 */
@Data
public class ShopGoodsCategory extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long shopId; // 店铺id
	private Long categoryId; // 分类id

	private GoodsCategory goodsCategory;

	public ShopGoodsCategory() {
		super();
	}

	public ShopGoodsCategory(Long shopId, Long categoryId, GoodsCategory goodsCategory) {
		super();
		this.shopId = shopId;
		this.categoryId = categoryId;
		this.goodsCategory = goodsCategory;
	}
}
