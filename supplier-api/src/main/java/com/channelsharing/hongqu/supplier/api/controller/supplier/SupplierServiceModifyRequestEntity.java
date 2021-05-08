/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.supplier;

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
	
	@ApiModelProperty(value = "退货状态", example = "2")
	public Integer status;		// 退货状态，1:申请退货，2:审核通过，3:审核不通过，99:取消退换货
	
	@ApiModelProperty(value = "答复", example = "可以的")
	@Length(min=0, max=128, message="答复长度必须介于 0 和 128 之间")
	public String reply;		// 答复
	


}
