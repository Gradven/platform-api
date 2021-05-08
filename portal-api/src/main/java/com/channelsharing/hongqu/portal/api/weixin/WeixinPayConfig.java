package com.channelsharing.hongqu.portal.api.weixin;

import com.channelsharing.hongqu.portal.api.constant.WeixinConstant;
import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayDomainSimpleImpl;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by liuhangjun on 2018/3/26.
 */
public class WeixinPayConfig extends WXPayConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    private byte[] certData;

    public WeixinPayConfig() throws Exception {


        Resource resource = new ClassPathResource("weixin_cert/apiclient_cert.p12");
        String fileName = resource.getFilename();

        logger.debug("weixin apiclient_cert.p12 name is :" + fileName);

        if (resource.isReadable()) {
            //每次都会打开一个新的流
            InputStream certStream = resource.getInputStream();

            this.certData = IOUtils.toByteArray(certStream);
            certStream.read(this.certData);
            certStream.close();
        }

    }

    @Override
    public String getAppID() {
        return WeixinConstant.WEIXIN_MINA_APP_ID;
    }

    @Override
    public String getMchID() {
        return WeixinConstant.WEIXIN_MCH_ID;
    }

    @Override
    public String getKey() {
        return WeixinConstant.WEIXIN_MCH_KEY;
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        return  WXPayDomainSimpleImpl.instance();
    }
}
