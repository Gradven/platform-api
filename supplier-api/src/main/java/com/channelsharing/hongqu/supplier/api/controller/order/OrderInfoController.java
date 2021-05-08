/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.order;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.hongqu.supplier.api.controller.BaseController;
import com.channelsharing.hongqu.supplier.api.service.OrderInfoService;
import com.channelsharing.hongqu.supplier.api.entity.OrderInfo;
import com.channelsharing.common.entity.Paging;


/**
 * 订单信息Controller
 * @author liuhangjun
 * @version 2018-07-01
 */
@Api(tags = "订单信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/orderInfo")
public class OrderInfoController extends BaseController {

	@Resource
	private OrderInfoService orderInfoService;

    @ApiOperation(value = "获取单条订单信息")
	@GetMapping("/{sn}")
	public OrderInfo findOneBySn(@PathVariable String sn){

	    OrderInfo orderInfo = orderInfoService.findOneBySn(sn);
	    if (orderInfo== null)
			orderInfo =  new OrderInfo();

		return orderInfo;
	}

	@ApiOperation(value = "获取订单信息列表")
	//@GetMapping
	public Paging<OrderInfo> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit){

		OrderInfo entity = new OrderInfo();
		entity.setOffset(offset);
		entity.setLimit(limit);

		return orderInfoService.findPaging(entity);

	}
}
