/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.page;

import javax.annotation.Resource;

import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.common.exception.BadRequestException;
import com.channelsharing.common.exception.DataNotFoundException;
import com.channelsharing.hongqu.portal.api.entity.*;
import com.channelsharing.hongqu.portal.api.enums.AgentFlag;
import com.channelsharing.hongqu.portal.api.enums.FragmentType;
import com.channelsharing.hongqu.portal.api.enums.RoleType;
import com.channelsharing.hongqu.portal.api.enums.ShopGoodsStatus;
import com.channelsharing.hongqu.portal.api.service.*;
import com.channelsharing.pub.enums.ApproveStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.common.entity.Paging;

import java.util.ArrayList;
import java.util.List;


/**
 * 页面区块信息Controller
 * @author liuhangjun
 * @version 2018-07-26
 */
@Api(tags = "页面区块信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/pageFragment")
public class PageFragmentController extends BaseController {

	@Resource
	private PageFragmentService pageFragmentService;
	
	@Autowired
	private ShopGoodsService shopGoodsService;
	
	@Autowired
	private GoodsCategoryService goodsCategoryService;
	
	@Autowired
	private GoodsInfoService goodsInfoService;
	
	@Autowired
	private ShopInfoService shopInfoService;

    @ApiOperation(value = "获取单条页面区块信息")
	@GetMapping("/{keyword}")
	public PageFragment findOne(@PathVariable String keyword,
								@ApiParam(value = "用户角色类型：1：用户，2：店主", example = "2")
								@RequestParam Integer roleType){
		PageFragment entity = new PageFragment();
		entity.setKeyword(keyword);
		
	    PageFragment pageFragment = pageFragmentService.findOne(entity);
	    
	    if (pageFragment == null){
			return null;
		}
		

		this.putDataInPageFragment(pageFragment);
		
	 
		// 如果是商品类型的数据，并且是店主身份来查询，需要告知代理状态
	    if (pageFragment.getType().equals(FragmentType.goods.getCode()) && roleType.equals(RoleType.shopkeeper.getCode())){
			
	    	Long shopId = super.currentUser().getShopId();
			// 如果会话中没有shopid，那么从数据库中再查一次
			if (shopId == null){
				ShopInfo query = new ShopInfo();
				query.setUserId(super.currentUserId());
				ShopInfo shopInfoResult = shopInfoService.findOne(query);
				if (shopInfoResult != null){
					shopId = shopInfoResult.getId();
				}else {
					throw new DataNotFoundException("没有对应的店铺信息");
				}
			}
			
	    	List<Object> goodsInfoList = pageFragment.getObjectList();
			List<Object> goodsInfoListNew = new ArrayList<>();
	    	for (Object object : goodsInfoList){
	    		GoodsInfo goodsInfo = (GoodsInfo) object;
				ShopGoods shopGoods = shopGoodsService.findOne(shopId, goodsInfo.getId());
				
				if(shopGoods != null && shopGoods.getShopId() != null){
					if (shopGoods.getStatus().equals(ShopGoodsStatus.add.getCode())){
						goodsInfo.setAgentFlag(AgentFlag.agent.getCode());
					}else if (shopGoods.getStatus().equals(ShopGoodsStatus.move.getCode())){
						goodsInfo.setAgentFlag(AgentFlag.onceAgent.getCode());
					}
				}else {
					goodsInfo.setAgentFlag(AgentFlag.unAgent.getCode());
				}
			
				goodsInfoListNew.add(goodsInfo);
			}
	  
		}

		return pageFragment;
	}
	
	
	/**
	 * 将对应的类型的数据放入到PageFragment中
	 * @param pageFragment
	 */
	private void putDataInPageFragment(PageFragment pageFragment){
		List<Object> list = new ArrayList<>();
		String[] idArray = StringUtils.split(pageFragment.getValue(), "|");
		
		// 放入商品数据
		if (pageFragment.getType().equals(FragmentType.goods.getCode())){
			
			for(String idStr : idArray){
				Long id = Long.parseLong(idStr);
				GoodsInfo goodsInfo = goodsInfoService.findOne(id);
				
				// 商品信息不为空，且审核通过,未删除的商品才能显示
				if (goodsInfo != null ){
					if (goodsInfo.getApproveStatus() != null
							&& goodsInfo.getApproveStatus().equals(ApproveStatus.approved.getCode())
							&& goodsInfo.getDelFlag() != null
							&& goodsInfo.getDelFlag().equals(BooleanEnum.no.getCode())){
						list.add(goodsInfo);
					}
				}
				
			}
			
		}else if (pageFragment.getType().equals(FragmentType.goodsCategory.getCode())){ // 商品分类
			
			for(String idStr : idArray){
				Long id = Long.parseLong(idStr);
				GoodsCategory goodsCategory = goodsCategoryService.findOne(id);
				
				if (goodsCategory !=null && goodsCategory.getId() != null){
					list.add(goodsCategory);
				}
				
			}
			
			
		}else if (pageFragment.getType().equals(FragmentType.shop.getCode())){ // 店铺
			
			for(String idStr : idArray){
				Long id = Long.parseLong(idStr);
				ShopInfo shopInfo = shopInfoService.findOne(id);
				
				if (shopInfo != null && shopInfo.getId() != null){
					list.add(shopInfo);
				}
				
			}
		}
		
		pageFragment.setObjectList(list);
	}

}
