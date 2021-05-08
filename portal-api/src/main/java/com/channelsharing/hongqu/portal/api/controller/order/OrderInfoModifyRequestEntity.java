/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.order;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单信息Entity
 * @author liuhangjun
 * @version 2018-06-20
 */
@Data
public class OrderInfoModifyRequestEntity {

    @ApiModelProperty(value = "id", example = "1")
    @NotNull
	public Long id;
	
	@Length(min=1, max=64, message="订单编号长度必须介于 1 和 64 之间")
	public String sn;		// 订单编号

	@Length(min=0, max=128, message="备注长度必须介于 0 和 128 之间")
	public String remark;		// 备注



}
