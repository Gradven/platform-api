/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.hongqu.portal.api.dao.RegionInfoDao;
import com.channelsharing.hongqu.portal.api.entity.RegionInfo;
import com.channelsharing.hongqu.portal.api.service.RegionInfoService;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;

/**
 * 国家地区信息Service
 * @author liuhangjun
 * @version 2018-07-16
 */
@Service
public class RegionInfoServiceImpl extends CrudServiceImpl<RegionInfoDao, RegionInfo> implements RegionInfoService {

    @Override
    public RegionInfo findOne(Long id) {
        RegionInfo entity = new RegionInfo();
        entity.setId(id);

        return super.findOne(entity);
    }


}
