package com.totoro.listener;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: totoro
 * @createDate: 2023 12 11 17 24
 * @description:
 **/
@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {

    @Resource
    JavaMailSender sender;

    @Value("${spring.mail.username}")
    String username;

    @RabbitHandler
    public void sendMailMessage(Map<String, Object> data){
        String email = data.get("email").toString();
        Integer code = (Integer) data.get("code");
        String type = data.get("type").toString();

        SimpleMailMessage message = switch (type){
            case "register" -> createMessage("欢迎注册"
                    ,"你的邮件注册码为："+code+",有效时间三分钟，为了保障您的安全，请勿向他人泄露。"
            ,email);
            case "reset" -> createMessage("您的密码重置邮件"
                    ,"您好，您正在进行重置密码操作，验证码："+code+",有效时间三分钟，为了保障您的安全，请勿向他人泄露。"
                    ,email);
            default -> null;
        };
        if (message == null){
            return;
        }

        sender.send(message);
    }

    private SimpleMailMessage createMessage(String title, String content, String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(content);
        message.setTo(email);
        message.setFrom(username);
        return message;
    }

}
