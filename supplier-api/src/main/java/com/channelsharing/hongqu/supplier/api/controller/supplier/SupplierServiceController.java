/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.supplier;

import javax.annotation.Resource;

import com.channelsharing.hongqu.supplier.api.entity.OrderGoods;
import com.channelsharing.hongqu.supplier.api.service.OrderGoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.channelsharing.hongqu.supplier.api.service.SupplierServiceService;
import com.channelsharing.hongqu.supplier.api.entity.SupplierService;
import com.channelsharing.common.entity.Paging;

import java.util.ArrayList;
import java.util.List;


/**
 * 供应商售后服务Controller
 * @author liuhangjun
 * @version 2018-08-08
 */
@Api(tags = "供应商售后服务操作接口")
@Validated
@RestController
@RequestMapping("/v1/supplierService")
public class SupplierServiceController extends BaseController {

	@Resource
	private SupplierServiceService supplierServiceService;
	
	@Autowired
	private OrderGoodsService orderGoodsService;

    @ApiOperation(value = "获取单条供应商售后服务")
	@GetMapping("/{id}")
	public SupplierService findOne(@PathVariable Long id){

	    SupplierService supplierService = supplierServiceService.findOne(id);
	    if (supplierService == null)
			return new SupplierService();
		
	    OrderGoods orderGoods = orderGoodsService.findOne(supplierService.getOrderGoodsId());
		if (orderGoods != null){
			supplierService.setOrderGoods(orderGoods);
		}

		return supplierService;
	}

	@ApiOperation(value = "获取供应商售后服务列表")
	@GetMapping
	public Paging<SupplierService> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit,
			@RequestParam(required = false) Integer status){

		SupplierService entity = new SupplierService();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setSupplierId(currentSupplierUser().getSupplierId());
		entity.setStatus(status);
		
		Paging<SupplierService> supplierServicePaging = supplierServiceService.findPaging(entity);
		
		List<SupplierService> supplierServiceList = new ArrayList<>();
		
		for (SupplierService supplierService : supplierServicePaging.getRows()){
			OrderGoods orderGoods = orderGoodsService.findOne(supplierService.getOrderGoodsId());
			if (orderGoods != null){
				supplierService.setOrderGoods(orderGoods);
			}
			supplierServiceList.add(supplierService);
		}
		
		supplierServicePaging.setRows(supplierServiceList);
		
		return supplierServicePaging;

	}
	

	@ApiOperation(value = "修改一条供应商售后服务")
	@PatchMapping
	public void modify(@RequestBody @Validated SupplierServiceModifyRequestEntity supplierServiceModifyRequestEntity){
        SupplierService entity = new SupplierService();
		BeanUtils.copyProperties(supplierServiceModifyRequestEntity, entity);

		supplierServiceService.modify(entity);
	}

	@ApiOperation(value = "删除一条供应商售后服务")
	//@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id){
	    SupplierService entity = new SupplierService();
	    entity.setId(id);
		supplierServiceService.delete(entity);
	}

}
