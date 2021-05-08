/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.goods;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.entity.*;
import com.channelsharing.hongqu.portal.api.service.*;
import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.common.enums.DelFlagEnum;
import com.channelsharing.pub.enums.ApproveStatus;
import com.channelsharing.hongqu.portal.api.enums.AgentFlag;
import com.channelsharing.hongqu.portal.api.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.common.entity.Paging;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品信息Controller
 *
 * @author liuhangjun
 * @version 2018-06-11
 */
@Api(tags = "商品信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/goodsInfo")
public class GoodsInfoController extends BaseController {

	@Resource
	private GoodsInfoService goodsInfoService;

	@Autowired
	private ShopGoodsService shopGoodsService;

	@Autowired
	private AddressInfoService addressInfoService;
	
	@Autowired
	private GoodsLikeService goodsLikeService;
	
	@Autowired
	private GoodsStatisticService goodsStatisticService;
	
	@Autowired
	private GoodsDescriptionService goodsDescriptionService;
	
	@Autowired
	private GoodsParamService goodsParamService;

	@ApiOperation(value = "获取单条商品信息")
	@GetMapping("/{id}")
	public GoodsInfo findOne(@PathVariable Long id, @RequestParam(required = false) boolean self) {

		GoodsInfo goodsInfo = goodsInfoService.findOne(id);
		
		if (goodsInfo == null){
			return null;
		}

		// 表示店主查看自己的商品，如果是, 那么需要查询商品的代理状态
		if (self == true && goodsInfo != null) {
			Long shopId = super.currentUser().getShopId();
			ShopGoods shopGoodsResult = shopGoodsService.findOne(shopId, goodsInfo.getId());

			if (shopGoodsResult == null || shopGoodsResult.getShopId() == null) {
				goodsInfo.setAgentFlag(AgentFlag.unAgent.getCode());
			} else {
				goodsInfo.setAgentFlag(shopGoodsResult.getStatus());
			}
		}
		
		// 图文详情
		GoodsDescription goodsDescription = goodsDescriptionService.findOne(id);
		goodsInfo.setGoodsDescription(goodsDescription);
		
		// 商品参数介绍
		List<GoodsParam> goodsParamList = goodsParamService.findList(id);
		goodsInfo.setGoodsParamList(goodsParamList);
		
        // 地址信息
		AddressInfo addressInfo = addressInfoService.findDefaultAddress(super.currentUserId());
		goodsInfo.setAddressInfo(addressInfo);

		return goodsInfo;
	}

	@ApiOperation(value = "店主获取单条商品信息")
	@GetMapping("/{id}/shopkeeper")
	public GoodsInfo findOneByShopkeeper(@PathVariable Long id) {

		GoodsInfo goodsInfo = goodsInfoService.findOne(id);
		if (goodsInfo == null) {
			goodsInfo = new GoodsInfo();
		}

		// 判断店主是否代理
		ShopGoods shopGoods = shopGoodsService.findOne(super.currentUser().getShopId(), id);
		if (shopGoods != null) {
			goodsInfo.setAgentFlag(BooleanEnum.yes.getCode());
		}

		AddressInfo addressInfo = addressInfoService.findDefaultAddress(super.currentUserId());
		goodsInfo.setAddressInfo(addressInfo);

		return goodsInfo;
	}

	@ApiOperation(value = "店主搜索所有商品信息列表")
	@GetMapping("/all")
	public Paging<GoodsInfo> findPaging(@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit, @RequestParam(required = false) String name) {

		GoodsInfo entity = new GoodsInfo();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setName(name);
		entity.setRoleType(RoleType.shopkeeper.getCode());
		entity.setOnSaleFlag(BooleanEnum.yes.getCode());
		entity.setApproveStatus(ApproveStatus.approved.getCode());
		entity.setDelFlag(DelFlagEnum.notDelete.getCode());

		Long shopId = super.currentUser().getShopId();

		return goodsInfoService.shopkeeperFindPaging(entity, shopId);

	}

	@ApiOperation(value = "用户搜索店铺内商品信息列表")
	@GetMapping("/shop")
	public Paging<GoodsInfo> findPaging(@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit, @RequestParam Long shopId,
			@RequestParam(required = false) String name) {

		GoodsInfo entity = new GoodsInfo();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setName(name);
		entity.setRoleType(RoleType.user.getCode());
		entity.setOnSaleFlag(BooleanEnum.yes.getCode());
		entity.setApproveStatus(ApproveStatus.approved.getCode());
		entity.setDelFlag(DelFlagEnum.notDelete.getCode());

		Paging<GoodsInfo> goodsInfoPaging = goodsInfoService.findPaging(entity, shopId);

		List<GoodsInfo> goodsInfoList = goodsInfoPaging.getRows();
		List<GoodsInfo> goodsInfoListNew = new ArrayList<>();
		for (GoodsInfo goodsInfo : goodsInfoList) {
			
			// 查询推荐语
			ShopGoods shopGoods = shopGoodsService.findOne(shopId, goodsInfo.getId());
			String recommend = shopGoods.getRecommend();
			goodsInfo.setRecommend(recommend);
			

			goodsInfoListNew.add(goodsInfo);
		}

		goodsInfoPaging.setRows(goodsInfoListNew);

		return goodsInfoPaging;

	}

}
