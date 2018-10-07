package com.loikun.spring.topic;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("topic")
public class TopicSender {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private TopicExchange topic;

    private int index, count;

    private final String[] keys ={"red.11.12","13.green.14",
            "15.16.blue","red.green.blue"};

    @GetMapping("send")
    public void send(){
        StringBuilder builder = new StringBuilder("消息 ： ");
        if (++ this.index == keys.length) {
            this.index = 0;
        }
        String key = keys[this.index];
        builder.append(key).append(" ");
        builder.append(Integer.toString(++ this.count));
        String message = builder.toString();
        template.convertAndSend(topic.getName(), key, message);
        System.out.println(" [x] Sent '" + message + "'");
    }
}
