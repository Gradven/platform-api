package com.channelsharing.hongqu.portal.api.controller.user;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 * Created by liuhangjun on 2017/6/20.
 */
public class UserInitRequestEntity {

    private String nickname;
    private String password;
    
    @ApiModelProperty(value = "昵称", example = "用户昵称用户昵称用户昵称")
    @Length(min=1, max=30, message="用户昵称长度必须介于 1 和 30 之间")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    @ApiModelProperty(value = "用户密码", example = "MTIzNDU2")
    @Length(min=6, max=20, message="用户密码长度必须介于 6 和 20 之间")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
