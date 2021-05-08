/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.pub.service.impl;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.pub.dao.ScheduleJobLogDao;
import com.channelsharing.pub.entity.ScheduleJobLog;
import com.channelsharing.pub.service.ScheduleJobLogService;
import org.springframework.stereotype.Service;

/**
 * 定时任务日志Service
 * @author liuhangjun
 * @version 2018-08-04
 */
@Service
public class ScheduleJobLogServiceImpl extends CrudServiceImpl<ScheduleJobLogDao, ScheduleJobLog> implements ScheduleJobLogService {

    @Override
    public ScheduleJobLog findOne(Long id) {
        ScheduleJobLog entity = new ScheduleJobLog();
        entity.setId(id);

        return super.findOne(entity);
    }


}
