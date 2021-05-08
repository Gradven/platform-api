/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.entity.ShopGoods;
import com.channelsharing.hongqu.portal.api.service.ShopGoodsCategoryService;
import com.channelsharing.hongqu.portal.api.service.ShopGoodsService;

/**
 * 店铺的商品分类Service
 * 
 * @author liuhangjun
 * @version 2018-06-27
 */
@Service
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
public class ShopGoodsCategoryServiceImpl implements ShopGoodsCategoryService {
	@Resource
	private ShopGoodsService shopGoodsService;

	public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;

	/**
	 * 获取店铺内所有商品的分类
	 * 
	 * @param shopId
	 * @return
	 */
	@Cacheable(value = PORTAL_CACHE_PREFIX
			+ "shopGoodsCategories", key = "#root.target.PORTAL_CACHE_PREFIX + 'shopGoodsCategories:shopId:' + #shopId", unless = "#result == null")
	@Override
	public Set<Long> findCategoryIdSet(@NotNull Long shopId) {
		ShopGoods shopGoodsQuery = new ShopGoods();
		shopGoodsQuery.setShopId(shopId);
		shopGoodsQuery.setStatus(BooleanEnum.yes.getCode());
		shopGoodsQuery.setLimit(Integer.MAX_VALUE);

		List<ShopGoods> shopGoodsList = shopGoodsService.findPaging(shopGoodsQuery).getRows();

		return shopGoodsList.stream().map(t -> t.getGoodsCategoryId()).collect(Collectors.toSet());
	}
}
