package com.channelsharing.hongqu.supplier.api.service.impl;

import javax.annotation.Resource;

import com.channelsharing.hongqu.supplier.api.entity.ShippingCompany;
import com.channelsharing.hongqu.supplier.api.service.CourierService;
import com.channelsharing.hongqu.supplier.api.service.ShippingCompanyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.channelsharing.cloud.courier.KuaiDi100;
import com.channelsharing.common.exception.DataNotFoundException;

@Service
public class CourierServiceImpl implements CourierService {
	@Resource
	private ShippingCompanyService shippingCompanyService;

	@Override
	public JSONObject kuaidi100(int shippingCode, String shippingNo) {
		ShippingCompany queryEntity = new ShippingCompany();
		queryEntity.setCode(shippingCode);

		ShippingCompany shippingCompany = shippingCompanyService.findOne(queryEntity);

		if (shippingCompany != null && StringUtils.isNotBlank(shippingCompany.getKuaidi100Com())) {
			JSONObject result = KuaiDi100.query(shippingCompany.getKuaidi100Com(), StringUtils.trim(shippingNo));
			if (result != null) {
				return result;
			}
		}

		throw new DataNotFoundException("暂无物流信息");
	}
}
