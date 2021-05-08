/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.goods;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.service.GoodsSpecificationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.GoodsSpecification;

import java.util.List;
import java.util.Map;


/**
 * 商品规格值Controller
 * @author liuhangjun
 * @version 2018-06-20
 */
@Api(tags = "商品规格值操作接口")
@Validated
@RestController
@RequestMapping("/v1/goodsSpecification")
public class GoodsSpecificationController extends BaseController {

	@Resource
	private GoodsSpecificationService goodsSpecificationService;
	
	
	@ApiOperation(value = "获取商品规格值列表")
	@GetMapping
	public Map<String, List<GoodsSpecification>> combineGoodsSpecification(@RequestParam Long goodsId){
		
		return goodsSpecificationService.combineGoodsSpecification(goodsId);
	}




}
