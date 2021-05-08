/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.product;

import javax.annotation.Resource;

import com.channelsharing.hongqu.supplier.api.constant.Constant;
import com.channelsharing.hongqu.supplier.api.entity.GoodsInfo;
import com.channelsharing.hongqu.supplier.api.service.GoodsInfoService;
import com.channelsharing.pub.enums.ApproveStatus;
import lombok.Synchronized;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.channelsharing.hongqu.supplier.api.service.ProductInfoService;
import com.channelsharing.hongqu.supplier.api.entity.ProductInfo;

import java.util.List;


/**
 * 产品信息Controller
 * @author liuhangjun
 * @version 2018-06-07
 */
@Api(tags = "产品信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/productInfo")
public class ProductInfoController extends BaseController {

	@Resource
	private ProductInfoService productInfoService;
	
	@Autowired
	private GoodsInfoService goodsInfoService;
	

    /**@ApiOperation(value = "获取单条产品信息")
	@GetMapping("/{id}")
	public ProductInfo findOne(@PathVariable Long id){

	    ProductInfo entity = new ProductInfo();
	    entity.setId(id);
	    ProductInfo productInfo = productInfoService.findOne(entity);
	    if (productInfo== null)
			productInfo =  new ProductInfo();

		return productInfo;
	}*/
	
	
	@ApiOperation(value = "获取产品信息列表")
	@GetMapping
	public List<ProductInfo> findList(@RequestParam Long goodsId){
		ProductInfo entity = new ProductInfo();
		entity.setOffset(0);
		entity.setLimit(Constant.MAX_LIMIT);
		entity.setGoodsId(goodsId);
		
		return productInfoService.findPaging(entity).getRows();
	}
	

	@Synchronized
	@ApiOperation(value = "提交一条产品信息")
	@PostMapping
	public ProductInfo add(@RequestBody @Validated ProductInfoAddRequestEntity productInfoAddRequestEntity){
		ProductInfo entity = new ProductInfo();
		BeanUtils.copyProperties(productInfoAddRequestEntity, entity);
		
		// 提交产品前,先查询是否有对应的产品，生成不重复的产品sn
		List<ProductInfo> productInfoList = productInfoService.findList(productInfoAddRequestEntity.getGoodsId());
		if (productInfoList == null || productInfoList.size() == 0){
		 
			// 生成本商品的第一个产品序列号
			String sn = entity.getGoodsSn() + "-" + "1";
			entity.setSn(sn);
		
		}else {
			int maxNumber = 1;
			for (ProductInfo productInfo : productInfoList){
				String sn = productInfo.getSn();
				String number = StringUtils.split(sn, "-")[1];
				int num = Integer.parseInt(number);
				
				if (num >= maxNumber){
					maxNumber = num;
				}
			}
			
			entity.setSn(entity.getGoodsSn() + "-" + Integer.toString(maxNumber + 1));
		}
		
		
		ProductInfo productInfo = productInfoService.addWithResult(entity);
		return productInfo;
	}

	@ApiOperation(value = "修改一条产品信息")
	@PatchMapping
	public void modify(@RequestBody @Validated ProductInfoModifyRequestEntity productInfoModifyRequestEntity){
        ProductInfo entity = new ProductInfo();
		BeanUtils.copyProperties(productInfoModifyRequestEntity, entity);

		productInfoService.modify(entity);
		
		/*// 修改了产品信息把商品改为审核中
		GoodsInfo goodsInfo = new GoodsInfo();
		goodsInfo.setId(entity.getGoodsId());
		goodsInfo.setSupplierId(super.currentSupplierUser().getSupplierId());
		goodsInfo.setApproveStatus(ApproveStatus.approving.getCode());
		goodsInfoService.modify(goodsInfo);*/
	}

	@ApiOperation(value = "删除一条产品信息")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id){
	    ProductInfo entity = new ProductInfo();
	    entity.setId(id);
		productInfoService.delete(entity);
	}

}
