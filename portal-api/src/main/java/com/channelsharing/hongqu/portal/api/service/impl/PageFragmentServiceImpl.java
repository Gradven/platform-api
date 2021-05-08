/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.entity.GoodsCategory;
import com.channelsharing.hongqu.portal.api.entity.GoodsInfo;
import com.channelsharing.hongqu.portal.api.entity.ShopInfo;
import com.channelsharing.hongqu.portal.api.enums.FragmentType;
import com.channelsharing.hongqu.portal.api.service.GoodsCategoryService;
import com.channelsharing.hongqu.portal.api.service.GoodsInfoService;
import com.channelsharing.hongqu.portal.api.service.ShopInfoService;
import com.channelsharing.pub.enums.ApproveStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.service.PageFragmentService;
import com.channelsharing.hongqu.portal.api.entity.PageFragment;
import com.channelsharing.hongqu.portal.api.dao.PageFragmentDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 页面区块信息Service
 * @author liuhangjun
 * @version 2018-07-26
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class PageFragmentServiceImpl extends CrudServiceImpl<PageFragmentDao, PageFragment> implements PageFragmentService {
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;
    

    @Override
    public PageFragment findOne(Long id) {
        PageFragment entity = new PageFragment();
        entity.setId(id);

        return super.findOne(entity);
    }
    
    @Cacheable(value = PORTAL_CACHE_PREFIX
            + "pageFragment", key = "#root.target.PORTAL_CACHE_PREFIX + 'pageFragment:keyword:' + #entity.keyword", unless = "#result == null")
    @Override
    public PageFragment findOne(PageFragment entity) {
        PageFragment pageFragment = super.findOne(entity);
        
        if (pageFragment == null) {
            return null;
        }
        
        
        return pageFragment;
    }


}
