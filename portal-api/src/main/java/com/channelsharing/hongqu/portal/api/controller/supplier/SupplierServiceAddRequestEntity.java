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

/**
 * 供应商售后服务Entity
 * @author liuhangjun
 * @version 2018-08-08
 */
@Data
public class SupplierServiceAddRequestEntity {


	@ApiModelProperty(value = "订单商品id", example = "2")
	@NotNull(message="订单商品id不能为空")
	public Long orderGoodsId;		// 订单商品id

	@ApiModelProperty(value = "供应商id", example = "1")
	public Long supplierId;		// 供应商id
	

	@ApiModelProperty(value = "退货理由，1：发错货，2:尺码偏大，3:尺码偏小，4:七天无理由，5:商品不适合，6:不喜欢，99:其他", example = "1")
	public Integer reasonType;		// 退货理由，1：发错货，2:尺码偏大，3:尺码偏小，4:七天无理由，5:商品不适合，6:不喜欢，99:其他

	@ApiModelProperty(value = "其他理由", example = "我就想退货")
	@Length(min=0, max=255, message="其他理由长度必须介于 0 和 255 之间")
	public String reason;		// 其他理由
	

	@ApiModelProperty(value = "退换货地址", example = "浙江杭州")
	@Length(min=0, max=255, message="寄回地址长度必须介于 0 和 255 之间")
	public String serviceAddress;		// 寄回地址
	
	@ApiModelProperty(value = "店铺id", example = "店铺id")
	@NotNull(message = "店铺id不能为空")
	public Long shopId;		// 店铺id
	
	
	
	
}
