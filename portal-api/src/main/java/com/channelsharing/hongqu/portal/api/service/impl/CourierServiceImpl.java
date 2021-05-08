package com.channelsharing.hongqu.portal.api.service.impl;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.entity.ShippingCompany;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.channelsharing.cloud.courier.KuaiDi100;
import com.channelsharing.common.exception.DataNotFoundException;
import com.channelsharing.hongqu.portal.api.dao.CourierMapper;
import com.channelsharing.hongqu.portal.api.service.CourierService;

@Service
public class CourierServiceImpl implements CourierService {
	@Resource
	private CourierMapper courierMapper;

	@Override
	public JSONObject kuaidi100(int shippingCode, String shippingNo) {
		ShippingCompany shippingCompany = courierMapper.selectShippingCompany(shippingCode);

		if (shippingCompany != null && StringUtils.isNotBlank(shippingCompany.getKuaidi100Com())) {
			JSONObject result = KuaiDi100.query(shippingCompany.getKuaidi100Com(), StringUtils.trim(shippingNo));
			if (result != null) {
				return result;
			}
		}

		throw new DataNotFoundException("暂无物流信息");
	}
}
