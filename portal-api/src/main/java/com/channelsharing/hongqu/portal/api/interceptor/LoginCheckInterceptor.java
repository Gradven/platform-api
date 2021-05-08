package com.channelsharing.hongqu.portal.api.interceptor;


import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.common.exception.UserNotLoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liuhangjun on 2017/6/20.
 */
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        logger.debug("Current user session id is : {}", request.getSession().getId());

        UserInfo userInfo;
        try{
            userInfo = (UserInfo)request.getSession().getAttribute(BaseController.SESSION_USER_KEY);
        }catch (ClassCastException ex){
            throw new UserNotLoginException();
        }


        if (userInfo == null)
            throw new UserNotLoginException();

        return true;
    }

}
