/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.portal.api.entity.PageFragment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 页面区块信息Dao接口
 * @author liuhangjun
 * @version 2018-07-26
 */
@Mapper
public interface PageFragmentDao extends CrudDao<PageFragment> {

}