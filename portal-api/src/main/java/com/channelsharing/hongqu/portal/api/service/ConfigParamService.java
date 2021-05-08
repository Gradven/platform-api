/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.hongqu.portal.api.entity.ConfigParam;
import com.channelsharing.common.service.CrudService;


/**
 * 系统配置项Service
 * @author liuhangjun
 * @version 2018-06-17
 */
public interface ConfigParamService extends CrudService<ConfigParam>{
 
    String findOne(String key);

}
