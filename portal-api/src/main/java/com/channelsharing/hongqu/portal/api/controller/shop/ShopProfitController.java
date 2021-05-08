/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.shop;

import javax.annotation.Resource;

import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.ShopProfit;
import com.channelsharing.hongqu.portal.api.service.ShopProfitService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.common.entity.Paging;

import java.math.BigDecimal;


/**
 * 店铺收益Controller
 * @author liuhangjun
 * @version 2018-07-17
 */
@Api(tags = "店铺收益操作接口")
@Validated
@RestController
@RequestMapping("/v1/shopProfit")
public class ShopProfitController extends BaseController {

	@Resource
	private ShopProfitService shopProfitService;

    @ApiOperation(value = "获取单条店铺收益")
	//@GetMapping("/{id}")
	public ShopProfit findOne(@PathVariable Long id){

	    ShopProfit shopProfit = shopProfitService.findOne(id);
	    if (shopProfit== null)
			shopProfit =  new ShopProfit();

		return shopProfit;
	}

	@ApiOperation(value = "获取店铺收益列表")
	@GetMapping
	public Paging<ShopProfit> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit){

		ShopProfit entity = new ShopProfit();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setShopId(super.currentUser().getShopId());
		entity.setAvailableFlag(BooleanEnum.yes.getCode());

		return shopProfitService.findPaging(entity);

	}
	
	/**
	 * 待入账收益
	 * @return
	 */
	@ApiOperation(value = "店主待入账收益")
	@GetMapping("/unAvailableProfit")
	public BigDecimal unAvailableProfit(){
		
		BigDecimal unAvailableProfit = shopProfitService.unAvailableProfit(super.currentUser().getShopId());
		
		return unAvailableProfit;
	}
	
	/**
	 * 待入账收益
	 * @return
	 */
	@ApiOperation(value = "店主所有收益")
	@GetMapping("/allProfit")
	public BigDecimal allProfit(){
		
		BigDecimal unAvailableProfit = shopProfitService.allProfit(super.currentUser().getShopId());
		
		return unAvailableProfit;
	}
	
	

}
