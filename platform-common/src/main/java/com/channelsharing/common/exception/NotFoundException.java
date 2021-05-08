package com.channelsharing.common.exception;

public class NotFoundException extends BaseException {
	private static final long serialVersionUID = 4860396917402779906L;
    private static final String ERROR_MESSAGE = "Not found";

    public NotFoundException() {
        super("没有找到服务");
        super.setError(ERROR_MESSAGE);
    }

	public NotFoundException(String message) {
		super(message);
        super.setError(ERROR_MESSAGE);
	}

}
