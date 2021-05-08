/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.supplier;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 供应商用户Entity
 *
 * @author liuhangjun
 * @version 2018-02-02
 */
@Data
public class SupplierUserAddRequestEntity {

    @Length(min = 0, max = 32, message = "供应商用户姓名长度必须介于 0 和 32 之间")
    public String name;        // 供应商用户姓名

    @Length(min = 1, max = 512, message = "登录密码长度必须介于 1 和 512 之间")
    public String password;        // 登录密码

    public Integer age;        // 年龄

    @Length(min = 1, max = 50, message = "登录名长度必须介于 1 和 50 之间")
    public String account;        // 登录名

    @Length(min = 0, max = 20, message = "手机号码长度必须介于 0 和 20 之间")
    public String mobile;        // 手机号码

    @Length(min = 0, max = 128, message = "邮箱长度必须介于 0 和 128 之间")
    public String email;        // 邮箱

    public Integer status;        // 用户状态

    @NotNull(message = "所属供应商不能为空")
    public Integer supplierId;        // 所属供应商
    

    @Length(min = 0, max = 64, message = "备注号长度必须介于 0 和 64 之间")
    public String remark;        // 备注


}
