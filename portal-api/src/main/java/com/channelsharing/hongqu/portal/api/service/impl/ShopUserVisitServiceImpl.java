/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.hongqu.portal.api.entity.ShopUserVisit;
import com.channelsharing.hongqu.portal.api.service.ShopUserVisitService;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.dao.ShopUserVisitMapper;

@Service
public class ShopUserVisitServiceImpl extends CrudServiceImpl<ShopUserVisitMapper, ShopUserVisit>
		implements ShopUserVisitService {
	@Override
	public ShopUserVisit findOne(Long id) {
		ShopUserVisit entity = new ShopUserVisit();
		entity.setId(id);

		return super.findOne(entity);
	}
}
