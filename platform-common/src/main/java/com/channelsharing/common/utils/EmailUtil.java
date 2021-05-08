package com.channelsharing.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by liuhangjun on 2017/6/19.
 */
@Component
public class EmailUtil {

    @Value("${spring.mail.username}")
    private String  from;

    @Resource
    private JavaMailSender mailSender;


    public void sendSimpleMail(String sendTo, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(sendTo);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }


}
