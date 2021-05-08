/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.supplier;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.entity.OrderGoods;
import com.channelsharing.hongqu.portal.api.service.OrderGoodsService;
import com.channelsharing.pub.enums.SupplierServiceStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.channelsharing.hongqu.portal.api.service.SupplierServiceService;
import com.channelsharing.hongqu.portal.api.entity.SupplierService;
import com.channelsharing.common.entity.Paging;

import java.util.ArrayList;
import java.util.Date;
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
	    if (supplierService== null)
			supplierService =  new SupplierService();

		return supplierService;
	}

	@ApiOperation(value = "获取供应商售后服务列表")
	@GetMapping
	public Paging<SupplierService> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit){

		SupplierService entity = new SupplierService();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setUserId(super.currentUserId());
		
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

	@ApiOperation(value = "提交一条供应商售后服务")
	@PostMapping
	public void add(@RequestBody @Validated SupplierServiceAddRequestEntity supplierServiceAddRequestEntity){
		SupplierService entity = new SupplierService();
		BeanUtils.copyProperties(supplierServiceAddRequestEntity, entity);
		entity.setUserId(super.currentUserId());
		entity.setStatus(SupplierServiceStatus.apply.getCode());

		supplierServiceService.add(entity);
	}

	@ApiOperation(value = "取消申请供应商售后服务")
	@PutMapping
	public void cancel(@RequestBody @Validated SupplierServiceModifyRequestEntity supplierServiceModifyRequestEntity){
        SupplierService entity = new SupplierService();
		BeanUtils.copyProperties(supplierServiceModifyRequestEntity, entity);
		entity.setUserId(super.currentUserId());
		entity.setStatus(SupplierServiceStatus.cancel.getCode());
		entity.setCancelTime(new Date());

		supplierServiceService.modify(entity);
	}

	@ApiOperation(value = "删除一条供应商售后服务")
	//@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id){
		
		SupplierService supplierService = supplierServiceService.findOne(id);
		
		if (supplierService != null){
			SupplierService entity = new SupplierService();
			entity.setId(id);
			entity.setUserId(super.currentUserId());
			entity.setOrderGoodsId(supplierService.getOrderGoodsId());
			supplierServiceService.delete(entity);
		}
	 
	}

}
