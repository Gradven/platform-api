/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.order;

import javax.annotation.Resource;

import com.channelsharing.common.lock.method.CacheLock;
import com.channelsharing.common.lock.method.CacheParam;
import com.channelsharing.pub.enums.CancelType;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.service.OrderShopServeService;
import com.channelsharing.hongqu.portal.api.entity.OrderShopServe;
import com.channelsharing.common.entity.Paging;

import java.util.Map;


/**
 * 店铺技术服务费订单Controller
 * @author liuhangjun
 * @version 2018-06-18
 */
@Api(tags = "店铺技术服务费订单操作接口")
@Validated
@RestController
@RequestMapping("/v1/orderShopService")
public class OrderShopServeController extends BaseController {

	@Resource
	private OrderShopServeService orderShopServiceService;

    @ApiOperation(value = "获取单条店铺技术服务费订单")
	@GetMapping("/{id}")
	public OrderShopServe findOne(@PathVariable Long id){

	    OrderShopServe entity = new OrderShopServe();
	    entity.setId(id);
	    entity.setUserId(super.currentUserId());
	    OrderShopServe orderShopService = orderShopServiceService.findOne(entity);
	    if (orderShopService== null)
			orderShopService =  new OrderShopServe();

		return orderShopService;
	}

	@ApiOperation(value = "获取店铺技术服务费订单列表")
	@GetMapping
	public Paging<OrderShopServe> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit){

		OrderShopServe entity = new OrderShopServe();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setUserId(super.currentUserId());

		return orderShopServiceService.findPagingWithCalculate(entity);

	}
	
	@CacheLock(prefix = "portal:OrderShopServeController:add:")
	@ApiOperation(value = "提交一条店铺技术服务费订单")
	@PostMapping
	public Map<String, String> add(@CacheParam(name = "orderShopServiceAddRequestEntity")
								   @RequestBody @Validated OrderShopServeAddRequestEntity orderShopServiceAddRequestEntity){
		OrderShopServe entity = new OrderShopServe();
		BeanUtils.copyProperties(orderShopServiceAddRequestEntity, entity);
		entity.setUserId(super.currentUserId());

		return orderShopServiceService.addOrder(entity, super.currentUser());
	}

	@ApiOperation(value = "用户主动取消店铺技术服务费订单")
	@PostMapping("/cancel/{sn}")
	public void cancel(@PathVariable String sn,
					   @ApiParam(value = "备注", example = "我就是要取消") String remark){
		
		orderShopServiceService.cancelOrder(sn, CancelType.userCancel, super.currentUserId(), remark);
	}

}
