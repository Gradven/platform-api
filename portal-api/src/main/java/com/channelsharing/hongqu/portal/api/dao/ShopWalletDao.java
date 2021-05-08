/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.hongqu.portal.api.entity.ShopWallet;
import com.channelsharing.common.dao.CrudDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺钱包Dao接口
 * @author liuhangjun
 * @version 2018-06-26
 */
@Mapper
public interface ShopWalletDao extends CrudDao<ShopWallet> {

}
