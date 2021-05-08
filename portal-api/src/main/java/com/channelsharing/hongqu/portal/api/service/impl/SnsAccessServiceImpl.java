package com.channelsharing.hongqu.portal.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.channelsharing.hongqu.portal.api.constant.WeixinConstant;
import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.hongqu.portal.api.enums.AccountType;
import com.channelsharing.hongqu.portal.api.service.SnsAccessService;
import com.channelsharing.common.exception.ArgumentNotValidException;
import com.channelsharing.common.exception.BadRequestException;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import com.channelsharing.hongqu.portal.api.enums.UserSex;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by liuhangjun on 2017/7/25.
 */
@Service
@Slf4j
public class SnsAccessServiceImpl implements SnsAccessService {
    

    private static final String WEIXIN_APP_ID = "";
    private static final String WEIXIN_SECRET = "";

    private static final String WEIBO_CLIENT_ID = "";
    private static final String WEIBO_CLIENT_SECRET = "";

    private static final String QQ_CLIENT_ID = "";
    private static final String QQ_CLIENT_SECRET = "";




    @Override
    public UserInfo getUserInfoFromThirdParty(AccountType accountType, String code, Map<String, String> miniVerifyMap)
            throws IOException {
        if (StringUtils.isBlank(code)) {
            throw new ArgumentNotValidException("参数错误：code为空");
        }

        switch (accountType) {
            case weixin:
                return this.getUserInfoFromWeChat(code);
            case weibo:
                return this.getUserInfoFromWeibo(code);
            case qq:
                return this.getUserInfoFromQQ(code);
            case weixinMina:
                return this.getUserInfoFromWeixinMina(code, miniVerifyMap);
            default:
                throw new ArgumentNotValidException("参数错误：无对应的accountType值");
        }
    }

    private UserInfo getUserInfoFromWeChat(String code) throws IOException {
        HttpUriRequest request = RequestBuilder.get("https://api.weixin.qq.com/sns/oauth2/access_token")
                .addParameter("appid", WEIXIN_APP_ID).addParameter("secret", WEIXIN_SECRET)
                .addParameter("grant_type", "authorization_code").addParameter("code", code).build();

        JSONObject jsonObject = this.doRequestAndCheckResponse(request, "errcode", "errmsg", "获取微信access_token失败");

        String accessToken = jsonObject.getString("access_token");
        String openid = jsonObject.getString("openid");

        request = RequestBuilder.get("https://api.weixin.qq.com/sns/userinfo").addParameter("access_token", accessToken)
                .addParameter("openid", openid).build();

        jsonObject = this.doRequestAndCheckResponse(request, "errcode", "errmsg", "获取微信userinfo失败");

        log.debug("Third weixin user information json is : {}", jsonObject.toJSONString());

        UserInfo userInfo = new UserInfo();
        String avatar = jsonObject.getString("headimgurl");
        userInfo.setAvatar(StringUtils.equals("/0", avatar) ? null : avatar);
        userInfo.setNickname(jsonObject.getString("nickname"));
        userInfo.setSex(jsonObject.getIntValue("sex"));
        userInfo.setCountry(jsonObject.getString("country"));
        userInfo.setProvince(jsonObject.getString("province"));
        userInfo.setCity(jsonObject.getString("city"));
        userInfo.setThirdPartyUserId(jsonObject.getString("openid"));
        userInfo.setAccountType(AccountType.weixin.getCode());

        return userInfo;
    }

