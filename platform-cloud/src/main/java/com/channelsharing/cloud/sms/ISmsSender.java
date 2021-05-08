package com.channelsharing.cloud.sms;

/**
 * Created by liuhangjun on 2017/11/28.
 */
public interface ISmsSender {

    void sendSms(String mobile, String content);
}
