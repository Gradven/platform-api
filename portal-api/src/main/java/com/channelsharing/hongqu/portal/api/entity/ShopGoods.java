/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 店铺代理商品信息Entity
 * @author liuhangjun
 * @version 2018-06-12
 */
@Data
public class ShopGoods extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long shopId;		// 店铺id
	private Long goodsId;		// 商品id
	private Integer status;		// 状态，1：添加到代理，2:移除代理
	private Long goodsCategoryId; // 商品分类id
	private String recommend;   // 推荐语
	
	private Integer goodsOrderType; // 商品列表排序依据
	private Integer ascDesc;  // 升序或降序，1：升序，2：降序
	
	private GoodsInfo goodsInfo;


	public ShopGoods() {
		super();
	}


}
