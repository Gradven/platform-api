/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.service.ConfigParamService;
import com.channelsharing.hongqu.portal.api.entity.ConfigParam;
import com.channelsharing.hongqu.portal.api.dao.ConfigParamDao;

/**
 * 系统配置项Service
 * @author liuhangjun
 * @version 2018-06-17
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class ConfigParamServiceImpl extends CrudServiceImpl<ConfigParamDao, ConfigParam> implements ConfigParamService {
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;
    
    
    @Cacheable(value = PORTAL_CACHE_PREFIX +"configParam", key = "#root.target.PORTAL_CACHE_PREFIX + 'configParam:key:' + #key", unless = "#result == null")
    @Override
    public String findOne(String key) {
        ConfigParam entity = new ConfigParam();
        entity.setConfigKey(key);
    
        ConfigParam configParam = super.findOne(entity);
        
        if (configParam != null){
            return configParam.getConfigValue();
        }else {
            return null;
        }
        
    }
    
    @Override
    public ConfigParam findOne(Long id) {
        ConfigParam entity = new ConfigParam();
        entity.setId(id);
        return super.findOne(entity);
    }
}
