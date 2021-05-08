package com.channelsharing.cloud.aliyun.ocr;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.channelsharing.common.yaml.YamlProperties;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.cloudapi.sdk.core.BaseApiClient;
import com.alibaba.cloudapi.sdk.core.enums.Method;
import com.alibaba.cloudapi.sdk.core.enums.Scheme;
import com.alibaba.cloudapi.sdk.core.model.ApiRequest;
import com.alibaba.cloudapi.sdk.core.model.ApiResponse;
import com.alibaba.cloudapi.sdk.core.model.BuilderParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import com.channelsharing.common.utils.OkHttpClientUtil;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liuhangjun on 2018/7/12.
 */
public final class IdCardRecognizeUtil extends BaseApiClient {
	private static Logger logger = LoggerFactory.getLogger(IdCardRecognizeUtil.class);
	private static String APP_KEY = YamlProperties.getProperty("aliyun.ocr.appKey");
	private static String APP_SECRET = YamlProperties.getProperty("aliyun.ocr.appSecret");
	private static String GROUP_HOST = YamlProperties.getProperty("aliyun.ocr.groupHost");
	private static String API_PATH = YamlProperties.getProperty("aliyun.ocr.apiPath");

	private static BuilderParams builderParams = new BuilderParams();
	static {
		builderParams.setAppKey(APP_KEY);
		builderParams.setAppSecret(APP_SECRET);
		builderParams.setConnectionTimeoutMillis(30000);
		builderParams.setReadTimeoutMillis(30000);
		builderParams.setWriteTimeoutMillis(30000);
	}

	private static final IdCardRecognizeUtil instance = new IdCardRecognizeUtil(builderParams);

	private IdCardRecognizeUtil(BuilderParams builderParams) {
		super(builderParams);
	}

	public static JSONObject recognize(String fileUrl, String side) throws IOException {
		Request request = new Request.Builder().get().url(fileUrl).build();
		Response response = OkHttpClientUtil.execute(request);

		if (response.isSuccessful()) {
			byte[] fileContent = response.body().bytes();
			String base64 = Base64.encodeBase64String(fileContent);

			JSONObject configure = new JSONObject();
			configure.put("side", side);

			JSONObject body = new JSONObject();
			body.put("image", base64);
			body.put("configure", configure);

			ApiRequest apiRequest = new ApiRequest(Scheme.HTTPS, Method.POST_BODY, GROUP_HOST, API_PATH,
					JSON.toJSONBytes(body));
			ApiResponse apiResponse = instance.syncInvoke(apiRequest);

			if (apiResponse.getStatusCode() >= 200 && apiResponse.getStatusCode() < 300) {
				JSONObject respBody = JSON.parseObject(new String(apiResponse.getBody(), StandardCharsets.UTF_8));
				return respBody;
			} else {
				String requestId = apiResponse.getHeaders().get("X-Ca-Request-Id");
				String errorMessage = apiResponse.getHeaders().get("X-Ca-Error-Message");

				logger.error(
						"request ocr idcard fail, statusCode={}, fileUrl={}, side={}, requestId={}, errorMessage={}",
						apiResponse.getStatusCode(), fileUrl, side, requestId, errorMessage);
				throw new SystemInnerBusinessException("身份证" + ("face".equals(side) ? "正" : "背") + "面识别失败，请重新拍摄");
			}
		}

		throw new SystemInnerBusinessException("文件下载失败");
	}
}
