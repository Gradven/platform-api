/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.goods;

import javax.annotation.Resource;

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
import com.channelsharing.hongqu.portal.api.service.GoodsParamService;
import com.channelsharing.hongqu.portal.api.entity.GoodsParam;
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
	

	@ApiOperation(value = "获取商品参数介绍列表")
	@GetMapping
	public List<GoodsParam> findList(@RequestParam Long goodsId){


		return goodsParamService.findList(goodsId);

	}
	

}
