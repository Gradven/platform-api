/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.goods;

import javax.annotation.Resource;

import com.channelsharing.hongqu.supplier.api.entity.GoodsCategory;
import com.channelsharing.pub.constant.Constant;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.hongqu.supplier.api.controller.BaseController;
import com.channelsharing.hongqu.supplier.api.service.GoodsCategoryService;
import com.channelsharing.common.entity.Paging;


/**
 * 商品分类Controller
 *
 * @author liuhangjun
 * @version 2018-06-07
 */
@Api(tags = "商品分类操作接口")
@Validated
@RestController
@RequestMapping("/v1/goodsCategory")
public class GoodsCategoryController extends BaseController {
    
    @Resource
    private GoodsCategoryService goodsCategoryService;
    
    @ApiOperation("获取一级产品分类信息")
    @GetMapping("/firstGrade")
    public Paging<GoodsCategory> getFirstGradeCategory() {
        
        return this.getChildrenByParentId(0L);
        
    }
    
    @ApiOperation("根据parentId获取子分类信息")
    @GetMapping("/children")
    public Paging<GoodsCategory> getChildrenByParentId(@RequestParam Long parentId) {
        GoodsCategory goodsCategory = new GoodsCategory();
        goodsCategory.setParentId(parentId);
        goodsCategory.setLimit(Constant.MAX_LIMIT);
        
        return goodsCategoryService.findPaging(goodsCategory);
        
    }
    
    
}
