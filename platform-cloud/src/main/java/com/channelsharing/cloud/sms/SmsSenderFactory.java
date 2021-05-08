package com.channelsharing.cloud.sms;

import com.alibaba.druid.util.StringUtils;

/**
 * 短信发送能力工厂类
 * Created by liuhangjun on 2017/11/28.
 */
public class SmsSenderFactory {

    private static final String ALIYUN_SMS = "ALICOM_SMS";
    private static final String CHUANGLAN_SMS = "CHUANGLAN_SMS";

    // 设定由那个服务商提供服务
    private static final String SMS_SERVER = ALIYUN_SMS;


    private static ChuanglanSms chuanglanSms = new ChuanglanSms();

    private static AliyunSms aliyunSms = new AliyunSms();

    private SmsSenderFactory(){

    }

    public static synchronized ISmsSender getSmsSender(){

        if (StringUtils.equals(SMS_SERVER, ALIYUN_SMS)) {
            return aliyunSms;
        }else if (StringUtils.equals(SMS_SERVER, CHUANGLAN_SMS)){
            return chuanglanSms;
        }else{
            return aliyunSms;
        }

    }
}
