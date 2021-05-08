/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.entity.SpecificationInfo;
import com.channelsharing.hongqu.portal.api.service.GoodsSpecificationService;
import com.channelsharing.hongqu.portal.api.service.SpecificationInfoService;
import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.entity.GoodsSpecification;
import com.channelsharing.hongqu.portal.api.dao.GoodsSpecificationDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品规格值Service
 * @author liuhangjun
 * @version 2018-06-20
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class GoodsSpecificationServiceImpl extends CrudServiceImpl<GoodsSpecificationDao, GoodsSpecification> implements GoodsSpecificationService {
    
    @Autowired
    private SpecificationInfoService specificationInfoService;
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;
    
    /**
     * 将产品的规格进行重新组合一个新的格式（主要是规格id去重）
     * @param goodsId
     * @return
     */
    @Cacheable(value = PORTAL_CACHE_PREFIX + "goodsSpecification", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsSpecification:goodsId:' + #goodsId", unless = "#result == null")
    @Override
    public Map<String, List<GoodsSpecification>> combineGoodsSpecification(Long goodsId){
        
        Map<String, List<GoodsSpecification>> map = new HashMap<>();
        
        GoodsSpecification query = new GoodsSpecification();
        query.setGoodsId(goodsId);
        List<GoodsSpecification> GoodsSpecificationList = super.findPaging(query).getRows();
        
        for (GoodsSpecification goodsSpecification : GoodsSpecificationList){
            
            long specificationId = goodsSpecification.getSpecificationId();
            SpecificationInfo specificationInfo = new SpecificationInfo();
            specificationInfo.setId(specificationId);
            String specificationName = specificationInfoService.findOne(specificationInfo).getName();
            
            String key = Long.toString(specificationId) + "|" + specificationName;
            
            List<GoodsSpecification> goodsSpecificationList =  new ArrayList<>();
            if (map.containsKey(key)){
                goodsSpecificationList = map.get(key);
                goodsSpecificationList.add(goodsSpecification);
                
            }else {
                goodsSpecificationList.add(goodsSpecification);
            }
            
            map.put(key, goodsSpecificationList);
            
        }
        
        
        return map;
    }
    
    @Override
    public GoodsSpecification findOne(Long id){
        GoodsSpecification entity = new GoodsSpecification();
        entity.setId(id);
        
        return super.findOne(entity);
    }

}
