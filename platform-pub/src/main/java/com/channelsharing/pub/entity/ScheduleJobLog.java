/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.pub.entity;

import com.channelsharing.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 定时任务日志Entity
 * @author liuhangjun
 * @version 2018-08-04
 */
@Data
public class ScheduleJobLog extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String className;		// 类名称
	private String methodName;		// 方法名
	private Integer status;		// 任务状态    1：成功    0：失败
	private String error;		// 失败信息
	private String message;		// 信息
	private Integer duration;		// 耗时(单位：毫秒)
	private Date beginTime;		// 开始时间
	private Date endTime;		// 结束时间


	public ScheduleJobLog() {
		super();
	}


}
