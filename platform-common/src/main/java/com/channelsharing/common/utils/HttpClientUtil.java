package com.channelsharing.common.utils;


import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HttpClientUtil {
	public static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	private static CloseableHttpClient client;

	static {
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(30000).setConnectTimeout(30000)
				.setSocketTimeout(30000).build();

		ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(Consts.UTF_8).build();

		SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(30000).build();

		client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setMaxConnPerRoute(300)
				.setMaxConnTotal(600).setConnectionTimeToLive(30, TimeUnit.SECONDS)
				.setDefaultConnectionConfig(connectionConfig).setDefaultSocketConfig(socketConfig)
				.setRetryHandler(new StandardHttpRequestRetryHandler()).build();
	}

	/**
	 * 使用get方式调用
	 *
	 * @param url 调用的URL
	 * @return 调用得到的字符串
	 */
	public static String requestGet(String url) {
		return getResponseStr(RequestBuilder.get(url).build());
	}

	public static void sendRequestOneway(String url) {
		sendRequestOneway(RequestBuilder.get(url).build());
	}

	/**
	 * 使用get方式调用
	 *
	 * @param url
	 *            调用的URL
	 * @param parameters
	 *            传递的参数值
	 * @return 调用得到的字符串
	 */
	public static String requestGet(String url, NameValuePair[] parameters) {
		return getResponseStr(RequestBuilder.get(url).addParameters(parameters).build());
	}

	/**
	 * 使用post方式调用
	 *
	 * @param url
	 *            调用的URL
	 * @return 调用得到的字符串
	 */
	public static String httpClientPost(String url) {
		return getResponseStr(RequestBuilder.post(url).build());
	}

	/**
	 * 使用post方式调用
	 *
	 * @param url
	 *            调用的URL
	 * @param parameters
	 *
	 * @return 调用得到的字符串
	 */
	public static String httpClientPost(String url, NameValuePair[] parameters) {
		return getResponseStr(RequestBuilder.post(url).addParameters(parameters).build());
	}

	/**
	 * 发送post或get请求获取响应信息
	 *
	 * @param request
	 *            http请求类型,post或get请求
	 * @return 服务器返回的信息
	 */
	public static String getResponseStr(HttpUriRequest request) {
		CloseableHttpResponse response = null;
		try {
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				logger.error("HttpClient Error : statusCode = " + response.getStatusLine().getStatusCode() + ", uri :"
						+ request.getURI());
				return "";
			}

			response.getEntity().isStreaming();
			return EntityUtils.toString(response.getEntity(), Consts.UTF_8);
		} catch (Exception e) {
			logger.error("network error!" + e.getMessage(), e);
			return "";
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 发送post或get请求获取响应信息
	 *
	 * @param request
	 *            http请求类型,post或get请求
	 * @return 服务器返回的信息
	 */
	public static void sendRequestOneway(HttpUriRequest request) {
		CloseableHttpResponse response = null;
		try {
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				logger.error("HttpClient Error : statusCode = " + response.getStatusLine().getStatusCode() + ", uri :"
						+ request.getURI());
			}

			EntityUtils.consumeQuietly(response.getEntity());
		} catch (Exception e) {
			logger.error("network error!" + e.getMessage(), e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void main(String[] args) {
		requestGet("http://www.baidu.com");
	}
}
