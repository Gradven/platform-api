/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.shop;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.ShopInfo;
import com.channelsharing.hongqu.portal.api.entity.ShopVisit;
import com.channelsharing.hongqu.portal.api.service.ShopInfoService;
import com.channelsharing.hongqu.portal.api.service.ShopVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;


/**
 * 店铺访问记录Controller
 * @author liuhangjun
 * @version 2018-07-08
 */
@Api(tags = "店铺访问记录操作接口")
@Validated
@RestController
@RequestMapping("/v1/shopVisit")
public class ShopVisitController extends BaseController {

	@Resource
	private ShopVisitService shopVisitService;
	
	@Autowired
	private ShopInfoService shopInfoService;


	@ApiOperation(value = "获取店铺访问记录列表")
	@GetMapping
	public List<ShopInfo> findList(){

		ShopVisit entity = new ShopVisit();
		entity.setUserId(super.currentUser().getId());
		
		List<ShopVisit> shopVisitList =  shopVisitService.findList(entity);
		List<ShopInfo> shopInfoList = new ArrayList<>();
		for (ShopVisit shopVisit : shopVisitList){
			
			ShopInfo shopInfo = shopInfoService.findOne(shopVisit.getShopId());
			shopInfoList.add(shopInfo);
		}
		
		return shopInfoList;

	}
	

	@ApiOperation(value = "删除一条店铺访问记录")
	//@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id){
	    ShopVisit entity = new ShopVisit();
	    entity.setId(id);
		shopVisitService.delete(entity);
	}

}
