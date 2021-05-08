package com.channelsharing.hongqu.supplier.api.controller.supplier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by liuhangjun on 2017/6/22.
 */
@Data
public class SupplierUserPwdModifyRequestEntity {

    @ApiModelProperty(value = "老密码", example="MTIzNDU2")
    @Length(min = 6, max = 20, message = "用户旧密码长度必须介于 6 和 20 之间")
    public String oldPassword;

    @ApiModelProperty(value = "新密码", example="MTIzNDU2")
    @Length(min = 6, max = 20, message = "用户新密码长度必须介于 6 和 20 之间")
    public String newPassword;

}
