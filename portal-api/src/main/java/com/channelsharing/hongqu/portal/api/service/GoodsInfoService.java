/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.entity.Paging;
import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.portal.api.entity.GoodsInfo;

/**
 * 商品信息Service
 *
 * @author liuhangjun
 * @version 2018-06-11
 */
public interface GoodsInfoService extends CrudService<GoodsInfo> {

	Paging<GoodsInfo> findPaging(GoodsInfo entity, Long shopId);

	/**
	 * 增加销量
	 *
	 * @param addNum
	 * @param id
	 *            商品id
	 */
	void addSalesVolume(Integer addNum, Long id);

	/**
	 * 增加库存
	 *
	 * @param addNum
	 * @param id
	 *            商品id
	 */
	void addStoreNumber(Integer addNum, Long id);

	Paging<GoodsInfo> shopkeeperFindPaging(GoodsInfo entity, Long shopId);

	Paging<GoodsInfo> searchGoods(GoodsInfo queryEntity, Long shopId);
}
