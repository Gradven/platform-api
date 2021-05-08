/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.dao.GoodsCategoryDao;
import com.channelsharing.hongqu.portal.api.entity.GoodsCategory;
import com.channelsharing.hongqu.portal.api.service.GoodsCategoryService;

/**
 * 商品分类Service
 * 
 * @author liuhangjun
 * @version 2018-06-27
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class GoodsCategoryServiceImpl extends CrudServiceImpl<GoodsCategoryDao, GoodsCategory>
		implements GoodsCategoryService {

	public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;

	@Override
	@Cacheable(value = PORTAL_CACHE_PREFIX
			+ "goodsCategory", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsCategory:' + #id", unless = "#result == null")
	public GoodsCategory findOne(Long id) {
		GoodsCategory entity = new GoodsCategory();
		entity.setId(id);

		return super.findOne(entity);
	}

	@Cacheable(value = PORTAL_CACHE_PREFIX
			+ "allGoodsCategories", key = "#root.target.PORTAL_CACHE_PREFIX + 'allGoodsCategories'")
	@Override
	public GoodsCategory getAllCategories() {
		GoodsCategory queryEntity = new GoodsCategory();
		queryEntity.setOffset(0);
		queryEntity.setLimit(Integer.MAX_VALUE);

		Long rootCategoryId = 1L;

		List<GoodsCategory> allCategories = dao.findList(queryEntity);

		Map<Long, List<GoodsCategory>> categoryMap = allCategories.stream()
				.filter(c -> c.getId().longValue() != rootCategoryId)
				.collect(Collectors.groupingBy(GoodsCategory::getParentId));

		GoodsCategory rootCategory = allCategories.get(0);
		this.fillChildCategories(rootCategory, categoryMap);

		return rootCategory;
	}

	private void fillChildCategories(GoodsCategory parentCategory, Map<Long, List<GoodsCategory>> categoryMap) {
		List<GoodsCategory> childCategories = categoryMap.get(parentCategory.getId());
		if (CollectionUtils.isNotEmpty(childCategories)) {
			CollectionUtils.addAll(parentCategory.getChildCategories(), childCategories);
			for (GoodsCategory goodsCategory : childCategories) {
				this.fillChildCategories(goodsCategory, categoryMap);
			}
		}
	}
}
