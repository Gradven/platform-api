package com.channelsharing.common.binder;

/**
 * Created by liuhangjun on 2018/3/12.
 */
import com.channelsharing.common.exception.BadRequestException;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class DateEditor extends PropertyEditorSupport {

    private static ThreadLocal<SimpleDateFormat> DateFormaterHolder = ThreadLocal
            .withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
    private static ThreadLocal<SimpleDateFormat> TimeFormaterHolder = ThreadLocal
            .withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    private final Logger logger = Logger.getLogger(getClass());

    /**
     * 日期标准化输出 ：yyyy-MM-dd HH:mm:ss
     */
    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        return (value != null ? TimeFormaterHolder.get().format(value) : "");
    }

    /**
     * 入参String转换为Date 可接受：[yyyy-MM-dd、yyyy-MM-dd HH:mm:ss、毫秒数]
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isEmpty(text)) {
            setValue(null);
        } else {
            try {
                if (text.contains("-")) {
                    if (text.contains(":")) {
                        setValue(TimeFormaterHolder.get().parse(text));
                    } else {
                        setValue(DateFormaterHolder.get().parse(text));
                    }
                } else if (text.length() == 13) {
                    setValue(Date.from(Instant.ofEpochMilli(Long.valueOf(text))));
                } else {
                    throw new BadRequestException("可接受时间格式[yyyy-MM-dd、yyyy-MM-dd HH:mm:ss、毫秒数],异常数据：" + text);
                }
            } catch (ParseException e) {
                logger.error("可接受时间格式[yyyy-MM-dd、yyyy-MM-dd HH:mm:ss、毫秒数],时间格式异常,异常数据：" + text, e);
                throw new BadRequestException("可接受时间格式[yyyy-MM-dd、yyyy-MM-dd HH:mm:ss、毫秒数],时间格式异常,异常数据：" + text);
            }
        }
    }
}
