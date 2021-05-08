package com.channelsharing.hongqu.supplier.api.controller.supplier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by liuhangjun on 2018/1/24.
 */
@Data
public class SupplierUserLoginRequestEntity {

    @ApiModelProperty(value = "用户名", example="test123")
    @Length(min = 6, max = 50, message = "用户账户长度必须介于 6 和 50 之间")
    public String account;

    @ApiModelProperty(value = "密码", example="MTIzNDU2")
    @Length(min = 6, max = 50, message = "登录密码长度必须介于 6 和 50 之间")
    public String password;

}
