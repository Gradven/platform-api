/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.portal.api.entity.InvoiceInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 发票信息Dao接口
 * @author liuhangjun
 * @version 2018-07-29
 */
@Mapper
public interface InvoiceInfoDao extends CrudDao<InvoiceInfo> {

}