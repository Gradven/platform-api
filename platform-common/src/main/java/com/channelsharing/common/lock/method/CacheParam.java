package com.channelsharing.common.lock.method;

import java.lang.annotation.*;

/**
 * 锁的参数
 *
 * update by liuhangjun
 *
 * create by Levin
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheParam {
    
    /**
     * 字段名称
     *
     * @return String
     */
    String name() default "";
}
