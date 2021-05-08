/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.order;

import javax.validation.constraints.NotNull;

import com.channelsharing.hongqu.portal.api.controller.invoice.InvoiceInfoAddRequestEntity;
import org.hibernate.validator.constraints.Length;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单的商品信息Entity
 * @author liuhangjun
 * @version 2018-06-20
 */
@Data
public class OrderGoodsAddRequestEntity {
	
	
	@ApiModelProperty(value = "店铺Id", example = "1")
	@NotNull(message="店铺Id不能为空")
	public Long shopId;		// 店铺Id

	@ApiModelProperty(value = "产品I", example = "1")
	@NotNull(message="产品Id不能为空")
	public Long productId;		// 产品Id
	
	@ApiModelProperty(value = "产品规格名称", example = "烟白灰;玛瑙红;浅杏粉;1.8m床垫*1+枕头*2;1.5m床垫*1+枕头*2")
	@Length(min=1, max=64, message="产品规格名称长度必须介于 1 和 4000 之间")
	@NotNull(message="产品规格名称")
	public String goodsSpecificationNameValue;		// 产品规格名称

	@ApiModelProperty(value = "商品数量", example = "2")
	@NotNull(message="购买数量不能为空")
	public Integer goodsNumber;		// 商品数量
	
	// 发票信息
	public InvoiceInfoAddRequestEntity invoiceInfoAddRequestEntity;

}
