package com.channelsharing.hongqu.portal.api.schedule;

import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.lock.RedisLock;
import com.channelsharing.common.utils.DateUtils;
import com.channelsharing.hongqu.portal.api.constant.ConfigParamConstant;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.entity.OrderGoods;
import com.channelsharing.hongqu.portal.api.service.ConfigParamService;
import com.channelsharing.hongqu.portal.api.service.OrderGoodsService;
import com.channelsharing.pub.aspect.ScheduleLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 如果超过N天，用户为确认收货，那么系统自动确认签收
 * Created by liuhangjun on 2018/8/10.
 */
@Slf4j
@Service
public class AutoConfirmSignSchedule {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private String lockKey =  Constant.PORTAL_CACHE_PREFIX + ":" + this.getClass().getName();
    
    @Autowired
    private OrderGoodsService orderGoodsService;
    
    @Autowired
    private ConfigParamService configParamService;
    
    // 设置5分钟执行一次任务
    @Scheduled(cron = "0 */5 * * * ?")
    @ScheduleLog(message = "系统自动确认签收处理")
    public void build() {
        
        // 任务开始前获取锁
        RedisLock lock = new RedisLock(redisTemplate, lockKey, ExpireTimeConstant.HALF_AN_HOUR);
        lock.tryLock();
        
        // 业务逻辑
        this.dealBusiness();
        
        // 任务结束后释放锁
        lock.unlock();
        
    }
    
    /**
     * 处理自动确认签收业务逻辑
     */
    private void dealBusiness(){
    
        String days = configParamService.findOne(ConfigParamConstant.PORTAL_ORDER_GOODS_AUTO_CONFIRM_DAYS);
        int daysInt = NumberUtils.toInt(days, 15);
    
        Date beginTime = DateUtils.minusSecond(daysInt * ExpireTimeConstant.ONE_DAY + ExpireTimeConstant.ONE_DAY);  // 前N天 + 1天
        Date endTime = DateUtils.minusSecond(daysInt * ExpireTimeConstant.ONE_DAY);    // 前N天
        OrderGoods orderGoods = new OrderGoods();
        orderGoods.setBeginDeliveryTime(beginTime);
        orderGoods.setEndDeliveryTime(endTime);
        orderGoodsService.autoConfirm(orderGoods);
        
        
    }

}
