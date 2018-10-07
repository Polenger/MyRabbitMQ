package com.loikun.spring.task;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("task")
public class TaskSender {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    @Qualifier("taskQueue")
    private Queue queue;

    int dots = 0;
    int count = 0;

    @GetMapping("send")
    public void send(){
        StringBuilder builder = new StringBuilder("Hello");
        if (dots++ == 3) {
            dots = 1;
        }
        for (int i = 0; i < dots; i++) {
            builder.append('.');
        }
        builder.append(Integer.toString(++count));
        String message = builder.toString();
        template.convertAndSend(queue.getName(), message);
        System.out.println(getClass().getSimpleName() + "  [x] Sent '" + message + "'");
    }

}
