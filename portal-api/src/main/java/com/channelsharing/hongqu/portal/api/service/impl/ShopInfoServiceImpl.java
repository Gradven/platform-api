/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.dao.ShopInfoDao;
import com.channelsharing.hongqu.portal.api.entity.ShopInfo;
import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.hongqu.portal.api.service.ConfigParamService;
import com.channelsharing.hongqu.portal.api.service.ShopInfoService;
import com.channelsharing.hongqu.portal.api.service.UserInfoService;
import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import com.channelsharing.common.utils.RandomUtil;
import com.channelsharing.hongqu.portal.api.constant.ConfigParamConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;

/**
 * 店铺信息Service
 * @author liuhangjun
 * @version 2018-06-11
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class ShopInfoServiceImpl extends CrudServiceImpl<ShopInfoDao, ShopInfo> implements ShopInfoService {
    
    @Autowired
    private UserInfoService userInfoService;
    
    @Autowired
    private ConfigParamService configParamService;
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;
    
    /**
     * 添加店铺信息的同时，在用户信息表中，设置此用户拥有了店铺
     * @param entity
     */
    @Transactional
    @Override
     public void add(ShopInfo entity){
        
        ShopInfo shopInfoQuery = new ShopInfo();
        shopInfoQuery.setUserId(entity.getUserId());
        ShopInfo shopInfoResult = super.findOne(shopInfoQuery);
        if (shopInfoResult != null){
            throw new SystemInnerBusinessException("一个用户只能创建一个店铺");
        }
    
        shopInfoQuery = new ShopInfo();
        shopInfoQuery.setName(entity.getName());
        shopInfoResult = super.findOne(shopInfoQuery);
        if (shopInfoResult != null){
            // 名称重复了，在名称后面加上随机数
            String name = entity.getName() + "_" + RandomUtil.createRandomCharData(4);
            entity.setName(name);
        }
        
        ShopInfo shopInfo = super.addWithResult(entity);
    
        // 更新userInfo shopId字段,设置此用户拥有了店铺
        UserInfo userInfo = new UserInfo();
        userInfo.setId(entity.getUserId());
        userInfo.setShopId(shopInfo.getId());
        userInfoService.modify(userInfo);
     }
    
    @Cacheable(value = PORTAL_CACHE_PREFIX + "shopInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'shopInfo:id:' + #id", unless = "#result == null")
    @Override
    public ShopInfo findOne(Long id) {
        ShopInfo entity = new ShopInfo();
        entity.setId(id);
    
        ShopInfo shopInfo = super.findOne(entity);
        
        if (shopInfo != null ){
            
            if (StringUtils.isBlank(shopInfo.getBackgroundUrl())){
                shopInfo.setBackgroundUrl(configParamService.findOne(ConfigParamConstant.PORTAL_SHOP_DEFAULT_BACKGROUND_URL));
            }
    
            if (StringUtils.isBlank(shopInfo.getDescription())){
                shopInfo.setDescription(configParamService.findOne(ConfigParamConstant.PORTAL_SHOP_DEFAULT_DESCRIPTION));
            }
            
        }
        
        return shopInfo;
    }
    
    
     @CacheEvict(value = PORTAL_CACHE_PREFIX + "shopInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'shopInfo:id:' + #entity.id")
     @Transactional
     @Override
     public void modify(ShopInfo entity){
         ShopInfo shopInfoQuery = new ShopInfo();
         shopInfoQuery.setExactMatchName(entity.getName());
         ShopInfo shopInfoResult = super.findOne(shopInfoQuery);
         
         // 判断名称是否与其他的店铺名称重复
         if (shopInfoResult != null && shopInfoResult.getName().equals(entity.getName()) &&
                 !entity.getUserId().equals(shopInfoResult.getUserId())){
    
             throw new SystemInnerBusinessException("店铺名称重复了，另取一个名字吧");
         
         }
         
         super.modify(entity);
     }
    
    /**
     * 查询是否有此店主的信息
     * @param name
     * @return
     */
    public ShopInfo findByName(String name){
    
        return super.dao.findByName(name);
    }
    
    

}
