package com.channelsharing.hongqu.supplier.api.interceptor;


import com.channelsharing.hongqu.supplier.api.service.SupplierUserService;
import com.channelsharing.hongqu.supplier.api.entity.SupplierUser;
import com.channelsharing.hongqu.supplier.api.controller.BaseController;
import com.channelsharing.common.exception.NotFoundException;
import com.channelsharing.common.utils.CustomHttpHeaderUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang3.StringUtils.isNoneBlank;


public class DebugInterceptor extends HandlerInterceptorAdapter {

	//以后需要改为从数据库中获取
	private String trustedIps = "*";

	@Resource
	private SupplierUserService entUserService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (CorsUtils.isPreFlightRequest(request)) {
			return true;
		}
		String debugUserId = request.getParameter("debugUserId");
		if (StringUtils.isNotBlank(debugUserId) && NumberUtils.isDigits(debugUserId)) {
			SupplierUser entity = new SupplierUser();
			entity.setId(Long.valueOf(debugUserId));
			SupplierUser supplierUser = entUserService.findOne(entity);
			if (supplierUser != null) {
				if (StringUtils.equals(trustedIps, "*")) {
					request.getSession().setAttribute(BaseController.SESSION_SUPPLIER_USER_KEY, supplierUser);
					return true;
				} else {
					String remoteIp = CustomHttpHeaderUtil.getRemoteIp(request);
					if (isNoneBlank(remoteIp, trustedIps) && StringUtils.contains(trustedIps, remoteIp)) {
						request.getSession().setAttribute(BaseController.SESSION_SUPPLIER_USER_KEY, supplierUser);
						return true;
					}
				}
			} else {
				throw new NotFoundException("用户信息不存在");
			}
		}

		return true;
	}
}
