package com.tensquare.sms.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "yamiMES")
public class SmsListener {
    @RabbitHandler
    public void sendSms(Map<String, String> messagehMap){
        String mobile = messagehMap.get("mobile");
        String code = messagehMap.get("code");
        System.out.println("手机号："+mobile);
        System.out.println("验证码："+code);

    }
}
