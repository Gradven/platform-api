/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.goods;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.channelsharing.common.entity.Paging;
import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.common.enums.DelFlagEnum;
import com.channelsharing.common.exception.BadRequestException;
import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.GoodsCategory;
import com.channelsharing.hongqu.portal.api.entity.GoodsInfo;
import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.hongqu.portal.api.service.GoodsCategoryService;
import com.channelsharing.hongqu.portal.api.service.GoodsInfoService;
import com.channelsharing.pub.enums.ApproveStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 商品分类Controller
 * 
 * @author liuhangjun
 * @version 2018-06-27
 */
@Api(tags = "商品分类接口")
@Validated
@RestController
@RequestMapping("/v1/goodsCategory")
public class GoodsCategoryController extends BaseController {

	@Resource
	private GoodsCategoryService goodsCategoryService;

	@Resource
	private GoodsInfoService goodsInfoService;

	@ApiOperation(value = "获取所有商品分类")
	@GetMapping
	public GoodsCategory getAllCategories() {
		return goodsCategoryService.getAllCategories();
	}

	@ApiOperation(value = "查询分类下商品列表")
	@GetMapping("/{categoryId}/goods")
	public Paging<GoodsInfo> searchGoods(@PathVariable Long categoryId,
			@RequestParam(required = false, defaultValue = "id") String orderBy,
			@RequestParam(required = false, defaultValue = "desc") String direction,
			@RequestParam(required = false) BigDecimal minRetailPrice,
			@RequestParam(required = false) BigDecimal maxRetailPrice,
			@RequestParam(required = false, defaultValue = "0") Integer offset,
			@RequestParam(required = false, defaultValue = "10") Integer limit) {
		if (!StringUtils.equalsAny(orderBy, "id", "retail_price", "profit")) {
			throw new BadRequestException("orderBy参数只能是retail_price或者profit");
		}

		if (!StringUtils.equalsAny(direction, "asc", "desc")) {
			throw new BadRequestException("direction参数只能是asc或者desc");
		}

		UserInfo currentUser = super.currentUserWithoutException();

		GoodsInfo queryEntity = new GoodsInfo();
		queryEntity.setCategoryId(categoryId);
		queryEntity.setOnSaleFlag(BooleanEnum.yes.getCode());
		queryEntity.setApproveStatus(ApproveStatus.approved.getCode());
		queryEntity.setOrderBy(orderBy);
		queryEntity.setDirection(direction);
		queryEntity.setMinRetailPrice(minRetailPrice);
		queryEntity.setMaxRetailPrice(maxRetailPrice);
		queryEntity.setOffset(offset);
		queryEntity.setLimit(limit);
		queryEntity.setDelFlag(DelFlagEnum.notDelete.getCode());

		return goodsInfoService.searchGoods(queryEntity, (currentUser != null ? currentUser.getShopId() : null));
	}
}