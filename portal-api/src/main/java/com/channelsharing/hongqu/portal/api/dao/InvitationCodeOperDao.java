/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.hongqu.portal.api.entity.InvitationCodeOper;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 运营邀请码Dao接口
 * @author liuhangjun
 * @version 2018-07-16
 */
@Mapper
public interface InvitationCodeOperDao extends CrudDao<InvitationCodeOper> {

}
