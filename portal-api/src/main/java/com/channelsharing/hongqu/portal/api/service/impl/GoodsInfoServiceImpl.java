/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.entity.Paging;
import com.channelsharing.common.exception.DataNotFoundException;
import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.dao.GoodsInfoDao;
import com.channelsharing.hongqu.portal.api.entity.GoodsInfo;
import com.channelsharing.hongqu.portal.api.entity.ShopGoods;
import com.channelsharing.hongqu.portal.api.enums.AgentFlag;
import com.channelsharing.hongqu.portal.api.enums.RoleType;
import com.channelsharing.hongqu.portal.api.service.GoodsInfoService;
import com.channelsharing.hongqu.portal.api.service.ShopGoodsService;

/**
 * 商品信息Service
 *
 * @author liuhangjun
 * @version 2018-06-11
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class GoodsInfoServiceImpl extends CrudServiceImpl<GoodsInfoDao, GoodsInfo> implements GoodsInfoService {

	@Autowired
	private ShopGoodsService shopGoodsService;

	public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;

	@Cacheable(value = PORTAL_CACHE_PREFIX
			+ "goodsInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsInfo:id:' + #id", unless = "#result == null")
	@Override
	public GoodsInfo findOne(@NotNull Long id) {

		GoodsInfo entity = new GoodsInfo();
		entity.setId(id);
		GoodsInfo goodsInfo = super.findOne(entity);

		if (goodsInfo == null)
			throw new DataNotFoundException("没有找到此商品的信息");

		return goodsInfo;

	}

	@CacheEvict(value = PORTAL_CACHE_PREFIX
			+ "goodsInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsInfo:id:' + #entity.id")
	@Override
	public void modify(@NotNull GoodsInfo entity) {
		super.modify(entity);
	}

	@Override
	public Paging<GoodsInfo> searchGoods(GoodsInfo queryEntity, Long shopId) {
		Paging<GoodsInfo> goodsInfoPaging = super.findPaging(queryEntity);
		if (shopId != null) {
			goodsInfoPaging.getRows().forEach(goods -> {
				ShopGoods shopGoodsResult = shopGoodsService.findOne(shopId, goods.getId());
				goods.setAgentFlag(
						(shopGoodsResult == null || shopGoodsResult.getShopId() == null) ? AgentFlag.unAgent.getCode()
								: shopGoodsResult.getStatus());
			});
		}

		return goodsInfoPaging;
	}

	/**
	 * 店主搜索商品
	 *
	 * @param entity
	 * @return
	 */
	@Override
	public Paging<GoodsInfo> shopkeeperFindPaging(GoodsInfo entity, Long shopId) {

		Paging<GoodsInfo> goodsInfoPaging = super.findPaging(entity);

		// 如果是店主搜索，那么判断店主是否代理了商品
		if (RoleType.shopkeeper.getCode().equals(entity.getRoleType())) {

			List<GoodsInfo> goodsInfoList = goodsInfoPaging.getRows();

			List<GoodsInfo> goodsInfoListNew = new ArrayList<>();
			for (GoodsInfo goodsInfo : goodsInfoList) {

				ShopGoods shopGoodsResult = shopGoodsService.findOne(shopId, goodsInfo.getId());

				if (shopGoodsResult == null || shopGoodsResult.getShopId() == null) {
					goodsInfo.setAgentFlag(AgentFlag.unAgent.getCode());
				} else {
					goodsInfo.setAgentFlag(shopGoodsResult.getStatus());
				}
				
				goodsInfoListNew.add(goodsInfo);
			}

			goodsInfoPaging.setRows(goodsInfoListNew);
		}

		return goodsInfoPaging;

	}

	/**
	 * 用户根据店铺搜索商品
	 *
	 * @param entity
	 * @param shopId
	 * @return
	 */
	@Override
	public Paging<GoodsInfo> findPaging(GoodsInfo entity, @NotNull Long shopId) {
		Paging<GoodsInfo> paging = new Paging<>();
		paging.setCount(dao.findAllCountByShop(entity, shopId));
		paging.setRows(dao.findListByShop(entity, shopId));

		return paging;

	}

	/**
	 * 增加销量
	 *
	 * @param addNum
	 * @param id
	 *            商品id
	 */
	@CacheEvict(value = PORTAL_CACHE_PREFIX
			+ "goodsInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsInfo:id:' + #id")
	@Override
	public void addSalesVolume(Integer addNum, Long id) {
		super.dao.addSalesVolume(addNum, id);
	}

	/**
	 * 增加库存
	 *
	 * @param addNum
	 * @param id
	 *            商品id
	 */
	@CacheEvict(value = PORTAL_CACHE_PREFIX
			+ "goodsInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsInfo:id:' + #id")
	@Override
	public void addStoreNumber(Integer addNum, Long id) {
		super.dao.addStoreNumber(addNum, id);

	}

}
