/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.channelsharing.hongqu.portal.api.entity.*;
import com.channelsharing.hongqu.portal.api.enums.ShopGoodsStatus;
import com.channelsharing.hongqu.portal.api.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.entity.Paging;
import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.common.exception.BadRequestException;
import com.channelsharing.common.exception.DataNotFoundException;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.common.utils.RandomUtil;
import com.channelsharing.hongqu.portal.api.constant.ConfigParamConstant;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.dao.ShopGoodsDao;

/**
 * 店铺代理商品信息Service
 *
 * @author liuhangjun
 * @version 2018-06-12
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class ShopGoodsServiceImpl extends CrudServiceImpl<ShopGoodsDao, ShopGoods> implements ShopGoodsService {

	@Autowired
	private GoodsInfoService goodsInfoService;

	@Autowired
	private ShopInfoService shopInfoService;

	@Autowired
	private ConfigParamService configParamService;

	@Autowired
	private RecommendSentenceService recommendSentenceService;
	
	@Autowired
	private GoodsLikeService goodsLikeService;
	
	@Autowired
	private GoodsStatisticService goodsStatisticService;

	public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;

	@Cacheable(value = PORTAL_CACHE_PREFIX
			+ "shopGoods", key = "#root.target.PORTAL_CACHE_PREFIX + 'shopGoods:id:' + #id")
	@Override
	public ShopGoods findOne(Long id) {
		ShopGoods entity = new ShopGoods();
		entity.setId(id);

		ShopGoods shopGoods = super.findOne(entity);

		if (shopGoods == null)
			return new ShopGoods();

		GoodsInfo goodsInfo = new GoodsInfo();
		goodsInfo.setId(shopGoods.getGoodsId());
		GoodsInfo goodsInfoResult = goodsInfoService.findOne(goodsInfo);

		shopGoods.setGoodsInfo(goodsInfoResult);

		return shopGoods;

	}

	@Cacheable(value = PORTAL_CACHE_PREFIX
			+ "shopGoods", key = "#root.target.PORTAL_CACHE_PREFIX + 'shopGoods:shopId_goodsId:' + #shopId + '_' + #goodsId")
	@Override
	public ShopGoods findOne(@NotNull Long shopId, @NotNull Long goodsId) {

		ShopGoods entity = new ShopGoods();
		entity.setShopId(shopId);
		entity.setGoodsId(goodsId);

		ShopGoods shopGoods = super.findOne(entity);

		if (shopGoods == null)
			return new ShopGoods();

		return shopGoods;
	}

	/**
	 * 需要清除两个缓存 一个是以ShopGoods.id为key 一个是以ShopGoods.shopId和ShopGoods.goodsId组合的key
	 * 修改商品状态时需要根据加入商品的分类来维护本店商品的分类
	 *
	 * @param entity
	 */
	@Caching(evict = { @CacheEvict(value = PORTAL_CACHE_PREFIX
			+ "shopGoods", key = "#root.target.PORTAL_CACHE_PREFIX + 'shopGoods:id:' + #entity.id", condition = "#entity.id !=null"),
			@CacheEvict(value = PORTAL_CACHE_PREFIX
					+ "shopGoods", key = "#root.target.PORTAL_CACHE_PREFIX + 'shopGoods:shopId_goodsId:' + #entity.shopId + '_' +#entity.goodsId"),
			@CacheEvict(value = PORTAL_CACHE_PREFIX
					+ "shopGoodsCategories", key = "#root.target.PORTAL_CACHE_PREFIX + 'shopGoodsCategories:shopId:' + #entity.shopId") })
	@Transactional
	@Override
	public void modify(@NotNull ShopGoods entity) {

		if (entity.getShopId() == null && entity.getId() == null) {
			throw new BadRequestException("shopGoods的主键id和shopId不能同时为null");
		}
		
		// 判断代理商品是否超限
		this.shopAgentGoodsMaxNumber(entity.getShopId());
		
		// 优先用用户的推荐语，如果用户没填写用运营的推荐语
		if (StringUtils.isBlank(entity.getRecommend())) {
			entity.setRecommend(this.getRandomRecommend());
		}

		super.modify(entity);

		GoodsInfo goodsInfo = goodsInfoService.findOne(entity.getGoodsId());
		if (goodsInfo == null) {
			throw new DataNotFoundException("没有找到此商品");
		}
	}

	@Override
	public Paging<ShopGoods> findPaging(ShopGoods entity, Long userId) {
		Paging<ShopGoods> shopGoodsPaging = super.findPaging(entity);
		List<ShopGoods> shopGoodsList = shopGoodsPaging.getRows();

		List<ShopGoods> shopGoodsListNew = new ArrayList<>();
		for (ShopGoods shopGoods : shopGoodsList) {
			GoodsInfo goodsInfoResult = goodsInfoService.findOne(shopGoods.getGoodsId());
			
			// 查询用户是否点赞
			GoodsLike goodsLike = goodsLikeService.findOne(goodsInfoResult.getId(), userId);
			if (goodsLike != null && goodsLike.getGoodsId() != null){
				goodsInfoResult.setLikeFlag(BooleanEnum.yes.getCode());
			}
			
			// 查询商品的点赞数
			GoodsStatistic goodsStatistic = goodsStatisticService.findOneByGoodsId(goodsInfoResult.getId());
			goodsInfoResult.setLikeCount(goodsStatistic.getLikeCount());
			
			shopGoods.setGoodsInfo(goodsInfoResult);
			
			shopGoodsListNew.add(shopGoods);
		}

		shopGoodsPaging.setRows(shopGoodsListNew);

		return shopGoodsPaging;

	}

	/**
	 * 添加商品时判断店主是否缴了年费 添加商品时需要根据加入商品的分类来维护本店商品的分类
	 *
	 * @param entity
	 */
	@Caching(evict = { @CacheEvict(value = PORTAL_CACHE_PREFIX
			+ "shopGoods", key = "#root.target.PORTAL_CACHE_PREFIX + 'shopGoods:shopId_goodsId:' + #entity.shopId + '_' +#entity.goodsId"),
			@CacheEvict(value = PORTAL_CACHE_PREFIX
					+ "shopGoodsCategories", key = "#root.target.PORTAL_CACHE_PREFIX + 'shopGoodsCategories:shopId:' + #entity.shopId") })
	@Transactional
	@Override
	public void add(ShopGoods entity) {
		ShopInfo shopInfo = shopInfoService.findOne(entity.getShopId());

		if (shopInfo.getPayFeeFlag().equals(BooleanEnum.no.getCode()) || shopInfo.getExpireTime() == null) {
			throw new SystemInnerBusinessException("您还未缴纳技术服务年费，赶紧去支付年费吧");
		}

		Date now = new Date();
		if (shopInfo.getExpireTime().before(now)) {
			throw new SystemInnerBusinessException("您的技术服务年费已过期，请尽快续费哦");
		}

		ShopGoods shopGoodsQuery = new ShopGoods();
		shopGoodsQuery.setGoodsId(entity.getGoodsId());
		shopGoodsQuery.setShopId(entity.getShopId());
		ShopGoods shopGoods = super.findOne(shopGoodsQuery);


		// 判断代理商品是否超限
		this.shopAgentGoodsMaxNumber(entity.getShopId());
		

		if (shopGoods != null) {
			throw new SystemInnerBusinessException("此商品已经在店铺中啦");
		}

		// 添加商品时需要根据加入商品的分类来维护本店商品的分类
		GoodsInfo goodsInfo = goodsInfoService.findOne(entity.getGoodsId());
		if (goodsInfo == null) {
			throw new DataNotFoundException("没有找到此商品");
		}

		// 优先用用户的推荐语，如果用户没填写用运营的推荐语
		if (StringUtils.isBlank(entity.getRecommend())) {
			entity.setRecommend(this.getRandomRecommend());
		}

		// 最后才添加商品
		super.add(entity);

	}

	/**
	 * 获取随机运营推荐语
	 *
	 * @return
	 */
	private String getRandomRecommend() {

		String recommend = "";

		List<RecommendSentence> list = recommendSentenceService.findList();

		int size = list.size();
		if (size == 0) {
			return recommend;
		}

		int random = RandomUtil.getRandomNum(0, size - 1);
		RecommendSentence recommendSentence = list.get(random);
		recommend = recommendSentence.getRecommend();

		return recommend;
	}
	
	
	/**
	 * 判断代理商品是否超限
	 * @param shopId
	 */
	private void shopAgentGoodsMaxNumber(Long shopId){
		ShopGoods shopGoodsQuery = new ShopGoods();
		shopGoodsQuery.setShopId(shopId);
		shopGoodsQuery.setStatus(ShopGoodsStatus.add.getCode());
		int shopGoodsCount = super.dao.findAllCount(shopGoodsQuery);
		int maxNumber = NumberUtils
				.toInt(configParamService.findOne(ConfigParamConstant.PORTAL_SHOP_AGENT_GOODS_MAX_NUMBER));
		if (shopGoodsCount > maxNumber) {
			throw new SystemInnerBusinessException("代理的商品数量超过了最大[" + maxNumber + "]限制");
		}
	}

}
