package com.channelsharing.cloud.courier;

import java.io.IOException;

import com.channelsharing.common.yaml.YamlProperties;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.channelsharing.common.utils.OkHttpClientUtil;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class KuaiDi100 {
	private static String key = YamlProperties.getProperty("kuaidi100.key");

	private static String customer = YamlProperties.getProperty("kuaidi100.customer");

	private static Logger logger = LoggerFactory.getLogger(KuaiDi100.class);

	/**
	 * 快递100查询快递信息
	 *
	 * @param com
	 * @param nu
	 * @return
	 * @throws IOException
	 */
	public static JSONObject query(String com, String num) {
		HttpUrl url = new HttpUrl.Builder().scheme("https").host("poll.kuaidi100.com").addPathSegment("poll")
				.addPathSegment("query.do").build();

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("com", com);
		jsonObject.put("num", num);

		String param = jsonObject.toJSONString();

		String sign = DigestUtils.md5Hex(param + key + customer).toUpperCase();

		FormBody body = new FormBody.Builder().add("customer", customer).add("sign", sign).add("param", param).build();

		Request request = new Request.Builder().url(url).post(body).build();
		try {
			Response response = OkHttpClientUtil.execute(request);
			if (response.isSuccessful()) {
				JSONObject result = JSON.parseObject(response.body().string());
				String status = result.getString("status");
				if ("200".equals(status)) {
					return result;
				} else {
					logger.error("query kuaidi100 fail, response is {}", result);
				}
			}
		} catch (IOException e) {
			logger.error("query kuaidi100 IOException", e);
		}

		return null;
	}
}