    private UserInfo getUserInfoFromWeibo(String code) throws IOException {
        HttpUriRequest request = RequestBuilder.get("https://api.weibo.com/oauth2/access_token")
                .addParameter("client_id", WEIBO_CLIENT_ID)
                .addParameter("client_secret", WEIBO_CLIENT_SECRET)
                .addParameter("grant_type", "authorization_code")
                .addParameter("redirect_uri",
                        java.net.URLEncoder.encode("http://www.hfans.com.cn/getWeiboQrConnect.html", "UTF-8"))
                .addParameter("code", code).build();

        JSONObject jsonObject = this.doRequestAndCheckResponse(request, "error_code", "error", "获取微博access_token失败");

        String accessToken = jsonObject.getString("access_token");
        String uid = jsonObject.getString("uid");

        request = RequestBuilder.get("https://api.weibo.com/2/users/show.json")
                .addParameter("access_token", accessToken).addParameter("uid", uid).build();

        jsonObject = this.doRequestAndCheckResponse(request, "error_code", "error", "获取微博userinfo失败");

        log.debug("Third Weibo user information json is : {}", jsonObject.toJSONString());

        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar(jsonObject.getString("profile_image_url"));
        userInfo.setNickname(jsonObject.getString("screen_name"));
        String gender = jsonObject.getString("gender");
        userInfo.setSex("m".equals(gender) ? UserSex.male.getCode() : ("f".equals(gender) ? UserSex.female.getCode() : UserSex.unknow.getCode()));
        userInfo.setThirdPartyUserId(jsonObject.getString("idstr"));
        userInfo.setAccountType(AccountType.weibo.getCode());

        return userInfo;
    }

    private UserInfo getUserInfoFromQQ(String code) throws IOException {
        String accessToken = null, openid = null;

        try (CloseableHttpResponse response = HttpClientBuilder.create().build().execute(RequestBuilder
                .get("https://graph.qq.com/oauth2.0/token").addParameter("client_id", QQ_CLIENT_ID)
                .addParameter("client_secret", QQ_CLIENT_SECRET)
                .addParameter("grant_type", "authorization_code")
                .addParameter("redirect_uri", java.net.URLEncoder.encode("http://www.hfans.com.cn/getQQQrConnect.html", "UTF-8"))
                .addParameter("code", code).build())) {
            String responseBody = EntityUtils.toString(response.getEntity());
            if (StringUtils.startsWith(responseBody, "callback")) {
                JSONObject jsonObject = JSON.parseObject(
                        StringUtils.substringBetween(EntityUtils.toString(response.getEntity()), "callback(", ");"));
                this.checkResponse(jsonObject, "error", "error_description", "获取access_token失败");
            }

            List<NameValuePair> nvpList = URLEncodedUtils.parse(responseBody, StandardCharsets.UTF_8);
            for (NameValuePair nvp : nvpList) {
                if (StringUtils.equals("access_token", nvp.getName())) {
                    accessToken = nvp.getValue();
                    break;
                }
            }
        }

        try (CloseableHttpResponse response = HttpClientBuilder.create().build().execute(RequestBuilder
                .get("https://graph.qq.com/oauth2.0/me").addParameter("access_token", accessToken).build())) {
            JSONObject jsonObject = JSON.parseObject(
                    StringUtils.substringBetween(EntityUtils.toString(response.getEntity()), "callback(", ");"));
            this.checkResponse(jsonObject, "error", "error_description", "获取openid失败");
            openid = jsonObject.getString("openid");
        }

        try (CloseableHttpResponse response = HttpClientBuilder.create().build()
                .execute(RequestBuilder.get("https://graph.qq.com/user/get_user_info")
                        .addParameter("access_token", accessToken).addParameter("oauth_consumer_key", "101407190")
                        .addParameter("format", "json").addParameter("openid", openid).build())) {
            JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));

            log.debug("Third QQ user information json is : {}", jsonObject.toJSONString());

            int ret = jsonObject.getIntValue("ret");
            if (ret != 0) {
                throw new SystemInnerBusinessException(
                        "获取userInfo失败：errorCode=" + ret + ",errorMsg=" + jsonObject.getString("msg"));
            }

            UserInfo userInfo = new UserInfo();
            userInfo.setAvatar(StringUtils.defaultString(jsonObject.getString("figureurl_qq_2"),
                    jsonObject.getString("figureurl_qq_1")));
            userInfo.setNickname(jsonObject.getString("nickname"));
            String gender = jsonObject.getString("gender");
            userInfo.setSex("男".equals(gender) ? UserSex.male.getCode() : ("女".equals(gender) ? UserSex.female.getCode() : UserSex.unknow.getCode()));
            userInfo.setThirdPartyUserId(openid);
            userInfo.setAccountType(AccountType.qq.getCode());

