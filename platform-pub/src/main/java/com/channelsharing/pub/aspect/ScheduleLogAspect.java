package com.channelsharing.pub.aspect;

import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.common.utils.DateUtils;
import com.channelsharing.pub.entity.ScheduleJobLog;
import com.channelsharing.pub.service.ScheduleJobLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by liuhangjun on 2018/8/4.
 */
@Aspect
@Component
@Slf4j
public class ScheduleLogAspect {
    
    @Autowired
    private ScheduleJobLogService scheduleJobLogService;
    
    @Pointcut("@annotation(scheduleLog)")
    public void executeMethodLog(ScheduleLog scheduleLog) {
    }
    
    
    /**
            * 环绕方法,可自定义目标方法执行的时机
     * @param pjd JoinPoint的子接口,添加了
     *            Object proceed() throws Throwable 执行目标方法
     *            Object proceed(Object[] var1) throws Throwable 传入的新的参数去执行目标方法
     *            两个方法
     * @return 此方法需要返回值,返回值视为目标方法的返回值
     */
    @Around("executeMethodLog(scheduleLog)")
    public Object aroundMethod(ProceedingJoinPoint pjd, ScheduleLog scheduleLog){
        Object result = null;
        String error = "";
        String methodName = "";
        String className = "";
        long beginTime = 0L;
        long endTime = 0L;
        long duration = 0L;
        int status = BooleanEnum.no.getCode();
        
        try {
            //前置通知
            log.debug("目标方法执行前...");
            methodName = pjd.getSignature().getName();
            className = pjd.getTarget().getClass().getName();
            log.debug("Message:{}, ClassName: {}, MethodName: {}", scheduleLog.message(), className, methodName);
            
            beginTime = System.currentTimeMillis();
            //执行目标方法
            result = pjd.proceed();
            endTime = System.currentTimeMillis();
            
            duration = endTime - beginTime;
    
            status = BooleanEnum.yes.getCode();
        } catch (Throwable e) {
            
            error = e.getMessage();
            //异常通知
            log.error("error message {}", e.getMessage());
            
        }finally {
            
            Date beginDate = new Date(beginTime);
            Date endDate = new Date(endTime);
    
            ScheduleJobLog scheduleJobLog = new ScheduleJobLog();
            scheduleJobLog.setBeginTime(beginDate);
            scheduleJobLog.setEndTime(endDate);
            scheduleJobLog.setMessage(scheduleLog.message());
            scheduleJobLog.setClassName(className);
            scheduleJobLog.setMethodName(methodName);
            scheduleJobLog.setDuration((int)duration);
            scheduleJobLog.setError(error);
            scheduleJobLog.setStatus(status);
            
            scheduleJobLogService.add(scheduleJobLog);
            
            log.debug("Execute begin time {}, end time {}, duration {} ", beginDate, endDate, duration );
            //后置通知
            log.debug("最终执行的日志数据入库");
        }
        
        
        return result;
    }
    
}
