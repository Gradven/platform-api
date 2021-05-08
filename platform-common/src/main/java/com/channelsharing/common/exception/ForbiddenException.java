package com.channelsharing.common.exception;


public class ForbiddenException extends BaseException {
	private static final long serialVersionUID = 2685478916858032894L;

    private static final String ERROR_MESSAGE = "Forbidden request";

    public ForbiddenException() {
        super("禁止访问");
        super.setError(ERROR_MESSAGE);
    }

	public ForbiddenException(String message) {
		super(message);
        super.setError(ERROR_MESSAGE);
	}

}
