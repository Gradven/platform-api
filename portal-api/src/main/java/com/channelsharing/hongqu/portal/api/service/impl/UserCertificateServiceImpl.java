/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import java.io.IOException;

import com.channelsharing.hongqu.portal.api.enums.Side;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.channelsharing.cloud.aliyun.ocr.IdCardRecognizeUtil;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.dao.UserCertificateDao;
import com.channelsharing.hongqu.portal.api.entity.UserCertificate;
import com.channelsharing.hongqu.portal.api.enums.UserSex;
import com.channelsharing.hongqu.portal.api.service.UserCertificateService;

/**
 * 用户认证信息Service
 *
 * @author liuhangjun
 * @version 2018-07-12
 */
@Service
public class UserCertificateServiceImpl extends CrudServiceImpl<UserCertificateDao, UserCertificate>
		implements UserCertificateService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public UserCertificate findOne(Long id) {
		UserCertificate entity = new UserCertificate();
		entity.setId(id);

		return super.findOne(entity);
	}

	@Override
	public void add(UserCertificate entity) {
		this.recognizeIdCard(entity);
		dao.insert(entity);
	}

	@Override
	public void modify(UserCertificate entity) {
		this.recognizeIdCard(entity);
		dao.update(entity);
	}

	private void recognizeIdCard(UserCertificate entity) {
		String realName, idCard, address, sex, birth, nationality, startDate, endDate, issue;

		try {
			JSONObject face = IdCardRecognizeUtil.recognize(entity.getCardFront(), Side.face.name());
			if (face.getBooleanValue("success")) {
				realName = face.getString("name");
				idCard = face.getString("num");
				address = face.getString("address");
				sex = face.getString("sex");
				birth = face.getString("birth");
				nationality = face.getString("nationality");
			} else {
				throw new SystemInnerBusinessException("身份证正面识别失败，请重新拍摄");
			}

			JSONObject back = IdCardRecognizeUtil.recognize(entity.getCardBack(), Side.back.name());
			if (back.getBooleanValue("success")) {
				startDate = back.getString("start_date");
				endDate = back.getString("end_date");
				issue = back.getString("issue");
			} else {
				throw new SystemInnerBusinessException("身份证背面识别失败，请重新拍摄");
			}

			entity.setAddress(address);
			entity.setSex("男".equals(sex) ? UserSex.male.getCode() : UserSex.female.getCode());
			entity.setBirth(birth);
			entity.setNationality(nationality);
			entity.setStartDate(startDate);
			entity.setEndDate(endDate);
			entity.setIssue(issue);

			if (StringUtils.equals(entity.getRealName(), realName) && StringUtils.equals(entity.getIdCard(), idCard)) {
				entity.setApproveStatus(1);
			} else {
				entity.setApproveStatus(2);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new SystemInnerBusinessException("网络异常，识别身份证失败");
		}
	}
}
