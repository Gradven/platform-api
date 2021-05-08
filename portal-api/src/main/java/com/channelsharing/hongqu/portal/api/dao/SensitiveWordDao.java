/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;


import com.channelsharing.hongqu.portal.api.entity.SensitiveWord;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 敏感词信息Mapper接口
 * @author liuhangjun
 * @version 2017-07-10
 */
@Mapper
public interface SensitiveWordDao extends CrudDao<SensitiveWord> {

}
