package com.channelsharing.common.advice;

/**
 * Created by liuhangjun on 2018/4/10.
 */
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * Created by wb-zhangkenan on 2016/12/1.
 */
@ControllerAdvice
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice{

    public JsonpAdvice() {
        super("callback");
    }
}
