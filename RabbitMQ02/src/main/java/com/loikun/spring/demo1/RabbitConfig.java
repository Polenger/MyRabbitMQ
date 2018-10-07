package com.loikun.spring.demo1;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    /**
     * 定义一个队列： 名字hello
     */
    @Bean
    public Queue hello(){
        return new Queue("hello");
    }


}
