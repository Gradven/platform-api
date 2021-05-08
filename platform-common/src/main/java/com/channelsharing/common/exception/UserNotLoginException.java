package com.channelsharing.common.exception;


import com.channelsharing.common.enums.CustomHttpStatus;

/**
 * @author liuhangjun
 */
public class UserNotLoginException extends BaseException {

    private static final long serialVersionUID = 2941399378347570694L;
    private static final String ERROR_MESSAGE = "User not login";

    public UserNotLoginException() {
		super("请先登录");
        super.setStatus(CustomHttpStatus.USER_NOT_LOGIN);
        super.setError(ERROR_MESSAGE);
	}

}
