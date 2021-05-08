/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.hongqu.portal.api.entity.CartInfo;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 购物车Dao接口
 * @author liuhangjun
 * @version 2018-06-22
 */
@Mapper
public interface CartInfoDao extends CrudDao<CartInfo> {

}
