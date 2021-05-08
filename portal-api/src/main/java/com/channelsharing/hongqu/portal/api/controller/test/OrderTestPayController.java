/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.test;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.OrderInfo;
import com.channelsharing.hongqu.portal.api.entity.OrderShopServe;
import com.channelsharing.hongqu.portal.api.service.OrderShopServeService;
import com.channelsharing.pub.enums.CancelType;
import com.channelsharing.hongqu.portal.api.service.OrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 此接口只能在开发或者测试环境中使用
 * @author liuhangjun
 * @version 2018-06-20
 */
@Profile({"test", "dev"})
@Api(tags = "Test测试桩-订单支付操作接口")
@Validated
@RestController
@RequestMapping("/v1/test/order")
public class OrderTestPayController extends BaseController {
	
	@Resource
	private OrderShopServeService orderShopServiceService;
	
	@Autowired
	private OrderInfoService orderInfoService;
	
	/**
	 * 此接口只能在开发或者测试环境中使用
	 * @param sn
	 */
	@ApiOperation(value = "店铺技术服务费订单支付测试桩")
	@GetMapping("/pay/shop/{sn}")
	public void payShopOrder(@PathVariable String sn,
							 @ApiParam(value = "支付类型，1：微信支付，2：支付宝", example = "1")
							 @RequestParam Integer payType){
		
		BigDecimal money =  BigDecimal.valueOf(10.11);
		
		OrderShopServe orderShopService = new OrderShopServe();
		orderShopService.setSn(sn);
		orderShopService.setPayMoney(money);
		orderShopService.setPayNo("test_a_");
		orderShopService.setPayTime(new Date());
		orderShopService.setPayType(payType);
		
		orderShopServiceService.payOrder(orderShopService);
	}
	
	@ApiOperation(value = "店铺技术服务费订单取消测试桩")
	@GetMapping("/cancel/shop/{sn}")
	public void cancelShopOrder(@PathVariable String sn, CancelType cancelType,
								@ApiParam(value = "备注", example = "我就是想取消")
										String remark){
		
		orderShopServiceService.cancelOrder(sn, cancelType, super.currentUserId(), remark);
	}
	
	@ApiOperation(value = "商品订单支付测试桩")
	@GetMapping("/pay/goods/{sn}")
	public void payGoodsOrder(@PathVariable String sn,
							  @ApiParam(value = "支付类型，1：微信支付，2：支付宝", example = "1")
							  @RequestParam Integer payType){
		
		BigDecimal money = BigDecimal.valueOf(10.11);

		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setSn(sn);
		orderInfo.setPayMoney(money);
		orderInfo.setPayNo("test_");
		orderInfo.setPayTime(new Date());
		orderInfo.setPayType(payType);
		orderInfoService.payOrder(orderInfo);
		
	}
	
	@ApiOperation(value = "商品订单取消测试桩")
	@GetMapping("/cancel/goods/{sn}")
	public void cancelGoodsOrder(@PathVariable String sn, CancelType cancelType,
								 @ApiParam(value = "备注", example = "我就是想取消")
								 String remark){
		
		orderInfoService.cancelOrder(sn, cancelType, null, remark);
		
	}
	
}


