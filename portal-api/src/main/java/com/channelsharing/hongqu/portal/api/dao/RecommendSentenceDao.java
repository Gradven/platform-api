/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.portal.api.entity.RecommendSentence;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品运营推荐语Dao接口
 * @author liuhangjun
 * @version 2018-07-23
 */
@Mapper
public interface RecommendSentenceDao extends CrudDao<RecommendSentence> {

}