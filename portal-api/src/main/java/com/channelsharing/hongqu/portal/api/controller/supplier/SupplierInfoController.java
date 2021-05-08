/**
 * Copyright &copy; 2016-2022 供应商信息 All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.supplier;

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
import com.channelsharing.hongqu.portal.api.service.SupplierInfoService;
import com.channelsharing.hongqu.portal.api.entity.SupplierInfo;
import com.channelsharing.common.entity.Paging;


/**
 * 供应商信息Controller
 * @author 供应商信息
 * @version 2018-08-08
 */
@Api(tags = "供应商信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/supplierInfo")
public class SupplierInfoController extends BaseController {

	@Resource
	private SupplierInfoService supplierInfoService;

    @ApiOperation(value = "获取单条供应商信息")
	@GetMapping("/{id}")
	public SupplierInfo findOne(@PathVariable Long id){

	    SupplierInfo supplierInfo = supplierInfoService.findOne(id);
	    if (supplierInfo== null)
			supplierInfo =  new SupplierInfo();

		return supplierInfo;
	}
	

}
