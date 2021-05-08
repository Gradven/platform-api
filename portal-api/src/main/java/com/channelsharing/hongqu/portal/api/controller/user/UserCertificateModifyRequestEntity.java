/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.user;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 用户认证信息Entity
 * @author liuhangjun
 * @version 2018-07-12
 */
@Data
public class UserCertificateModifyRequestEntity {

    @ApiModelProperty(value = "id", example = "1")
    @NotNull
	public Long id;
	
	
	@ApiModelProperty(value = "真实姓名", example = "张三")
	@Length(min=0, max=64, message="真实姓名长度必须介于 0 和 64 之间")
	@NotBlank(message = "姓名不能为空")
	public String realName;		// 真实姓名
	
	@ApiModelProperty(value = "身份证号", example = "3301281999903080021")
	@Length(min=0, max=18, message="身份证号长度必须介于 0 和 18 之间")
	@NotBlank(message = "身份证号不能为空")
	public String idCard;		// 身份证号
	
	@ApiModelProperty(value = "身份证正面", example = "http://img.xxx.com")
	@Length(min=0, max=255, message="身份证正面长度必须介于 0 和 255 之间")
	@NotBlank(message = "身份证正面不能为空")
	public String cardFront;		// 身份证正面
	
	@ApiModelProperty(value = "身份证背面", example = "http://img.xxx.com")
	@Length(min=0, max=255, message="身份证背面长度必须介于 0 和 255 之间")
	@NotBlank(message = "身份证背面不能为空")
	public String cardBack;		// 身份证背面



}
