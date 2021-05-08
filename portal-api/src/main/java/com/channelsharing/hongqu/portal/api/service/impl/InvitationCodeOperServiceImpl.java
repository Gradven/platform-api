/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.service.InvitationCodeOperService;
import com.channelsharing.hongqu.portal.api.entity.InvitationCodeOper;
import com.channelsharing.hongqu.portal.api.dao.InvitationCodeOperDao;

/**
 * 运营邀请码Service
 * @author liuhangjun
 * @version 2018-07-16
 */
@Service
public class InvitationCodeOperServiceImpl extends CrudServiceImpl<InvitationCodeOperDao, InvitationCodeOper> implements InvitationCodeOperService {

    @Override
    public InvitationCodeOper findOne(Long id) {
        InvitationCodeOper entity = new InvitationCodeOper();
        entity.setId(id);

        return super.findOne(entity);
    }


}
