/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.order;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.hongqu.supplier.api.controller.BaseController;
import com.channelsharing.hongqu.supplier.api.service.OrderInvoiceService;
import com.channelsharing.hongqu.supplier.api.entity.OrderInvoice;
import com.channelsharing.common.entity.Paging;


/**
 * 订单发票Controller
 * @author liuhangjun
 * @version 2018-07-29
 */
@Api(tags = "订单发票操作接口")
@Validated
@RestController
@RequestMapping("/v1/orderInvoice")
public class OrderInvoiceController extends BaseController {

	@Resource
	private OrderInvoiceService orderInvoiceService;

    @ApiOperation(value = "获取单条订单发票")
	@GetMapping("/{orderSn}")
	public OrderInvoice findOne(@PathVariable String orderSn){
    	
	    OrderInvoice orderInvoice = orderInvoiceService.findOne(orderSn);
	    if (orderInvoice== null)
			orderInvoice =  new OrderInvoice();

		return orderInvoice;
	}

	@ApiOperation(value = "获取订单发票列表")
	//@GetMapping
	public Paging<OrderInvoice> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit){

		OrderInvoice entity = new OrderInvoice();
		entity.setOffset(offset);
		entity.setLimit(limit);

		return orderInvoiceService.findPaging(entity);

	}

	@ApiOperation(value = "提交一条订单发票")
	//@PostMapping
	public void add(@RequestBody @Validated OrderInvoiceAddRequestEntity orderInvoiceAddRequestEntity){
		OrderInvoice entity = new OrderInvoice();
		BeanUtils.copyProperties(orderInvoiceAddRequestEntity, entity);

		orderInvoiceService.add(entity);
	}

	@ApiOperation(value = "修改一条订单发票")
	@PatchMapping
	public void modify(@RequestBody @Validated OrderInvoiceModifyRequestEntity orderInvoiceModifyRequestEntity){
        OrderInvoice entity = new OrderInvoice();
		BeanUtils.copyProperties(orderInvoiceModifyRequestEntity, entity);

		orderInvoiceService.modify(entity);
	}

	@ApiOperation(value = "删除一条订单发票")
	//@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id){
	    OrderInvoice entity = new OrderInvoice();
	    entity.setId(id);
		orderInvoiceService.delete(entity);
	}

}
