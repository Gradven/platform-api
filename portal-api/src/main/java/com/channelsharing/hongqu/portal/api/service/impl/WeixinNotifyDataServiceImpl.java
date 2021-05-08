/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.hongqu.portal.api.entity.WeixinNotifyData;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.service.WeixinNotifyDataService;
import com.channelsharing.hongqu.portal.api.dao.WeixinNotifyDataDao;

/**
 * 微信回调数据记录Service
 * @author liuhangjun
 * @version 2018-03-29
 */
@Service
public class WeixinNotifyDataServiceImpl extends CrudServiceImpl<WeixinNotifyDataDao, WeixinNotifyData> implements WeixinNotifyDataService {
    
    
    @Override
    public WeixinNotifyData findOne(Long id) {
        WeixinNotifyData entity = new WeixinNotifyData();
        entity.setId(id);
        
        return super.findOne(entity);
    }
}
