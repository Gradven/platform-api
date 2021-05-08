/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service.impl;

import com.channelsharing.hongqu.supplier.api.entity.GoodsCategory;
import com.google.common.collect.Lists;
import com.channelsharing.common.entity.Paging;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.supplier.api.service.GoodsCategoryService;
import com.channelsharing.hongqu.supplier.api.dao.GoodsCategoryDao;

import java.util.List;

/**
 * 商品分类Service
 *
 * @author liuhangjun
 * @version 2018-06-07
 */
@Service
public class GoodsCategoryServiceImpl extends CrudServiceImpl<GoodsCategoryDao, GoodsCategory> implements GoodsCategoryService {
    
    public GoodsCategory findOne(Long id) {
        GoodsCategory entity = new GoodsCategory();
        entity.setId(id);
        
        return super.findOne(entity);
    }
    
    /**
     * 给每个节点加上是否是最后节点的判断
     * @param entity
     * @return
     */
    @Override
    public Paging<GoodsCategory> findPaging(GoodsCategory entity) {
        
        Paging<GoodsCategory> categoryPaging = super.findPaging(entity);
        
        List<GoodsCategory> list = Lists.newArrayList();
        for (GoodsCategory goodsCategory : categoryPaging.getRows()) {
            GoodsCategory goodsCategoryQuery = new GoodsCategory();
            goodsCategoryQuery.setParentId(goodsCategory.getId());
            GoodsCategory goodsCategoryResult = super.findOne(goodsCategoryQuery);
            
            if (goodsCategoryResult != null) {
                goodsCategory.setLastFlag(false);
            } else {
                goodsCategory.setLastFlag(true);
            }
            
            list.add(goodsCategory);
            
        }
    
        categoryPaging.setRows(list);
        
        return categoryPaging;
        
    }
    
}
