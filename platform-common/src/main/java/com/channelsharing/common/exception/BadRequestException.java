package com.channelsharing.common.exception;


public class BadRequestException extends BaseException {
	private static final long serialVersionUID = -364994139603740281L;

    private static final String ERROR_MESSAGE = "Bad request";

    public BadRequestException() {
        super("请求错误");
    }

	public BadRequestException(String message) {
		super(message);
		super.setError(ERROR_MESSAGE);
	}


}
