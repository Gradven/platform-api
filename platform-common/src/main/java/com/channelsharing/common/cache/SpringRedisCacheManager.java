package com.channelsharing.common.cache;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Created by liuhangjun on 2017/9/7.
 */
public class SpringRedisCacheManager extends RedisCacheManager implements ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;
    
    public SpringRedisCacheManager(RedisOperations redisOperations) {
        super(redisOperations);
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @Override
    public void afterPropertiesSet() {
        parseCacheDuration(applicationContext);
    }
    
    private void parseCacheDuration(ApplicationContext applicationContext) {
        final Map<String, Long> cacheExpires = new HashMap<>();
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        for (String beanName : beanNames) {
            final Class clazz = applicationContext.getType(beanName);
            Service service = AnnotationUtils.findAnnotation(clazz, Service.class);
            if (null == service) {
                continue;
            }
            addCacheExpires(clazz, cacheExpires);
        }
        //设置有效期
        super.setExpires(cacheExpires);
    }
    
    private void addCacheExpires(final Class clazz, final Map<String, Long> cacheExpires) {
        ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                ReflectionUtils.makeAccessible(method);
                CacheDuration cacheDuration = findCacheDuration(clazz, method);
                Cacheable cacheable = AnnotationUtils.findAnnotation(method, Cacheable.class);
                CacheConfig cacheConfig = AnnotationUtils.findAnnotation(clazz, CacheConfig.class);
                Set<String> cacheNames = findCacheNames(cacheConfig, cacheable);
                for (String cacheName : cacheNames) {
                    cacheExpires.put(cacheName, cacheDuration.duration());
                }
            }
        }, new ReflectionUtils.MethodFilter() {
            @Override
            public boolean matches(Method method) {
                return null != AnnotationUtils.findAnnotation(method, Cacheable.class);
            }
        });
    }
    
    /**
     * CacheDuration标注的有效期，优先使用方法上标注的有效期
     *
     * @param clazz
     * @param method
     * @return
     */
    private CacheDuration findCacheDuration(Class clazz, Method method) {
        CacheDuration methodCacheDuration = AnnotationUtils.findAnnotation(method, CacheDuration.class);
        if (null != methodCacheDuration) {
            return methodCacheDuration;
        }
        
        CacheDuration classCacheDuration = AnnotationUtils.findAnnotation(clazz, CacheDuration.class);
        if (null != classCacheDuration) {
            return classCacheDuration;
        }
        throw new IllegalStateException("No CacheDuration config on Class " + clazz.getName() + " and method " + method.toString());
    }
    
    private Set<String> findCacheNames(CacheConfig cacheConfig, Cacheable cacheable) {
        return StringUtils.isEmpty(cacheable.value()) ?
                newHashSet(cacheConfig.cacheNames()) : newHashSet(cacheable.value());
    }
}
