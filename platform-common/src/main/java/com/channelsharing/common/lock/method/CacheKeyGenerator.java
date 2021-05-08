package com.channelsharing.common.lock.method;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Update by liuhangjun on 2018/6/21.
 *
 * Create by Levin
 */
public interface CacheKeyGenerator {
    
    /**
     * 获取AOP参数,生成指定缓存Key
     *
     * @param pjp PJP
     * @return 缓存KEY
     */
    String getLockKey(ProceedingJoinPoint pjp);
}
