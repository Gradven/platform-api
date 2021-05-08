/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import java.util.Set;

import javax.validation.constraints.NotNull;

/**
 * 店铺的商品分类Service
 * 
 * @author liuhangjun
 * @version 2018-06-27
 */
public interface ShopGoodsCategoryService {
	Set<Long> findCategoryIdSet(@NotNull Long shopId);
}
