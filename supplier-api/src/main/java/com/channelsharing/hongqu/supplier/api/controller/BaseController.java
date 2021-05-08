package com.channelsharing.hongqu.supplier.api.controller;


import com.channelsharing.common.binder.DateEditor;
import com.channelsharing.hongqu.supplier.api.entity.SupplierUser;
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

    public static final String SESSION_SUPPLIER_USER_KEY = "SESSION_SUPPLIER_USER_KEY";   //用户会话关键字

    @Resource
    protected HttpServletRequest request;


    protected SupplierUser currentSupplierUser() {
        HttpSession session = getSession(false);

        if (session != null) {
            SupplierUser supplierUser = (SupplierUser) session.getAttribute(SESSION_SUPPLIER_USER_KEY);
            if (supplierUser != null && supplierUser.getId() != null) {
                return supplierUser;
            }
        }

        throw new UserNotLoginException();
    }

    protected SupplierUser currentSupplierUserWithoutException() {
        HttpSession session = getSession(false);
        SupplierUser supplierUser = null;
        if (session != null) {
            supplierUser = (SupplierUser) session.getAttribute(SESSION_SUPPLIER_USER_KEY);
        }

        return supplierUser;
    }

    protected void addSupplierUserToSession(SupplierUser supplierUser) {
        request.getSession().setAttribute(SESSION_SUPPLIER_USER_KEY, supplierUser);
    }

    protected Long currentSupplierUserId() {
        return currentSupplierUser().getId();
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
