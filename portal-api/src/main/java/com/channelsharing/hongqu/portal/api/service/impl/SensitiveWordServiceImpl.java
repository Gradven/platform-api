/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;


import com.channelsharing.hongqu.portal.api.entity.SensitiveWord;
import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.dao.SensitiveWordDao;
import com.channelsharing.hongqu.portal.api.service.SensitiveWordService;
import org.springframework.stereotype.Service;

/**
 * 敏感词信息Service
 * @author liuhangjun
 * @version 2017-07-10
 */
@Service
public class SensitiveWordServiceImpl extends CrudServiceImpl<SensitiveWordDao, SensitiveWord> implements SensitiveWordService {
    
    @Override
    public SensitiveWord findOne(Long id) {
        SensitiveWord entity = new SensitiveWord();
        entity.setId(id);
        
        return super.findOne(entity);
    }
}
