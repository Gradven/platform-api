package com.channelsharing.hongqu.portal.api.controller.user;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 * Created by liuhangjun on 2017/6/22.
 */

public class PasswordResetRequestEntity {
    
    @ApiModelProperty(value = "新密码", example = "MTIzNDU2")
    @Length(min = 6, max = 20, message = "用户新密码长度必须介于 6 和 20 之间")
    public String password;



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
