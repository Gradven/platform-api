/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.shop;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.ShopGoodsCategory;
import com.channelsharing.hongqu.portal.api.service.GoodsCategoryService;
import com.channelsharing.hongqu.portal.api.service.ShopGoodsCategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 店铺的商品分类Controller
 * 
 * @author liuhangjun
 * @version 2018-06-27
 */
@Api(tags = "店铺的商品分类操作接口")
@Validated
@RestController
@RequestMapping("/v1/shopGoodsCategory")
public class ShopGoodsCategoryController extends BaseController {
	@Resource
	private ShopGoodsCategoryService shopGoodsCategoryService;

	@Resource
	private GoodsCategoryService goodsCategoryService;

	@ApiOperation(value = "获取店铺的商品分类列表")
	@GetMapping
	public List<ShopGoodsCategory> findList(@RequestParam Long shopId) {
		Set<Long> shopGoodsCategoryIdSet = shopGoodsCategoryService.findCategoryIdSet(shopId);

		return shopGoodsCategoryIdSet.stream()
				.map(categoryId -> new ShopGoodsCategory(shopId, categoryId, goodsCategoryService.findOne(categoryId)))
				.collect(Collectors.toList());
	}
}
