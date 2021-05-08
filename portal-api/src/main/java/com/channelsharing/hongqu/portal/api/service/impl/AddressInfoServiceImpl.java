/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.dao.AddressInfoDao;
import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import com.channelsharing.hongqu.portal.api.constant.ConfigParamConstant;
import com.channelsharing.hongqu.portal.api.service.ConfigParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.service.AddressInfoService;
import com.channelsharing.hongqu.portal.api.entity.AddressInfo;

import javax.validation.constraints.NotNull;

/**
 * 用户地址信息Service
 * @author liuhangjun
 * @version 2018-07-16
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class AddressInfoServiceImpl extends CrudServiceImpl<AddressInfoDao, AddressInfo> implements AddressInfoService {
    
    @Autowired
    private ConfigParamService configParamService;
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;

    @Override
    public AddressInfo findOne(Long id) {
        AddressInfo entity = new AddressInfo();
        entity.setId(id);

        return super.findOne(entity);
    }
    
    @Cacheable(value = PORTAL_CACHE_PREFIX + "addressInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'addressInfo:defaultByUserId:' + #userId", unless = "#result == null")
    public AddressInfo findDefaultAddress(Long userId){
        AddressInfo query = new AddressInfo();
        query.setDefaultFlag(BooleanEnum.yes.getCode());
        query.setUserId(userId);
        
        return super.findOne(query);
    
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "addressInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'addressInfo:defaultByUserId:' + #entity.userId")
    @Transactional
    @Override
    public void add(AddressInfo entity){
        AddressInfo countQuery =  new AddressInfo();
        countQuery.setUserId(entity.getUserId());
        Integer count = super.dao.findAllCount(countQuery);
        Integer maxNumber = Integer.parseInt(configParamService.findOne(ConfigParamConstant.PORTAL_ADDRESS_MAX_NUMBER));
        if (count > maxNumber){
            throw new SystemInnerBusinessException("您的收货地址数超过系统设置的"+maxNumber+"个啦，"+ "清理下不用的地址吧");
        }
        
        this.setOtherNotDefault(entity);
        
        super.add(entity);
    
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "addressInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'addressInfo:defaultByUserId:' + #entity.userId")
    @Transactional
    @Override
    public void modify(@NotNull AddressInfo entity){
        
        if (entity.getId() == null && entity.getUserId() == null){
            throw new SystemInnerBusinessException("地址的id主键和用户id不能同时为空");
        
        }
    
        this.setOtherNotDefault(entity);
        
        super.modify(entity);
    }
    
    /**
     * /如果是增加默认的地址，则把之前的默认地址修改为不默认
     * @param entity
     */
    private void setOtherNotDefault(AddressInfo entity){
        
        if (entity.getUserId() == null || entity.getDefaultFlag() == null){
            return;
        }
        
        if (entity.getDefaultFlag().equals(BooleanEnum.yes.getCode())){
            AddressInfo query = new AddressInfo();
            query.setUserId(entity.getUserId());
            query.setDefaultFlag(BooleanEnum.yes.getCode());
            AddressInfo result = super.findOne(query);
        
            if (result != null){
                AddressInfo addressInfoUpdate = new AddressInfo();
                addressInfoUpdate.setUserId(entity.getUserId());
                addressInfoUpdate.setDefaultFlag(BooleanEnum.no.getCode());
                this.modify(addressInfoUpdate);
            }
        
        }
    }


}
