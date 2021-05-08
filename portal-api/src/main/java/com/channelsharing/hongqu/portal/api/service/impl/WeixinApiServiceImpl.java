package com.channelsharing.hongqu.portal.api.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.channelsharing.cloud.aliyun.oss.OssUtil;
import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.service.WeixinApiService;
import com.channelsharing.hongqu.portal.api.weixin.WeixinAccessTokenProvider;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class WeixinApiServiceImpl implements WeixinApiService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;

	@Resource
	private WeixinAccessTokenProvider accessTokenProvider;

	@Resource
	private OssUtil ossUtil;

	private static final MediaType APPLICATION_JSON = MediaType.parse("application/json;charset=UTF-8");

	@Override
	@CacheDuration(duration = ExpireTimeConstant.NONE)
	@Cacheable(value = PORTAL_CACHE_PREFIX
			+ "wxaCode", key = "#root.target.PORTAL_CACHE_PREFIX + 'wxaCode:' + #fileKey", unless = "#result == null")
	public String getwxacodeunlimit(String fileKey, String body) {
		String accessToken = accessTokenProvider.accessToken();
		if (accessToken != null) {
			String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;

			RequestBody requestBody = RequestBody.create(APPLICATION_JSON, body.getBytes(StandardCharsets.UTF_8));

			Request request = new Request.Builder().post(requestBody).url(url).build();
			try (Response response = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
					.readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build().newCall(request)
					.execute()) {

				String contentType = response.header("Content-Type");
				MediaType mediaType = MediaType.parse(contentType);
				String subtype = mediaType != null ? mediaType.subtype() : "png";

				if (response.isSuccessful() && !"json".equals(subtype)) {
					String fileName = "wxacode/" + fileKey + "." + subtype;
					String imgUrl = ossUtil.uploadFile(new ByteArrayInputStream(response.body().bytes()), fileName);

					return imgUrl;
				} else if ("json".equals(subtype)) {
					String responseBody = response.body().string();
					logger.error("request getwxacodeunlimit api fail, status code = {}, response body = {}",
							response.code(), responseBody);
					throw new SystemInnerBusinessException(
							"请求生成小程序码接口失败，原因：" + JSON.parseObject(responseBody).getString("errmsg"));
				} else {
					String responseBody = response.body().string();
					logger.error("request getwxacodeunlimit api fail, status code = {}, response body = {}",
							response.code(), responseBody);
					throw new SystemInnerBusinessException("请求生成小程序码接口失败，原因：" + responseBody);
				}
			} catch (IOException e) {
				logger.error("request getwxacodeunlimit api error", e);
				throw new SystemInnerBusinessException("请求生成小程序码接口失败，原因：网络异常");
			}
		} else {
			throw new SystemInnerBusinessException("获取微信access_token失败");
		}
	}
}
