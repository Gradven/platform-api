/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.order;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 订单的商品信息Entity
 * @author liuhangjun
 * @version 2018-07-01
 */
@Data
public class OrderGoodsModifyRequestEntity {

    @ApiModelProperty(value = "id", example = "1")
    @NotNull
	public Long id;
	
	@ApiModelProperty(value = "快递公司id", example = "1")
	@NotNull(message = "快递公司id不能为空")
	public Integer shippingCode;		// 快递公司id
	
	@ApiModelProperty(value = "快递公司名", example = "顺丰")
	@Length(min=1, max=12, message="快递公司名长度必须介于 1 和 12 之间")
	@NotNull(message = "快递公司名不能为空")
	public String shippingName;		// 快递公司名
	
	@ApiModelProperty(value = "运费", example = "12.00")
	public BigDecimal shippingFee;		// 运费
	
	@ApiModelProperty(value = "运单号", example = "32432023423423")
	@NotNull(message = "运单号不能为空")
	@Length(min=1, max=64, message="运单号长度必须介于 1 和 64 之间")
	public String shippingNo;		// 运单号


}
