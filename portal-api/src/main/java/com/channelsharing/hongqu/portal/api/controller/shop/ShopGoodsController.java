/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.shop;

import javax.annotation.Resource;

import com.channelsharing.common.enums.BooleanEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.service.ShopGoodsService;
import com.channelsharing.hongqu.portal.api.entity.ShopGoods;
import com.channelsharing.common.entity.Paging;


/**
 * 店铺代理商品信息Controller
 * @author liuhangjun
 * @version 2018-06-12
 */
@Api(tags = "店铺代理商品信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/shopGoods")
public class ShopGoodsController extends BaseController {

	@Resource
	private ShopGoodsService shopGoodsService;

    @ApiOperation(value = "获取单条代理商品信息")
	//@GetMapping("/{id}")
	public ShopGoods findOne(@PathVariable Long id){
		
	    ShopGoods shopGoods = shopGoodsService.findOne(id);
	    if (shopGoods== null)
			shopGoods =  new ShopGoods();

		return shopGoods;
	}

	@ApiOperation(value = "查看已经代理的商品")
	@GetMapping
	public Paging<ShopGoods> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit,
			@ApiParam(value = "店铺id", example = "1")
			@RequestParam Long shopId,
			@ApiParam(value = "排序规则，1：最新，2：价格，3：分类", example = "1")
			@RequestParam Integer goodsOrderType,
			@ApiParam(value = "升序或降序(只对价格生效)，1：升序，2：降序", example = "1")
			@RequestParam Integer ascDesc){

		ShopGoods entity = new ShopGoods();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setGoodsOrderType(goodsOrderType);
		entity.setAscDesc(ascDesc);
		entity.setShopId(shopId);
		entity.setStatus(BooleanEnum.yes.getCode());
		
		return shopGoodsService.findPaging(entity, super.currentUserId());

	}
	
	@ApiOperation(value = "根据分类id查看已经代理的商品")
	@GetMapping("/{categoryId}")
	public Paging<ShopGoods> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit,
	        @RequestParam Long shopId,
	        @PathVariable Long categoryId){
		
		ShopGoods entity = new ShopGoods();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setGoodsCategoryId(categoryId);
		entity.setStatus(BooleanEnum.yes.getCode());
		entity.setShopId(shopId);
		
		return shopGoodsService.findPaging(entity);
		
	}
	

	@ApiOperation(value = "把商品加入代理")
	@PostMapping
	public void add(@RequestBody @Validated ShopGoodsAddRequestEntity shopGoodsAddRequestEntity){
		ShopGoods entity = new ShopGoods();
		BeanUtils.copyProperties(shopGoodsAddRequestEntity, entity);
		entity.setShopId(super.currentUser().getShopId());

		shopGoodsService.add(entity);
	}

	@ApiOperation(value = "修改商品代理状态和推荐语")
	@PutMapping
	public void modify(@RequestBody @Validated ShopGoodsModifyRequestEntity shopGoodsModifyRequestEntity){
        ShopGoods entity = new ShopGoods();
		BeanUtils.copyProperties(shopGoodsModifyRequestEntity, entity);
		entity.setShopId(super.currentUser().getShopId());
		
		shopGoodsService.modify(entity);
	}
	

}
