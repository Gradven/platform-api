package com.channelsharing.hongqu.portal.api.schedule;

import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.lock.RedisLock;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.service.ShopProfitWalletService;
import com.channelsharing.pub.aspect.ScheduleLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 店铺收益定时处理
 * Created by liuhangjun on 2018/8/2.
 */
@Slf4j
@Service
public class ShopProfitSchedule {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private String lockKey =  Constant.PORTAL_CACHE_PREFIX + ":" + this.getClass().getName();
    
    @Autowired
    private ShopProfitWalletService shopProfitWalletService;
    
    
    // 设置3分钟执行一次任务
    @Scheduled(cron = "0 */3 * * * ?")
    @ScheduleLog(message = "店铺收益延时到账处理")
    public void build() {
        
        // 任务开始前获取锁
        RedisLock lock = new RedisLock(redisTemplate, lockKey, ExpireTimeConstant.HALF_AN_HOUR);
        lock.tryLock();
    
        // 业务逻辑
        shopProfitWalletService.calculateProfitToWallet();
    
        // 任务结束后释放锁
        lock.unlock();
        
    
    }
    
    
    
    
    
}
