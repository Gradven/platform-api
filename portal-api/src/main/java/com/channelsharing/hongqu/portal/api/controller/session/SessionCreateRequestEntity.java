package com.channelsharing.hongqu.portal.api.controller.session;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SessionCreateRequestEntity {

    @ApiModelProperty(value = "登录账号", example="13512345678")
    @Length(min = 6, max = 50, message = "用户账户长度必须介于 6 和 50 之间")
    public String account;  //(此字段包含手机号码和email，后台根据规则自动判断是邮箱还是手机号码)


    @ApiModelProperty(value = "登录密码", example="MTIzNDU2")
    @Length(min = 6, max = 30, message = "登录密码长度必须介于 6 和 30 之间")
    public String password;

}

