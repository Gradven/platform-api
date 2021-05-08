/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.goods;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.GoodsDescription;
import com.channelsharing.hongqu.portal.api.service.GoodsDescriptionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 商品介绍内容Controller
 * @author liuhangjun
 * @version 2018-06-12
 */
@Api(tags = "商品介绍内容操作接口")
@Validated
@RestController
@RequestMapping("/v1/goodsDescription")
public class GoodsDescriptionController extends BaseController {

	@Resource
	private GoodsDescriptionService goodsDescriptionService;

    @ApiOperation(value = "获取单条商品介绍内容")
	@GetMapping("/{id}")
	public GoodsDescription findOne(@PathVariable Long id){
		
	    GoodsDescription goodsDescription = goodsDescriptionService.findOne(id);
	    if (goodsDescription== null)
			goodsDescription =  new GoodsDescription();

		return goodsDescription;
	}
	

}
