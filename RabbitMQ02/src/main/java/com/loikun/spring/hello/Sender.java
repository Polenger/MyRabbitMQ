package com.loikun.spring.hello;

import com.loikun.spring.config.RabbitConfig;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Sender {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    @Qualifier(value = RabbitConfig.QUEUE_NAME)
    private Queue queue;

    @GetMapping("/send")
    public Map<String, Object> send(){
        Map<String, Object> map = new HashMap<>();
        // 需要发送的消息
        String message="Hello world";
        this.template.convertAndSend(queue.getName(), message);
        System.out.println(" [x] Sent '" + message + "'");
        map.put("data",message);
        return map;
    }
}