            return userInfo;
        }
    }

    private UserInfo getUserInfoFromWeixinMina(String code, Map<String, String> miniVerifyMap)
            throws IOException {
        String encryptedData = miniVerifyMap.get("encryptedData");
        String iv = miniVerifyMap.get("iv");
        String signature = miniVerifyMap.get("signature");
        String rawData = miniVerifyMap.get("rawData");

        if (StringUtils.isAnyBlank(encryptedData, iv, signature, rawData)) {
            throw new ArgumentNotValidException("参数错误");
        }

        HttpUriRequest request = RequestBuilder.get("https://api.weixin.qq.com/sns/jscode2session")
                .addParameter("appid", WeixinConstant.WEIXIN_MINA_APP_ID).addParameter("secret", WeixinConstant.WEIXIN_MINA_SECRET)
                .addParameter("grant_type", "authorization_code").addParameter("js_code", code).build();

        log.debug("Third weixin mina check request url is: {}", request.getURI().toString());

        String errorCodeKey = "errcode";
        String errorMsgKey = "errmsg";
        String exceptionMessagePrefix = "获取微信access_token失败";
        JSONObject jsonObject = this.doRequestAndCheckResponse(request, errorCodeKey, errorMsgKey, exceptionMessagePrefix);

        // 这里需要unionId,如果打印出来的是openid的话，需要按照微信给的文档到公众平台进行相关的操作设置
        log.debug("Third weixin mina unionId and session_key json is : {}", jsonObject.toJSONString());

        String sessionKey = jsonObject.getString("session_key");
        String signature2 = DigestUtils.sha1Hex(rawData + sessionKey);

        if (!StringUtils.equals(signature, signature2)) {
            throw new SystemInnerBusinessException("校验微信小程序签名失败");
        }

        jsonObject = this.decryptUserInfo(encryptedData, sessionKey, iv);

        log.debug("Third weixin mina user information json is : {}", jsonObject.toJSONString());

        if (jsonObject != null) {
            JSONObject watermark = jsonObject.getJSONObject("watermark");
            if (watermark == null || !StringUtils.equals(WeixinConstant.WEIXIN_MINA_APP_ID, watermark.getString("appid"))) {
                throw new SystemInnerBusinessException("校验微信小程序数据水印失败");
            }

            UserInfo userInfo = new UserInfo();
            String avatar = jsonObject.getString("avatarUrl");
            userInfo.setAvatar(StringUtils.equals("/0", avatar) ? null : avatar);
            userInfo.setNickname(jsonObject.getString("nickName"));
            userInfo.setSex(jsonObject.getIntValue("gender"));
            userInfo.setCountry(jsonObject.getString("country"));
            userInfo.setProvince(jsonObject.getString("province"));
            userInfo.setCity(jsonObject.getString("city"));
            userInfo.setThirdPartyUserId(jsonObject.getString("openId"));
            userInfo.setAccountType(AccountType.weixinMina.getCode());

            if (StringUtils.isBlank(userInfo.getThirdPartyUserId())){
               throw new SystemInnerBusinessException("获取不到微信小程序的openId");
            }

            return userInfo;
        }

        return null;
    }

    public JSONObject decryptUserInfo(String encryptedData, String sessionKey, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decodeBase64(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decodeBase64(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decodeBase64(iv);

        try {
            // 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }

            // JDK自带AES不支持PKCS7Padding，这里要用Bouncy提供的实现
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    private JSONObject doRequestAndCheckResponse(HttpUriRequest request, String errorCodeKey, String errorMsgKey,
                                                 String exceptionMessagePrefix) throws IOException {
        try (CloseableHttpResponse response = HttpClientBuilder.create().build().execute(request)) {
            return this.checkResponse(JSON.parseObject(EntityUtils.toString(response.getEntity())), errorCodeKey,
                    errorMsgKey, exceptionMessagePrefix);
        }
    }

    private JSONObject checkResponse(JSONObject jsonObject, String errorCodeKey, String errorMsgKey,
                                     String exceptionMessagePrefix) {
        Integer errorCode = jsonObject.getInteger(errorCodeKey);
        if (errorCode != null && errorCode.intValue() > 0) {
            throw new BadRequestException(
                    exceptionMessagePrefix + "：errorCode=" + errorCode + ", errorCode=" + jsonObject.getString(errorMsgKey));
        }

        return jsonObject;
    }
}
