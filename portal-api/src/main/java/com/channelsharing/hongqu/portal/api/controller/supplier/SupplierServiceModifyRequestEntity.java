/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.supplier;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 供应商售后服务Entity
 * @author liuhangjun
 * @version 2018-08-08
 */
@Data
public class SupplierServiceModifyRequestEntity {

    @ApiModelProperty(value = "id", example = "1")
    @NotNull
	public Long id;
	
	
	@ApiModelProperty(value = "订单商品id", example = "")
	@NotNull(message="订单商品id不能为空")
	public Long orderGoodsId;		// 订单商品id
	
	



}
