/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.shipping;

import javax.annotation.Resource;

import com.channelsharing.hongqu.supplier.api.entity.ShippingCompany;
import com.channelsharing.hongqu.supplier.api.service.ShippingCompanyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.hongqu.supplier.api.controller.BaseController;
import com.channelsharing.common.entity.Paging;


/**
 * 物流公司信息Controller
 * @author liuhangjun
 * @version 2018-07-03
 */
@Api(tags = "物流公司信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/shippingCompany")
public class ShippingCompanyController extends BaseController {

	@Resource
	private ShippingCompanyService shippingCompanyService;
	

	@ApiOperation(value = "获取物流公司信息列表")
	@GetMapping
	public Paging<ShippingCompany> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit,
			@ApiParam(value = "物流公司", example = "顺丰")
			@RequestParam(required = false) String name){

		ShippingCompany entity = new ShippingCompany();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setName(name);

		return shippingCompanyService.findPaging(entity);

	}
	
}
