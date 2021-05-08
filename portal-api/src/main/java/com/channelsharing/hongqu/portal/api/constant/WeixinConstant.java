package com.channelsharing.hongqu.portal.api.constant;

import com.channelsharing.common.yaml.YamlProperties;

/**
 * Created by liuhangjun on 2018/3/26.
 */
public interface WeixinConstant {
    
    
    String WEIXIN_MINA_APP_ID = YamlProperties.getProperty("weixin.mina.appId");   // 暂时用上我的店的id
    String WEIXIN_MINA_SECRET = YamlProperties.getProperty("weixin.mina.secret");    // 暂时用上我的店的secret
    
    String WEIXIN_PAY_BODY = YamlProperties.getProperty("weixin.pay.body");
    String WEIXIN_MCH_ID = YamlProperties.getProperty("weixin.pay.mchId");   // 微信商户id-目前临时使用的id
    String WEIXIN_MCH_KEY = YamlProperties.getProperty("weixin.pay.mchKey");   // 微信商户key-目前临时使用的key


    // 以下为微信支付用到的字符串的key
    String RETURN_CODE = "return_code";
    String RETURN_MSG = "return_msg";

    String RESULT_CODE = "result_code";
    String RESULT_CODE_FAIL = "FAIL";

    String SUCCESS_STR = "SUCCESS";
    String FAIL_STR = "FAIL";

    String PAY_TIME_END = "time_end";
    String OUT_TRADE_NO = "out_trade_no";

    String TRANSACTION_ID = "transaction_id";
    String TOTAL_FEE = "total_fee";

}
