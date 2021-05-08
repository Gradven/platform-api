/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.pub.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.pub.entity.ScheduleJobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志Dao接口
 * @author liuhangjun
 * @version 2018-08-04
 */
@Mapper
public interface ScheduleJobLogDao extends CrudDao<ScheduleJobLog> {

}
