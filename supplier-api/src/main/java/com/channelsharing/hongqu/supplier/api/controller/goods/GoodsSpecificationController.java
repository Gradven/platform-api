/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.goods;

import java.util.List;
import java.util.Map;

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

import com.channelsharing.hongqu.supplier.api.controller.BaseController;
import com.channelsharing.hongqu.supplier.api.entity.GoodsSpecification;
import com.channelsharing.hongqu.supplier.api.service.GoodsSpecificationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 商品规格值Controller
 * @author liuhangjun
 * @version 2018-06-07
 */
@Api(tags = "商品规格值操作接口")
@Validated
@RestController
@RequestMapping("/v1/goodsSpecification")
public class GoodsSpecificationController extends BaseController {

	@Resource
	private GoodsSpecificationService goodsSpecificationService;

    @ApiOperation(value = "获取单条商品规格值")
	@GetMapping("/{id}")
	public GoodsSpecification findOne(@PathVariable Long id){
		
	    GoodsSpecification goodsSpecification = goodsSpecificationService.findOne(id);
	    if (goodsSpecification== null)
			goodsSpecification =  new GoodsSpecification();

		return goodsSpecification;
	}

	/**@ApiOperation(value = "获取商品规格值列表")
	@GetMapping
	public Paging<GoodsSpecification> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit,
			@RequestParam Long goodsId){

		GoodsSpecification entity = new GoodsSpecification();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setGoodsId(goodsId);

		return goodsSpecificationService.findPaging(entity);

	}*/
	
    @ApiOperation(value = "获取商品规格值列表")
    @GetMapping
	public Map<String, List<GoodsSpecification>> combineGoodsSpecification(@RequestParam Long goodsId){
	
    	return goodsSpecificationService.combineGoodsSpecification(goodsId);
	}
	
	
	
	@ApiOperation(value = "提交一条商品规格值")
	@PostMapping
	public void add(@RequestBody @Validated GoodsSpecificationAddRequestEntity goodsSpecificationAddRequestEntity){
		GoodsSpecification entity = new GoodsSpecification();
		BeanUtils.copyProperties(goodsSpecificationAddRequestEntity, entity);

		goodsSpecificationService.add(entity);
	}

	@ApiOperation(value = "修改一条商品规格值")
	@PatchMapping
	public void modify(@RequestBody @Validated GoodsSpecificationModifyRequestEntity goodsSpecificationModifyRequestEntity){
        GoodsSpecification entity = new GoodsSpecification();
		BeanUtils.copyProperties(goodsSpecificationModifyRequestEntity, entity);

		goodsSpecificationService.modify(entity);
	}

	@ApiOperation(value = "删除一条商品规格值")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id){
	    GoodsSpecification entity = new GoodsSpecification();
	    entity.setId(id);
		goodsSpecificationService.delete(entity);
	}

}
