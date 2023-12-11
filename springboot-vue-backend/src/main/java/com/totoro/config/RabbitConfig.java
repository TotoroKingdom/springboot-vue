package com.totoro.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author: totoro
 * @createDate: 2023 12 11 17 09
 * @description:
 **/
@Configuration
public class RabbitConfig {

    @Bean("emailQueue")
    public Queue emailQueue(){
        return QueueBuilder
                .durable("mail")
                .build();
    }
}
