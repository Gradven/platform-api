/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.product;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.ProductInfo;
import com.channelsharing.hongqu.portal.api.service.ProductInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;


/**
 * 商品的产品信息Controller
 * @author liuhangjun
 * @version 2018-06-20
 */
@Api(tags = "产品信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/productInfo")
public class ProductInfoController extends BaseController {

	@Resource
	private ProductInfoService productInfoService;

    @ApiOperation(value = "获取单条商品的产品信息")
	//@GetMapping("/{id}")
	public ProductInfo findOne(@PathVariable Long id){

	    ProductInfo productInfo = productInfoService.findOne(id);
	    if (productInfo== null)
			productInfo =  new ProductInfo();

		return productInfo;
	}

	@ApiOperation(value = "获取商品的产品信息列表")
	@GetMapping
	public List<ProductInfo> findList(@RequestParam Long goodsId){
		
		return productInfoService.findList(goodsId);

	}
	

}
