package com.channelsharing.common.exception;

/**
 * Created by liuhangjun on 2018/2/7.
 */
public class SystemInnerBusinessException extends BaseException{
    private static final String ERROR_MESSAGE = "System inner business error";

    public SystemInnerBusinessException() {
        super("系统内部业务逻辑错误");
        super.setError(ERROR_MESSAGE);
    }

    public SystemInnerBusinessException(String message) {
        super(message);
        super.setError(ERROR_MESSAGE);
    }
}
