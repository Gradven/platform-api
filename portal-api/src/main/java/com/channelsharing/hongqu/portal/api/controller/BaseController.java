package com.channelsharing.hongqu.portal.api.controller;


import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.common.binder.DateEditor;
import com.channelsharing.common.exception.UserNotLoginException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by liuhangjun on 2017/6/9.
 */
public abstract class BaseController {

    public static final String SESSION_USER_KEY = "SESSION_USER_KEY";   //用户会话关键字

    @Resource
    protected HttpServletRequest request;


    protected UserInfo currentUser() {
        HttpSession session = getSession(false);

        if (session != null) {
            UserInfo userInfo = (UserInfo) session.getAttribute(SESSION_USER_KEY);
            if (userInfo != null && userInfo.getId() != null) {
                return userInfo;
            }
        }

        throw new UserNotLoginException();
    }

    protected UserInfo currentUserWithoutException() {
        HttpSession session = getSession(false);
        UserInfo userInfo = null;
        if (session != null) {
            userInfo = (UserInfo) session.getAttribute(SESSION_USER_KEY);
        }

        return userInfo;
    }

    protected void addUserToSession(UserInfo userInfo) {
        request.getSession().setAttribute(SESSION_USER_KEY, userInfo);
    }

    protected Long currentUserId() {
        return currentUser().getId();
    }

    /**
     * 获取session对象
     */
    protected HttpSession getSession(boolean create) {
        return request.getSession(create);
    }

    /**
     * 获取request对象
     *
     * @return
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        //对于需要转换为Date类型的属性，使用DateEditor进行处理
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

}
