/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.goods;

import javax.annotation.Resource;

import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.common.exception.BadRequestException;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import com.channelsharing.hongqu.supplier.api.service.GoodsInfoService;
import com.channelsharing.pub.enums.ApproveStatus;
import com.channelsharing.hongqu.supplier.api.enums.GoodsOnSaleFlag;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.hongqu.supplier.api.controller.BaseController;
import com.channelsharing.hongqu.supplier.api.entity.GoodsInfo;
import com.channelsharing.common.entity.Paging;


/**
 * 商品信息接口Controller
 * @author liuhangjun
 * @version 2018-06-06
 */
@Api(tags = "商品信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/goodsInfo")
public class GoodsInfoController extends BaseController {

	@Resource
	private GoodsInfoService goodsInfoService;

    @ApiOperation(value = "获取单条商品信息")
	@GetMapping("/{id}")
	public GoodsInfo findOne(@PathVariable Long id){

	    GoodsInfo entity = new GoodsInfo();
	    entity.setId(id);
		entity.setSupplierId(super.currentSupplierUser().getSupplierId());
	    GoodsInfo goodsInfo = goodsInfoService.findOne(entity);
	    if (goodsInfo== null)
			goodsInfo =  new GoodsInfo();

		return goodsInfo;
	}

	@ApiOperation(value = "获取商品信息列表-简单搜索接口")
	@GetMapping
	public Paging<GoodsInfo> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String sn){

		GoodsInfo entity = new GoodsInfo();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setSupplierId(super.currentSupplierUser().getSupplierId());
		entity.setName(name);
		entity.setSn(sn);

		return goodsInfoService.findPaging(entity);

	}

	@ApiOperation(value = "提交一条商品信息")
	@PostMapping
	public GoodsInfo add(@RequestBody @Validated GoodsInfoAddRequestEntity goodsInfoAddRequestEntity){
		GoodsInfo entity = new GoodsInfo();
		BeanUtils.copyProperties(goodsInfoAddRequestEntity, entity);
		entity.setSupplierId(super.currentSupplierUser().getSupplierId());
		entity.setCreateSuId(super.currentSupplierUserId());
		entity.setApproveStatus(ApproveStatus.approving.getCode()); // 默认审核中
		
		// 如果前端没传商品的状态，那么默认为下架状态
		if (null == goodsInfoAddRequestEntity.getOnSaleFlag())
			entity.setOnSaleFlag(GoodsOnSaleFlag.notSale.getCode());
		
		GoodsInfo result = goodsInfoService.addWithResult(entity);
		
		return result;
	}
	
	/**
	 * 修改商品信息需要运营审核
	 * @param goodsInfoModifyRequestEntity
	 */
	@ApiOperation(value = "修改一条商品信息")
	@PatchMapping
	public void modify(@RequestBody @Validated GoodsInfoModifyRequestEntity goodsInfoModifyRequestEntity){
        GoodsInfo entity = new GoodsInfo();
		BeanUtils.copyProperties(goodsInfoModifyRequestEntity, entity);
		entity.setApproveStatus(ApproveStatus.approving.getCode());  // 修改商品信息把商品设置为审核中的逻辑
		entity.setSupplierId(super.currentSupplierUser().getSupplierId());
		
		goodsInfoService.modify(entity);
	}
	
	/**
	 * 上下架的操作不需要运营审核
	 * @param flag
	 */
	@ApiOperation(value = "上下架操作")
	@PatchMapping("/onSaleFlag/{flag}")
	public void modifyOnSaleFlag(@PathVariable Integer flag, @RequestParam Long goodsId){
		
		if (BooleanEnum.no.getCode().equals(flag) || BooleanEnum.yes.getCode().equals(flag)){
			GoodsInfo entity = new GoodsInfo();
			entity.setId(goodsId);
			entity.setOnSaleFlag(flag);
			entity.setSupplierId(super.currentSupplierUser().getSupplierId());
			
			goodsInfoService.modify(entity);
		}else {
			throw new BadRequestException("上下架的flag值只能为0或者1");
		}
		
		
	
	}

	@ApiOperation(value = "删除一条商品信息")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id){
	    GoodsInfo entity = new GoodsInfo();
	    entity.setId(id);
		entity.setSupplierId(super.currentSupplierUser().getSupplierId());
		
		GoodsInfo goodsInfoStatus = goodsInfoService.findOne(id);
		
		// 不是下架状态不能删除商品
		if (goodsInfoStatus !=null && !goodsInfoStatus.getOnSaleFlag().equals(BooleanEnum.no.getCode())){
		    throw new SystemInnerBusinessException("删除商品前，请下架商品");
		}
		
		goodsInfoService.delete(entity);
	}

}
