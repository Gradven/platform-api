package com.channelsharing.hongqu.portal.api.weixin;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.constant.WeixinConstant;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
@CacheDuration(duration = ExpireTimeConstant.ONE_HOUR)
public class WeixinAccessTokenProvider {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;

	private static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
			+ WeixinConstant.WEIXIN_MINA_APP_ID + "&secret=" + WeixinConstant.WEIXIN_MINA_SECRET;

	@Cacheable(value = PORTAL_CACHE_PREFIX
			+ "wxAccessToken", key = "#root.target.PORTAL_CACHE_PREFIX + 'wxAccessToken'", unless = "#result == null")
	public String accessToken() {
		Request request = new Request.Builder().get().url(GET_ACCESS_TOKEN_URL).build();
		try {
			Response response = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
					.readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build().newCall(request)
					.execute();

			if (response.isSuccessful()) {
				JSONObject jsonObject = JSON.parseObject(response.body().string());
				Integer errorCode = jsonObject.getInteger("errcode");

				if (errorCode == null || errorCode.intValue() == 0) {
					return jsonObject.getString("access_token");
				}
			}

			logger.error("get weixin access token fail, status code = {}, response body = {}", response.code(),
					response.body().string());
		} catch (IOException e) {
			logger.error("get weixin access token error", e);
		}

		return null;
	}
}
