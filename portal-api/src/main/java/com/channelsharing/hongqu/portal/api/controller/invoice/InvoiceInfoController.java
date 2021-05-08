/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.invoice;

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
import com.channelsharing.hongqu.portal.api.service.InvoiceInfoService;
import com.channelsharing.hongqu.portal.api.entity.InvoiceInfo;
import com.channelsharing.common.entity.Paging;


/**
 * 发票信息Controller
 * @author liuhangjun
 * @version 2018-07-29
 */
@Api(tags = "发票信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/invoiceInfo")
public class InvoiceInfoController extends BaseController {

	@Resource
	private InvoiceInfoService invoiceInfoService;

    @ApiOperation(value = "获取单条发票信息")
	@GetMapping("/{id}")
	public InvoiceInfo findOne(@PathVariable Long id){

	    InvoiceInfo invoiceInfo = invoiceInfoService.findOne(id, super.currentUserId());
	    if (invoiceInfo== null)
			invoiceInfo =  new InvoiceInfo();

		return invoiceInfo;
	}

	@ApiOperation(value = "获取发票信息列表")
	@GetMapping
	public Paging<InvoiceInfo> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit,
			@ApiParam(value = "发票类型,1:普通发票，2：增值税专用发票", example = "2")
			@RequestParam(required = false) Integer type){

		InvoiceInfo entity = new InvoiceInfo();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setType(type);
		entity.setUserId(super.currentUserId());

		return invoiceInfoService.findPaging(entity);

	}

	@ApiOperation(value = "提交一条发票信息")
	@PostMapping
	public void add(@RequestBody @Validated InvoiceInfoAddRequestEntity invoiceInfoAddRequestEntity){
		InvoiceInfo entity = new InvoiceInfo();
		BeanUtils.copyProperties(invoiceInfoAddRequestEntity, entity);
		entity.setUserId(super.currentUserId());

		invoiceInfoService.add(entity);
	}

	@ApiOperation(value = "修改一条发票信息")
	@PutMapping
	public void modify(@RequestBody @Validated InvoiceInfoModifyRequestEntity invoiceInfoModifyRequestEntity){
        InvoiceInfo entity = new InvoiceInfo();
		BeanUtils.copyProperties(invoiceInfoModifyRequestEntity, entity);
		entity.setUserId(super.currentUserId());

		invoiceInfoService.modify(entity);
	}

	@ApiOperation(value = "删除一条发票信息")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id){
	    InvoiceInfo entity = new InvoiceInfo();
	    entity.setId(id);
		invoiceInfoService.delete(entity);
	}

}
