/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.hongqu.portal.api.entity.ShopSalesData;
import com.channelsharing.hongqu.portal.api.service.ShopSalesDataService;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.dao.ShopSalesDataMapper;

@Service
public class ShopSalesDataServiceImpl extends CrudServiceImpl<ShopSalesDataMapper, ShopSalesData>
		implements ShopSalesDataService {
	@Override
	public ShopSalesData findOne(Long id) {
		ShopSalesData queryEntity = new ShopSalesData();
		queryEntity.setId(id);

		return dao.findOne(queryEntity);
	}

	@Override
	public ShopSalesData findByShopId(Long shopId) {
		ShopSalesData queryEntity = new ShopSalesData();
		queryEntity.setShopId(shopId);

		return dao.findOne(queryEntity);
	}
}
