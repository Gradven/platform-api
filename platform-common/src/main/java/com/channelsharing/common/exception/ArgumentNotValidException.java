package com.channelsharing.common.exception;

/**
 * @author  liuhangjun
 * @since 2017-6-30
 */
public class ArgumentNotValidException extends BaseException {
	private static final long serialVersionUID = -364994139603740281L;

	private static final String ERROR_MESSAGE = "Argument not valid";

    public ArgumentNotValidException() {
        super("请求参数不正确");
        super.setError(ERROR_MESSAGE);
    }

	public ArgumentNotValidException(String message) {
		super(message);
        super.setError(ERROR_MESSAGE);
	}


}
