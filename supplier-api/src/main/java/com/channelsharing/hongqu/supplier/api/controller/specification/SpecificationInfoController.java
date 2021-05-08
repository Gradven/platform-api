/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.specification;

import javax.annotation.Resource;

import com.channelsharing.hongqu.supplier.api.controller.BaseController;
import com.channelsharing.hongqu.supplier.api.entity.SpecificationInfo;
import com.channelsharing.hongqu.supplier.api.service.SpecificationInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.channelsharing.common.entity.Paging;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 规格维度接口Controller
 * @author liuhangjun
 * @version 2018-06-07
 */
@Api(tags = "规格维度操作接口")
@Validated
@RestController
@RequestMapping("/v1/specificationInfo")
public class SpecificationInfoController extends BaseController {

	@Resource
	private SpecificationInfoService specificationInfoService;

    @ApiOperation(value = "获取单条规格维度")
	@GetMapping("/{id}")
	public SpecificationInfo findOne(@PathVariable Long id){

	    SpecificationInfo specificationInfo = specificationInfoService.findOne(id);
	    if (specificationInfo== null)
			specificationInfo =  new SpecificationInfo();

		return specificationInfo;
	}

	@ApiOperation(value = "获取规格维度列表")
	@GetMapping
	public Paging<SpecificationInfo> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit,
	        @RequestParam(required = false) String name){

		SpecificationInfo entity = new SpecificationInfo();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setName(name);

		return specificationInfoService.findPaging(entity);

	}



}
