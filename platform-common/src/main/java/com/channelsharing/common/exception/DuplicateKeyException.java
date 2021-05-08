package com.channelsharing.common.exception;

/**
 * @author liuhangjun
 */
public class DuplicateKeyException extends BaseException {

    private static final long serialVersionUID = -4401587899700166317L;

    private static final String ERROR_MESSAGE = "Duplicate Key request";

    public DuplicateKeyException() {
        super("请求信息key值重复");
    }

	public DuplicateKeyException(String message) {
		super(message);
		super.setError(ERROR_MESSAGE);
	}


}
