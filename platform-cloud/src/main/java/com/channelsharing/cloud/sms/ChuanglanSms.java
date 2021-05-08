package com.channelsharing.cloud.sms;

import com.channelsharing.common.utils.HttpClientUtil;
import com.channelsharing.common.yaml.YamlProperties;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 用创蓝公司的接口，发送短信验证码
 * Created by liuhangjun on 2017/11/28.
 */
public class ChuanglanSms implements ISmsSender {

    private static final String MSG_ACCOUNT = YamlProperties.getProperty("chuanglanSms.account");
    private static final String MSG_PASSWORD = YamlProperties.getProperty("chuanglanSms.password");
    private static final String MSG_URL = "http://sapi.253.com/msg/HttpBatchSendSM";

    @Override
    public void sendSms(String mobile, String content) {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("account", MSG_ACCOUNT));
        params.add(new BasicNameValuePair("pswd", MSG_PASSWORD));
        params.add(new BasicNameValuePair("mobile", mobile));
        params.add(new BasicNameValuePair("needstatus", "false"));
        params.add(new BasicNameValuePair("msg", content));

        HttpUriRequest request = RequestBuilder.get(MSG_URL).addParameters(params.toArray(new NameValuePair[] {}))
                .build();

        HttpClientUtil.sendRequestOneway(request);

    }
}
