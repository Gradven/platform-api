/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.hongqu.portal.api.entity.WeixinNotifyData;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信回调数据记录Dao接口
 * @author liuhangjun
 * @version 2018-03-29
 */
@Mapper
public interface WeixinNotifyDataDao extends CrudDao<WeixinNotifyData> {

}
