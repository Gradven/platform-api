package com.channelsharing.hongqu.portal.api.service;


import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.hongqu.portal.api.enums.AccountType;

import java.io.IOException;
import java.util.Map;

public interface SnsAccessService {
	UserInfo getUserInfoFromThirdParty(AccountType accountType, String code, Map<String, String> userInfo)
			throws IOException;
}
