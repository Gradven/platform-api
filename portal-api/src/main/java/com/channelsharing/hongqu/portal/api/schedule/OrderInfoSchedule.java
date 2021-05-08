package com.channelsharing.hongqu.portal.api.schedule;

import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.entity.Paging;
import com.channelsharing.common.lock.RedisLock;
import com.channelsharing.common.utils.DateUtils;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.entity.OrderInfo;
import com.channelsharing.hongqu.portal.api.service.OrderInfoService;
import com.channelsharing.pub.aspect.ScheduleLog;
import com.channelsharing.pub.enums.CancelType;
import com.channelsharing.pub.enums.OrderStatus;
import com.channelsharing.pub.enums.PayStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 商品订单超时处理
 * Created by liuhangjun on 2018/8/2.
 */
@Slf4j
@Service
public class OrderInfoSchedule {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private String lockKey =  Constant.PORTAL_CACHE_PREFIX + ":" + this.getClass().getName();
    
    @Autowired
    private OrderInfoService orderInfoService;
    
    
    // 从第一分钟开始，设置每隔3分钟执行一次任务
    @Scheduled(cron = "0 1/3 * * * ?")
    @ScheduleLog(message = "订单超时未支付处理")
    public void build() {
        
        // 任务开始前获取锁
        RedisLock lock = new RedisLock(redisTemplate, lockKey, ExpireTimeConstant.HALF_AN_HOUR);
        lock.tryLock();
        
        this.dealBusiness();
        
        // 任务结束后释放锁
        lock.unlock();
        
    }
    
    /**
     * 超时订单业务逻辑处理
     */
    private void dealBusiness(){
        int num = 200; // 一次只处理200条数据
        // 业务逻辑
        OrderInfo query = new OrderInfo();
        query.setLimit(num);
        Date beginTime = DateUtils.minusSecond(ExpireTimeConstant.ONE_DAY);  // 前1天
        Date endTime = DateUtils.minusSecond(ExpireTimeConstant.HALF_AN_HOUR);    // 前30分钟
        query.setBeginCreateTime(beginTime);
        query.setEndCreateTime(endTime);
        query.setPayStatus(PayStatus.unPay.getCode());
        query.setCancelType(CancelType.unCancel.getCode());
        
        Paging<OrderInfo> orderInfoPaging = orderInfoService.findPaging(query);
        if (orderInfoPaging != null){
            for (OrderInfo orderInfo : orderInfoPaging.getRows()){
    
                orderInfoService.cancelOrder(orderInfo.getSn(), CancelType.outTimeCancel, orderInfo.getUserId(), null);
            }
        }
    }
}
