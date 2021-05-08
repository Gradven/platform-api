/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.order;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 店铺技术服务费订单Entity
 * @author liuhangjun
 * @version 2018-06-18
 */
@Data
public class OrderShopServeAddRequestEntity {
	

	@ApiModelProperty(value = "店铺id", example = "1")
	@NotNull(message="店铺id不能为空")
	public Long shopId;		// 店铺id
	
	



}
