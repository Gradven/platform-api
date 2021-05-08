package com.channelsharing.hongqu.portal.api.controller.session;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class FrontThirdSessionRequestEntity {

	@ApiModelProperty(value = "账号类型", example=" 1:微信小程序, 2: 手机号码, 3: 微信, 4:微博, 5:qq, 6:邮箱")
	@NotNull
	private Integer accountType;

	private String code;

	@ApiModelProperty(value = "微信小程序校验专用, map的key为：encryptedData，iv，signature，rawData")
	public Map<String, String> minaVerifyMap;


}
