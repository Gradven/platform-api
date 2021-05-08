package com.channelsharing.common.exception;

public class UnauthorizedException extends BaseException {

	private static final long serialVersionUID = -2712499790132482548L;
    private static final String ERROR_MESSAGE = "Unauthorized";

    public UnauthorizedException() {
		super("没有对应权限");
        super.setError(ERROR_MESSAGE);
	}

}
