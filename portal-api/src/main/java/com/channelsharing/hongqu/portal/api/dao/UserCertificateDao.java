/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.portal.api.entity.UserCertificate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户认证信息Dao接口
 * @author liuhangjun
 * @version 2018-07-12
 */
@Mapper
public interface UserCertificateDao extends CrudDao<UserCertificate> {

}
