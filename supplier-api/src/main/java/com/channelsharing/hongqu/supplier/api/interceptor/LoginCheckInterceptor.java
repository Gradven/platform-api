package com.channelsharing.hongqu.supplier.api.interceptor;


import com.channelsharing.hongqu.supplier.api.entity.SupplierUser;
import com.channelsharing.common.exception.UserNotLoginException;
import com.channelsharing.hongqu.supplier.api.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liuhangjun on 2017/6/20.
 */
@Slf4j
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.debug("Current user session id is : {}", request.getSession().getId());

        SupplierUser supplierUser;
        try{
            supplierUser = (SupplierUser)request.getSession().getAttribute(BaseController.SESSION_SUPPLIER_USER_KEY);
        }catch (ClassCastException ex){
            throw new UserNotLoginException();
        }


        if (supplierUser == null)
            throw new UserNotLoginException();

        return true;
    }

}
