/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.goods;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.hongqu.supplier.api.controller.BaseController;
import com.channelsharing.hongqu.supplier.api.service.GoodsParamService;
import com.channelsharing.hongqu.supplier.api.entity.GoodsParam;
import com.channelsharing.common.entity.Paging;

import java.util.List;


/**
 * 商品参数介绍Controller
 * @author liuhangjun
 * @version 2018-07-29
 */
@Api(tags = "商品参数介绍操作接口")
@Validated
@RestController
@RequestMapping("/v1/goodsParam")
public class GoodsParamController extends BaseController {

	@Resource
	private GoodsParamService goodsParamService;

    @ApiOperation(value = "获取单条商品参数介绍")
	//@GetMapping("/{id}")
	public GoodsParam findOne(@PathVariable Long id){

	    GoodsParam goodsParam = goodsParamService.findOne(id);
	    if (goodsParam== null)
			goodsParam =  new GoodsParam();

		return goodsParam;
	}

	@ApiOperation(value = "获取商品参数介绍列表")
	@GetMapping
	public List<GoodsParam> findList(
			@RequestParam Long goodsId ){

		return goodsParamService.findList(goodsId);

	}

	@ApiOperation(value = "提交一条商品参数介绍")
	@PostMapping
	public void add(@RequestBody @Validated GoodsParamAddRequestEntity goodsParamAddRequestEntity){
		GoodsParam entity = new GoodsParam();
		BeanUtils.copyProperties(goodsParamAddRequestEntity, entity);

		goodsParamService.add(entity);
	}
	
	@ApiOperation(value = "批量提交商品参数介绍")
	@PostMapping("/addBatch")
	public void addBatch(@RequestBody @Validated List<GoodsParamAddRequestEntity> goodsParamAddRequestEntityList){
		for(GoodsParamAddRequestEntity entity : goodsParamAddRequestEntityList){
		
			this.add(entity);
		}
		
	}

	@ApiOperation(value = "修改一条商品参数介绍")
	@PatchMapping
	public void modify(@RequestBody @Validated GoodsParamModifyRequestEntity goodsParamModifyRequestEntity){
        GoodsParam entity = new GoodsParam();
		BeanUtils.copyProperties(goodsParamModifyRequestEntity, entity);

		goodsParamService.modify(entity);
	}

	@ApiOperation(value = "删除一条商品参数介绍")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id, @RequestParam Long goodsId){
	    GoodsParam entity = new GoodsParam();
	    entity.setId(id);
		entity.setGoodsId(goodsId);
		goodsParamService.delete(entity);
	}

}
