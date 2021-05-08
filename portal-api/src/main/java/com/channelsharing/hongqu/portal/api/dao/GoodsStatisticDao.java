/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.portal.api.entity.GoodsStatistic;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品实时数据统计Dao接口
 * @author liuhangjun
 * @version 2018-07-27
 */
@Mapper
public interface GoodsStatisticDao extends CrudDao<GoodsStatistic> {

}