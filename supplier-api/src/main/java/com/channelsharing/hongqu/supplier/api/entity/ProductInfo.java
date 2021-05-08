/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.channelsharing.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 产品信息Entity
 * @author liuhangjun
 * @version 2018-06-07
 */
@Data
public class ProductInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String sn;		// 产品编号
	private Long goodsId;		// 商品Id
	private String goodsSpecificationIds;		// 商品规格ids
	private String goodsSn;		// 商品序列号
	private String picUrl;      // 产品图片
	private Integer salesVolume; // 销量
	private Integer storeNumber;		// 库存
	private BigDecimal retailPrice;		// 零售价格
	private BigDecimal unitPrice;		// 价格
	private BigDecimal profit;		// 利润
	private BigDecimal percent;
	
	@JsonProperty   // just to explicitly tell jackson to serialize this
	private Integer delFlag;


	public ProductInfo() {
		super();
	}


}
