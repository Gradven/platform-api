package com.channelsharing.hongqu.portal.api.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器注册配置, 本工程暂不启用拦截器
 * Created by liuhangjun on 2017/6/20.
 */
@Configuration
public class ConfigRegisterInterceptor extends WebMvcConfigurerAdapter {

    @Bean
    DebugInterceptor debugInterceptor(){
        return new DebugInterceptor();
    }

    @Bean
    LoginCheckInterceptor loginCheckInterceptor(){
        return new LoginCheckInterceptor();
    }


    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(debugInterceptor())
                .addPathPatterns("/v1/**");  //使用两个 **号 表示全url匹配

        /**registry.addInterceptor(loginCheckInterceptor())
                //添加需要验证登录用户操作权限的请求
                .addPathPatterns(
                        "/v1/**")
                //排除不需要验证登录用户操作权限的请求
                .excludePathPatterns(
                        "/v1/aliyun/oss/policies",
                		"/v1/userInfo/login",
                        "/v1/session/**",
                        "/v1/user/send/**",
                        "/v1/user/activate",
                        "/v1/wxpay/notify",
                        "/v/user/check/email",
                        "/v/user/check/mobile");*/


        //super.addInterceptors(registry);

    }


}
