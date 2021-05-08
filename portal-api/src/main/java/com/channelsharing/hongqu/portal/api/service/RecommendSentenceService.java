/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.portal.api.entity.RecommendSentence;

import java.util.List;


/**
 * 商品运营推荐语Service
 * @author liuhangjun
 * @version 2018-07-23
 */
public interface RecommendSentenceService extends CrudService<RecommendSentence>{
    List<RecommendSentence> findList();

}
