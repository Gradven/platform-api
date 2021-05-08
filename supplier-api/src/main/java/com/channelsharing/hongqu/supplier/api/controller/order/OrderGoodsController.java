/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.order;

import java.util.Date;

import javax.annotation.Resource;

import com.channelsharing.hongqu.supplier.api.entity.OrderGoods;
import com.channelsharing.hongqu.supplier.api.service.CourierService;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.channelsharing.common.entity.Paging;
import com.channelsharing.common.exception.DataNotFoundException;
import com.channelsharing.pub.enums.ShippingStatus;
import com.channelsharing.hongqu.supplier.api.controller.BaseController;
import com.channelsharing.hongqu.supplier.api.service.OrderGoodsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 订单的商品信息Controller
 *
 * @author liuhangjun
 * @version 2018-07-01
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

	@ApiOperation(value = "查询订单商品物流信息", notes = "sn:订单编号|shippingCode:快递公司编号|shippingNo:快递单号")
	@GetMapping("/{id}/couriers")
	public JSONObject queryCourierInfo(@PathVariable Long id) {
		OrderGoods queryEntity = new OrderGoods();
		queryEntity.setId(id);
		queryEntity.setSupplierId(super.currentSupplierUser().getSupplierId());

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

	@ApiOperation(value = "获取订单的商品信息列表")
	@GetMapping
	public Paging<OrderGoods> findPaging(@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit,
			@ApiParam(value = "订单编号", defaultValue = "2018010101010101") @RequestParam(required = false) String orderSn,
			@RequestParam(required = false) Long userId,
			@ApiParam(value = "支付状态，0：未支付，1：已支付", defaultValue = "1") @RequestParam(required = false) Integer payStatus,
			@ApiParam(value = "运送状态，0:未发货，1:已发货，2:已签收", example = "0") @RequestParam(required = false) Integer shippingStatus,
			@ApiParam(value = "开始时间范围", defaultValue = "2018-01-01 00:00:00") @RequestParam(required = false) Date beginCreateTime,
			@ApiParam(value = "开始时间范围", defaultValue = "2018-09-01 00:00:00") @RequestParam(required = false) Date endCreateTime) {

		OrderGoods entity = new OrderGoods();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setOrderSn(orderSn);
		entity.setSupplierId(super.currentSupplierUser().getSupplierId());
		entity.setUserId(userId);
		entity.setBeginCreateTime(beginCreateTime);
		entity.setEndCreateTime(endCreateTime);
		entity.setPayStatus(payStatus);
		entity.setShippingStatus(shippingStatus);

		return orderGoodsService.findPaging(entity);

	}

	@ApiOperation(value = "订单商品发货")
	@PatchMapping
	public void shippingGoods(@RequestBody @Validated OrderGoodsModifyRequestEntity orderGoodsModifyRequestEntity) {
		OrderGoods entity = new OrderGoods();
		BeanUtils.copyProperties(orderGoodsModifyRequestEntity, entity);
		entity.setShippingStatus(ShippingStatus.shipped.getCode());
		entity.setDeliveryTime(new Date());

		orderGoodsService.modify(entity);
	}

	@ApiOperation(value = "删除一条订单的商品信息")
	// @DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		OrderGoods entity = new OrderGoods();
		entity.setId(id);
		orderGoodsService.delete(entity);
	}

}
