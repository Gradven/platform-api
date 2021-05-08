package com.channelsharing.hongqu.portal.api.controller.user;

import org.hibernate.validator.constraints.Length;

/**
 * Created by liuhangjun on 2017/6/20.
 */
public class UserActivateRequestEntity {

    private String account;
    private String activationCode;

    @Length(min=6, max=50, message="用户账户长度必须介于 6 和 50 之间")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Length(min=6, max=6, message="激活码必须为6位数字")
    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
}
