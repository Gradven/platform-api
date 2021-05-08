/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.entity.Paging;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.service.RecommendSentenceService;
import com.channelsharing.hongqu.portal.api.entity.RecommendSentence;
import com.channelsharing.hongqu.portal.api.dao.RecommendSentenceDao;

import java.util.List;

/**
 * 商品运营推荐语Service
 * @author liuhangjun
 * @version 2018-07-23
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class RecommendSentenceServiceImpl extends CrudServiceImpl<RecommendSentenceDao, RecommendSentence> implements RecommendSentenceService {
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;

    @Override
    public RecommendSentence findOne(Long id) {
        RecommendSentence entity = new RecommendSentence();
        entity.setId(id);

        return super.findOne(entity);
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "recommendSentenceList", key = "#root.target.PORTAL_CACHE_PREFIX + 'recommendSentenceList:list'")
    @Override
    public List<RecommendSentence> findList(){
        RecommendSentence entity = new RecommendSentence();
        entity.setLimit(Constant.MAX_LIMIT);
        return super.dao.findList(entity);
    }


}
