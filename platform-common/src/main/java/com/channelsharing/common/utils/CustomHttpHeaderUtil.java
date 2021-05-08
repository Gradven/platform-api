package com.channelsharing.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class CustomHttpHeaderUtil {
	public static String getRemoteIp() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		return getRemoteIp(request);
	}

	public static String getRemoteIp(HttpServletRequest request) {
		String remoteIp = request.getHeader("X-Forwarded-For");
		if (StringUtils.isBlank(remoteIp) || StringUtils.equalsIgnoreCase("unknown", remoteIp)) {
			remoteIp = request.getHeader("X-Real-IP");
		}
		if (StringUtils.isBlank(remoteIp) || StringUtils.equalsIgnoreCase("unknown", remoteIp)) {
			remoteIp = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(remoteIp) || StringUtils.equalsIgnoreCase("unknown", remoteIp)) {
			remoteIp = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(remoteIp) || StringUtils.equalsIgnoreCase("unknown", remoteIp)) {
			remoteIp = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isBlank(remoteIp) || StringUtils.equalsIgnoreCase("unknown", remoteIp)) {
			remoteIp = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isBlank(remoteIp) || StringUtils.equalsIgnoreCase("unknown", remoteIp)) {
			remoteIp = request.getRemoteAddr();
		}
		if (StringUtils.isBlank(remoteIp) || StringUtils.equalsIgnoreCase("unknown", remoteIp)) {
			remoteIp = request.getRemoteHost();
		}
		return remoteIp;
	}
}
