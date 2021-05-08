package com.channelsharing.common.exception;


public class OccupiedException extends BaseException {
	private static final long serialVersionUID = -2342605116423305331L;
    private static final String ERROR_MESSAGE = "objectName Occupied";

    public OccupiedException(String prefix, String objectName) {
		super(prefix  + "[ "+ objectName + " ]已被占用");
        super.setError(ERROR_MESSAGE);
	}
    
    public OccupiedException(String message) {
        super(message);
        super.setError(ERROR_MESSAGE);
    }

}
