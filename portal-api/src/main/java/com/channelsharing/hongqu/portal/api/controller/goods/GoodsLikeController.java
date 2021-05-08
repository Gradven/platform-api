/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.goods;

import javax.annotation.Resource;

import com.channelsharing.common.exception.BadRequestException;
import com.channelsharing.common.exception.DuplicateKeyException;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.service.GoodsLikeService;
import com.channelsharing.hongqu.portal.api.entity.GoodsLike;
import com.channelsharing.common.entity.Paging;


/**
 * 商品点赞Controller
 * @author liuhangjun
 * @version 2018-07-27
 */
@Api(tags = "商品点赞操作接口")
@Validated
@RestController
@RequestMapping("/v1/goodsLike")
public class GoodsLikeController extends BaseController {

	@Resource
	private GoodsLikeService goodsLikeService;

    @ApiOperation(value = "获取单条商品点赞")
	//@GetMapping
	public GoodsLike findOne(@RequestParam Long goodsId){
		GoodsLike entity = new GoodsLike();
		entity.setGoodsId(goodsId);
		entity.setUserId(super.currentUserId());
	    GoodsLike goodsLike = goodsLikeService.findOne(entity);
	    if (goodsLike== null)
			goodsLike =  new GoodsLike();

		return goodsLike;
	}

	@ApiOperation(value = "获取商品点赞列表")
	//@GetMapping
	public Paging<GoodsLike> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit){

		GoodsLike entity = new GoodsLike();
		entity.setOffset(offset);
		entity.setLimit(limit);

		return goodsLikeService.findPaging(entity);

	}

	@ApiOperation(value = "提交一条商品点赞")
	@PostMapping
	public void add(@RequestBody @Validated GoodsLikeAddRequestEntity goodsLikeAddRequestEntity){
		GoodsLike entity = new GoodsLike();
		BeanUtils.copyProperties(goodsLikeAddRequestEntity, entity);
		entity.setUserId(super.currentUserId());
		
		GoodsLike result = goodsLikeService.findOne(entity.getGoodsId(), super.currentUserId());
		if (result != null && result.getGoodsId() != null){
			throw new DuplicateKeyException("您已点赞");
		}

		goodsLikeService.add(entity);
	}
	

	@ApiOperation(value = "删除一条商品点赞")
	//@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id){
	    GoodsLike entity = new GoodsLike();
	    entity.setId(id);
		goodsLikeService.delete(entity);
	}

}
