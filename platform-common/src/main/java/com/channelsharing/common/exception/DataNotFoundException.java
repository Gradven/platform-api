package com.channelsharing.common.exception;

/**
 * @author liuhangjun
 */
public class DataNotFoundException extends BaseException {

    private static final long serialVersionUID = -5344291495882163217L;

    private static final String ERROR_MESSAGE = "Data Not found";

    public DataNotFoundException() {
        super("没有找到数据");
        super.setError(ERROR_MESSAGE);
    }

	public DataNotFoundException(String message) {
		super(message);
        super.setError(ERROR_MESSAGE);
	}

}
