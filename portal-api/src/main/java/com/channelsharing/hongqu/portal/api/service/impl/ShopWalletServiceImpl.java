/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.entity.ShopWallet;
import com.channelsharing.hongqu.portal.api.service.ShopWalletService;
import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.dao.ShopWalletDao;

/**
 * 店铺钱包Service
 * @author liuhangjun
 * @version 2018-06-26
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class ShopWalletServiceImpl extends CrudServiceImpl<ShopWalletDao, ShopWallet> implements ShopWalletService {
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;

    @Override
    public ShopWallet findOne(Long id) {
        ShopWallet entity = new ShopWallet();
        entity.setId(id);

        return super.findOne(entity);
    }
    
    @Cacheable(value = PORTAL_CACHE_PREFIX + "shopWallet", key = "#root.target.PORTAL_CACHE_PREFIX + 'shopWallet:shopId:' + #shopId", unless = "#result == null")
    @Override
    public ShopWallet findOneByShopId(Long shopId) {
        ShopWallet entity = new ShopWallet();
        entity.setShopId(shopId);
        
        return super.findOne(entity);
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "shopWallet", key = "#root.target.PORTAL_CACHE_PREFIX + 'shopWallet:shopId:' + #entity.shopId")
    @Override
    public void modify(ShopWallet entity){
        super.modify(entity);
    }


}
