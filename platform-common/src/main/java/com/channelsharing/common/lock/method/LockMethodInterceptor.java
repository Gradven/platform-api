package com.channelsharing.common.lock.method;


import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;

import com.channelsharing.common.entity.BaseEntity;
import com.channelsharing.common.exception.ArgumentNotValidException;
import com.channelsharing.common.exception.BadRequestException;
import com.channelsharing.common.exception.CacheLockException;
import com.channelsharing.common.exception.DataNotFoundException;
import com.channelsharing.common.exception.DuplicateKeyException;
import com.channelsharing.common.exception.ForbiddenException;
import com.channelsharing.common.exception.NotFoundException;
import com.channelsharing.common.exception.OccupiedException;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import com.channelsharing.common.exception.UnauthorizedException;
import com.channelsharing.common.exception.UserNotLoginException;
import com.channelsharing.common.lock.RedisLock;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * update by liuhangjun on 2018/6/21.
 *
 * create by Levin
 */
@Aspect
@Configuration
@Slf4j
public class LockMethodInterceptor {
    
    @Resource(name="sessionRepository")
    private SessionRepository<ExpiringSession> sessionRepository;
    
    
    @Autowired
    public LockMethodInterceptor(StringRedisTemplate lockRedisTemplate, CacheKeyGenerator cacheKeyGenerator) {
        this.lockRedisTemplate = lockRedisTemplate;
        this.cacheKeyGenerator = cacheKeyGenerator;
    }
    
    private final StringRedisTemplate lockRedisTemplate;
    private final CacheKeyGenerator cacheKeyGenerator;
    
    
    @Around("execution(public * *(..)) && @annotation(com.channelsharing.common.lock.method.CacheLock)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        CacheLock lock = method.getAnnotation(CacheLock.class);
        if (StringUtils.isEmpty(lock.prefix())) {
            throw new RuntimeException("lock key don't null...");
        }
        String lockKey = cacheKeyGenerator.getLockKey(pjp);
    
        String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
    
        ExpiringSession session = sessionRepository.getSession(sessionId);
        BaseEntity baseEntity = session.getAttribute("SESSION_USER_KEY");
        
        // 如果用户登录了那么使用用户id作为key的组成部分
        if (baseEntity != null){
            lockKey = baseEntity.getId() + ":" + lockKey;
        }
        
    
        try {
            
            // 采用原生 API 来实现分布式锁
            //final Boolean success = lockRedisTemplate.execute((RedisCallback<Boolean>) connection -> connection.set(lockKey.getBytes(), new byte[0], Expiration.from(lock.expire(), lock.timeUnit()), RedisStringCommands.SetOption.SET_IF_ABSENT));
            
            
            RedisLock redisLock = new RedisLock(lockRedisTemplate, lockKey, lock.expire());
            Boolean success = redisLock.tryLock();
            
            if (!success) {
                throw new CacheLockException("请勿重复请求");
            }
            
            try {
                return pjp.proceed();
            }catch (BadRequestException ex) {
                throw new BadRequestException(ex.getMessage());
            }catch (ArgumentNotValidException ex) {
                throw new ArgumentNotValidException(ex.getMessage());
            }catch (CacheLockException ex) {
                throw new CacheLockException(ex.getMessage());
            }catch (DataNotFoundException ex) {
                throw new DataNotFoundException(ex.getMessage());
            }catch (DuplicateKeyException ex) {
                throw new DuplicateKeyException(ex.getMessage());
            }catch (ForbiddenException ex) {
                throw new ForbiddenException(ex.getMessage());
            }catch (NotFoundException ex) {
                throw new NotFoundException(ex.getMessage());
            }catch (OccupiedException ex) {
                throw new OccupiedException(ex.getMessage());
            }catch (SystemInnerBusinessException ex) {
                throw new SystemInnerBusinessException(ex.getMessage());
            }catch (UnauthorizedException ex) {
                throw new UnauthorizedException();
            }catch (UserNotLoginException ex) {
                throw new UserNotLoginException();
            }catch (Throwable throwable) {
                //throwable.printStackTrace();
            	log.error(throwable.getMessage(), throwable);
                log.error(throwable.fillInStackTrace().toString());
                log.error(throwable.getLocalizedMessage());
                throw new RuntimeException(throwable.getMessage());
            }
        } finally {
            lockRedisTemplate.delete(lockKey);
        }
        
    }
    
    
}
