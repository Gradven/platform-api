/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.order;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.service.OrderGoodsService;
import com.channelsharing.pub.enums.ShippingStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.service.CourierService;
import com.channelsharing.hongqu.portal.api.entity.OrderGoods;
import com.alibaba.fastjson.JSONObject;
import com.channelsharing.common.entity.Paging;
import com.channelsharing.common.exception.DataNotFoundException;

/**
 * 订单的商品信息Controller
 *
 * @author liuhangjun
 * @version 2018-06-20
 */
@Api(tags = "订单的商品信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/orderGoods")
public class OrderGoodsController extends BaseController {

	@Resource
	private OrderGoodsService orderGoodsService;

	@Resource
	private CourierService courierService;

	@ApiOperation(value = "查询订单商品物流信息")
	@GetMapping("/{id}/couriers")
	public JSONObject queryCourierInfo(@PathVariable Long id) {
		OrderGoods queryEntity = new OrderGoods();
		queryEntity.setId(id);
		queryEntity.setUserId(super.currentUserId());

		OrderGoods orderGoods = orderGoodsService.findOne(queryEntity);
		if (orderGoods == null || orderGoods.getShippingCode() == null || orderGoods.getShippingNo() == null) {
			throw new DataNotFoundException("暂无物流信息");
		}

		return courierService.kuaidi100(orderGoods.getShippingCode(), orderGoods.getShippingNo());
	}

	@ApiOperation(value = "获取单条订单的商品信息")
	@GetMapping("/{id}")
	public OrderGoods findOne(@PathVariable Long id) {

		OrderGoods orderGoods = orderGoodsService.findOne(id);
		if (orderGoods == null)
			orderGoods = new OrderGoods();

		return orderGoods;
	}

	@ApiOperation(value = "根据订单号获取订单商品列表的商品信息")
	@GetMapping("/orderSn/{orderSn}")
	public Paging<OrderGoods> findListByGoodsSn(@PathVariable String orderSn) {
		
		OrderGoods entity = new OrderGoods();
		entity.setLimit(Constant.MAX_LIMIT);
		entity.setUserId(super.currentUserId());
		entity.setOrderSn(orderSn);
		
		return orderGoodsService.findPaging(entity);
	}

	@ApiOperation(value = "获取订单的商品信息列表")
	@GetMapping
	public Paging<OrderGoods> findPaging(@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit,
			@ApiParam(value = "运送状态，0:未发货，1:已发货，2:已签收", example = "0") @RequestParam(required = false) Integer[] shippingStatusArray,
			@ApiParam(value = "支付状态，0:未支付，1:已支付", example = "1") @RequestParam(required = false) Integer payStatus) {

		OrderGoods entity = new OrderGoods();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setShippingStatusArray(shippingStatusArray);
		entity.setUserId(super.currentUserId());
		entity.setPayStatus(payStatus);

		return orderGoodsService.findPaging(entity);

	}

	@ApiOperation(value = "提交一条订单的商品信息")
	@PostMapping
	public void add(@RequestBody @Validated OrderGoodsAddRequestEntity orderGoodsAddRequestEntity) {
		OrderGoods entity = new OrderGoods();
		BeanUtils.copyProperties(orderGoodsAddRequestEntity, entity);

		orderGoodsService.add(entity);
	}

	@ApiOperation(value = "确认收货")
	@PutMapping("/{id}")
	public void confirm(@PathVariable Long id) {
		OrderGoods entity = new OrderGoods();
		entity.setId(id);
		entity.setShippingStatus(ShippingStatus.signed.getCode());
		
		OrderGoods orderGoods =  orderGoodsService.findOne(id);
		if (orderGoods != null && orderGoods.getOrderSn() != null){
			entity.setOrderSn(orderGoods.getOrderSn());
			orderGoodsService.confirm(entity);
		}

		
	}

	@ApiOperation(value = "删除一条订单的商品信息")
	// @DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		OrderGoods entity = new OrderGoods();
		entity.setId(id);
		orderGoodsService.delete(entity);
	}

}
