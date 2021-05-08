package com.channelsharing.hongqu.portal.api.enums;


import com.channelsharing.common.enums.BaseEnum;

/**
 * Created by liuhangjun on 2017/6/19.
 */
public enum AccountType implements BaseEnum {

     weixinMina(1, "微信小程序"), mobile(2, "手机号码注册"), weixin(3, "微信"), weibo(4, "微博"), qq(5, "qq"), email(6, "邮箱");

    private Integer code;
    private String name;

    AccountType(Integer code, String name){
        this.code = code;
        this.name = name;

    }


    public static AccountType valueOf(int value) {    // 手写的从int到enum的转换函数
        switch (value) {

            case 1:
                return weixinMina;
            case 2:
                return mobile;
            case 3:
                return weixin;
            case 4:
                return weibo;
            case 5:
                return qq;
            default:
                return null;
        }
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }
}
