package com.channelsharing.common.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpClientUtil {
	public static final ConnectionPool CONNECTION_POOL = new ConnectionPool(300, 10, TimeUnit.MINUTES);

	private static SSLSocketFactory sslSocketFactory;

	private static X509TrustManager trustManager;

	static {
		trustManager = new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				X509Certificate[] x509Certificates = new X509Certificate[0];
				return x509Certificates;
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
		};

		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { trustManager }, null);
			sslSocketFactory = sslContext.getSocketFactory();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (KeyManagementException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static final MediaType APPLICATION_JSON = MediaType.parse("application/json; charset=utf-8");

	private static okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient.Builder()
			.sslSocketFactory(sslSocketFactory, trustManager).hostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			}).connectionPool(CONNECTION_POOL).connectTimeout(3, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
			.writeTimeout(30, TimeUnit.SECONDS).build();

	public static Response execute(Request request) throws IOException {
		return okHttpClient.newBuilder().build().newCall(request).execute();
	}

	public static Response execute(Request request, long connectTimeout, long readTimeout) throws IOException {
		return okHttpClient.newBuilder().connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
				.readTimeout(readTimeout, TimeUnit.MILLISECONDS).build().newCall(request).execute();
	}
}
