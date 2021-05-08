package com.channelsharing.common.handler;


import java.util.Date;
import java.util.List;

/**
 * Created by liuhangjun on 2017/6/29.
 */
public class ErrorResponse {
    
    private Integer errorCode;
    private String message;
    private String error;
    private String exception;
    private String path;
    private Date timestamp;
    private List<ErrorInfo> errors;
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public List<ErrorInfo> getErrors() {
        return errors;
    }
    
    public void setErrors(List<ErrorInfo> errors) {
        this.errors = errors;
    }
    
    public Integer getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public String getException() {
        return exception;
    }
    
    public void setException(String exception) {
        this.exception = exception;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    

}
