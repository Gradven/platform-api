package com.channelsharing.common.handler;

import com.channelsharing.common.exception.BaseException;
import com.channelsharing.common.utils.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 对excepion对json格式返回格式统一处理
 * Created by liuhangjun on 2017/6/30.
 */
@ControllerAdvice
@EnableWebMvc
public class RestExceptionHandler  {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * 将hibernate对校验json返回，进行简化
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(HttpServletRequest request, MethodArgumentNotValidException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(HttpStatus.BAD_REQUEST.name());
        errorResponse.setTimestamp(new Date());
        errorResponse.setError("valid argument error");
        errorResponse.setException(ex.getClass().getName());
        errorResponse.setPath(request.getServletPath());
        
        BindingResult result = ex.getBindingResult();
    
        List<ObjectError> objectErrorList  = result.getAllErrors();
        List<ErrorInfo> errorInfoList = this.objectError2ErrorInfo(objectErrorList);
        errorResponse.setErrors(errorInfoList);
    
        ResponseEntity<ErrorResponse> responseEntity = ResponseEntity.ok().body(errorResponse);
    
        logger.error(JacksonUtil.toJSon(errorResponse));
        
        return responseEntity;
    }
    
    /**
     * 将系统ObjectError转换为工程定义的ErrorInfo
     * @param objectErrorList
     * @return
     */
    private List<ErrorInfo> objectError2ErrorInfo(List<ObjectError> objectErrorList){
    
        List<ErrorInfo> errorInfoList = new ArrayList<ErrorInfo>();
        
        for (ObjectError objectError : objectErrorList){
            ErrorInfo errorInfo = new ErrorInfo();
            BeanUtils.copyProperties(objectError, errorInfo);
    
            errorInfoList.add(errorInfo);
        }
        
        return errorInfoList;
        
    }
    
    /**
     * 将系统抛出对异常按照简化对hibernate格式进行处理
     * @param request
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<ErrorResponse> defaultExceptionHandler(HttpServletRequest request, BaseException ex) throws Exception {
        
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null)
            throw ex;
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(ex.getStatus().getCode());
        errorResponse.setMessage(ex.getStatus().getName());
        errorResponse.setTimestamp(new Date());
        errorResponse.setError(ex.getError());
        errorResponse.setException(ex.getClass().getName());
        errorResponse.setPath(request.getServletPath());
        
        ErrorInfo errors = new ErrorInfo();
        errors.setDefaultMessage(ex.getMessage());
        List<ErrorInfo> list = new ArrayList<ErrorInfo>();
        list.add(errors);
        errorResponse.setErrors(list);
        
        ResponseEntity<ErrorResponse> responseEntity = ResponseEntity.ok().body(errorResponse);
        
        logger.error(JacksonUtil.toJSon(errorResponse));
        
        return responseEntity;
    }
    
    
}
