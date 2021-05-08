package com.channelsharing.common.exception;


import com.channelsharing.common.enums.CustomHttpStatus;

public class CacheLockException extends BaseException {
	private static final long serialVersionUID = -364994139603740281L;

    private static final String ERROR_MESSAGE = "Cache lock error!";

    public CacheLockException() {
        super("缓存锁定失败");
    }

	public CacheLockException(String message) {
		super(message);
		super.setStatus(CustomHttpStatus.REQUEST_DUPLICATE);
		super.setError(ERROR_MESSAGE);
	}


}
