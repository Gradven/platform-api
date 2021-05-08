/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.shop;

import java.util.List;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.entity.OrderGoods;
import com.channelsharing.hongqu.portal.api.entity.ShopInfo;
import com.channelsharing.hongqu.portal.api.entity.ShopUserVisit;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.channelsharing.common.entity.Paging;
import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.common.exception.ForbiddenException;
import com.channelsharing.common.lock.method.CacheLock;
import com.channelsharing.common.lock.method.CacheParam;
import com.channelsharing.hongqu.portal.api.constant.ConfigParamConstant;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.ShopVisit;
import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.hongqu.portal.api.service.ConfigParamService;
import com.channelsharing.hongqu.portal.api.service.OrderGoodsService;
import com.channelsharing.hongqu.portal.api.service.ShopInfoService;
import com.channelsharing.hongqu.portal.api.service.ShopUserVisitService;
import com.channelsharing.hongqu.portal.api.service.ShopVisitService;
import com.channelsharing.hongqu.portal.api.service.UserInfoService;
import com.channelsharing.pub.enums.PayStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 店铺信息Controller
 *
 * @author liuhangjun
 * @version 2018-06-11
 */
@Api(tags = "店铺信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/shopInfo")
public class ShopInfoController extends BaseController {

	@Resource
	private ShopInfoService shopInfoService;

	@Autowired
	private ShopVisitService shopVisitService;

	@Resource
	private ShopUserVisitService shopUserVisitService;

	@Resource
	private UserInfoService userInfoService;

	@Autowired
	private ConfigParamService configParamService;

	@Resource
	private OrderGoodsService orderGoodsService;

	@ApiOperation(value = "店主查询自己店铺订单列表")
	@GetMapping("/order_goods")
	public Paging<OrderGoods> getShopOrderGoodsList(@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit) {
		UserInfo currentUser = super.currentUser();
		if (currentUser.getShopId() != null) {
			OrderGoods queryEntity = new OrderGoods();
			queryEntity.setOffset(offset);
			queryEntity.setLimit(limit);
			queryEntity.setShopId(currentUser.getShopId());
			queryEntity.setPayStatus(PayStatus.paid.getCode());

			return orderGoodsService.findPaging(queryEntity);
		}

		throw new ForbiddenException("您不是店主，不能查看销售的订单");
	}

	@ApiOperation(value = "查询店铺访问用户列表")
	@GetMapping("/{shopId}/visitors")
	public Paging<ShopUserVisit> getVisitors(@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit, @PathVariable Long shopId) {
		ShopUserVisit queryEntity = new ShopUserVisit();
		queryEntity.setOffset(offset);
		queryEntity.setLimit(limit);
		queryEntity.setShopId(shopId);

		return shopUserVisitService.findPaging(queryEntity);
	}

	@ApiOperation(value = "获取单条店铺信息")
	@GetMapping("/{id}")
	public ShopInfo findOne(@PathVariable Long id) {

		ShopInfo shopInfo = shopInfoService.findOne(id);
		if (shopInfo == null)
			return null;

		UserInfo userInfo = userInfoService.findOne(shopInfo.getUserId());
		shopInfo.setUserInfo(userInfo);

		UserInfo currentUser = super.currentUserWithoutException();

		// 访问别人的店铺才记录店铺访问信息
		if (currentUser != null && !shopInfo.getId().equals(currentUser.getShopId())) {
			ShopVisit shopVisit = new ShopVisit();
			shopVisit.setShopId(shopInfo.getId());
			shopVisit.setUserId(currentUser.getId());
			shopVisitService.add(shopVisit); // 执行的是INSERT ON DUPLICATE KEY UPDATE语句

			shopVisit = new ShopVisit();
			shopVisit.setUserId(currentUser.getId());
			shopVisit.setLimit(Constant.MAX_LIMIT);
			List<ShopVisit> shopVisitList = shopVisitService.findList(shopVisit);
			if (shopVisitList != null) {
				int maxNumber = NumberUtils
						.toInt(configParamService.findOne(ConfigParamConstant.PORTAL_SHOP_VISIT_MAX_NUMBER), 10);
				int size = shopVisitList.size();
				if (size > maxNumber) {
					for (int i = maxNumber; i < size; i++) {
						shopVisitService.delete(shopVisitList.get(i));
					}
				}
			}

			ShopUserVisit shopUserVisit = new ShopUserVisit();
			shopUserVisit.setShopId(shopInfo.getId());
			shopUserVisit.setUserId(currentUser.getId());
			shopUserVisitService.add(shopUserVisit);
		}

		return shopInfo;
	}

	@ApiOperation(value = "搜索店铺信息列表")
	@GetMapping
	public Paging<ShopInfo> findPaging(@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit, @RequestParam(required = false) String name) {

		ShopInfo entity = new ShopInfo();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setName(name);

		return shopInfoService.findPaging(entity);

	}

	@CacheLock(prefix = "portal:shopInfoController:add")
	@ApiOperation(value = "提交一条店铺信息")
	@PostMapping
	public void add(
			@CacheParam(name = "shopInfoAddRequestEntity") @RequestBody @Validated ShopInfoAddRequestEntity shopInfoAddRequestEntity) {
		ShopInfo entity = new ShopInfo();
		BeanUtils.copyProperties(shopInfoAddRequestEntity, entity);
		entity.setUserId(super.currentUserId());
		entity.setCertificateFlag(BooleanEnum.no.getCode());

		shopInfoService.add(entity);
	}

	@ApiOperation(value = "修改一条店铺信息")
	@PutMapping
	public ShopInfo modify(@RequestBody @Validated ShopInfoModifyRequestEntity shopInfoModifyRequestEntity) {
		ShopInfo entity = new ShopInfo();
		BeanUtils.copyProperties(shopInfoModifyRequestEntity, entity);
		entity.setUserId(super.currentUserId());

		shopInfoService.modify(entity);

		return shopInfoService.findOne(entity.getId());
	}

	@ApiOperation(value = "删除一条店铺信息")
	// @DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		ShopInfo entity = new ShopInfo();
		entity.setId(id);
		shopInfoService.delete(entity);
	}

}
