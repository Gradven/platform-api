/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.cart;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.CartInfo;
import com.channelsharing.hongqu.portal.api.service.CartInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.common.entity.Paging;


/**
 * 购物车Controller
 * @author liuhangjun
 * @version 2018-06-22
 */
@Api(tags = "购物车操作接口")
@Validated
@RestController
@RequestMapping("/v1/cartInfo")
public class CartInfoController extends BaseController {

	@Resource
	private CartInfoService cartInfoService;

    @ApiOperation(value = "获取单条购物车")
	//@GetMapping("/{id}")
	public CartInfo findOne(@PathVariable Long id){
		
	    CartInfo cartInfo = cartInfoService.findOne(id);
	    if (cartInfo== null)
			cartInfo =  new CartInfo();

		return cartInfo;
	}

	@ApiOperation(value = "获取购物车列表")
	@GetMapping
	public Paging<CartInfo> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit){

		CartInfo entity = new CartInfo();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setUserId(super.currentUserId());

		return cartInfoService.findPaging(entity);

	}

	@ApiOperation(value = "提交一条购物车")
	@PostMapping
	public void add(@RequestBody @Validated CartInfoAddRequestEntity cartInfoAddRequestEntity){
		CartInfo entity = new CartInfo();
		BeanUtils.copyProperties(cartInfoAddRequestEntity, entity);
		entity.setUserId(super.currentUserId());

		cartInfoService.add(entity);
	}

	@ApiOperation(value = "修改一条购物车")
	@PutMapping
	public void modify(@RequestBody @Validated CartInfoModifyRequestEntity cartInfoModifyRequestEntity){
        CartInfo entity = new CartInfo();
		BeanUtils.copyProperties(cartInfoModifyRequestEntity, entity);
		entity.setUserId(super.currentUserId());

		cartInfoService.modify(entity);
	}

	@ApiOperation(value = "删除一条购物车")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id){
	    CartInfo entity = new CartInfo();
	    entity.setId(id);
		entity.setUserId(super.currentUserId());
		cartInfoService.delete(entity);
	}
	
	@ApiOperation(value = "批量删除购物车")
	@DeleteMapping("/batch")
	public void batchDelete(@RequestParam Long[] ids){
		
		cartInfoService.batchDelete(ids, super.currentUserId());
	}

}
