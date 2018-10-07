package com.loikun.spring.config;

import com.loikun.spring.task.TaskReceiver;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    // hello
    public final static String QUEUE_NAME = "hello";
    // 工作队列
    public static final String TASK_QUEUE_NAME = "task_queue";
    // 发布和订阅
    public static final String PUB_SUB = "pub-sub";
    // 直连路由
    public static final String DIRECT = "direct";
    // 配置多个主题
    public static final String TOPIC = "topic";


    @Bean(name = "hello")
    public Queue hello() {
        return new Queue(QUEUE_NAME, false, false, true);
    }

    @Bean("taskQueue")
    public Queue taskQueue() {
        return new Queue(TASK_QUEUE_NAME, false, false, true);
    }

    @Bean("fanout")
    public FanoutExchange fanout() {
        return new FanoutExchange(PUB_SUB,false,true);
    }

    // 采用直连的方式
    @Bean
    public DirectExchange direct() {
        return new DirectExchange(DIRECT, false,true);
    }

    @Bean
    public TopicExchange topic(){
        return new TopicExchange(TOPIC, false, true);
    }

    /*=================任务执行者======================*/
    @Bean
    public TaskReceiver receiver1() {
        return new TaskReceiver(1);
    }

    @Bean
    public TaskReceiver receiver2() {
        return new TaskReceiver(2);
    }

    /*=================创建两个匿名队列======================*/
    @Bean
    public Queue autoDeleteQueue1() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue autoDeleteQueue2() {
        return new AnonymousQueue();
    }

    /*=================订阅和发布进行绑定======================*/
    /*=================fanout 方式======================*/
    @Bean
    public Binding binding1(FanoutExchange fanout,
                            Queue autoDeleteQueue1) {
        // 绑定匿名队列， 指定方式为fanout
        return BindingBuilder.bind(autoDeleteQueue1).to(fanout);
    }

    @Bean
    public Binding binding2(FanoutExchange fanout,
                            Queue autoDeleteQueue2) {
        return BindingBuilder.bind(autoDeleteQueue2).to(fanout);
    }

    /*================= 路由 ======================*/
    /*=================direct 方式======================*/
    @Bean
    public Binding binding1a(DirectExchange direct,
                             Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1)
                .to(direct)
                .with("orange");
    }

    @Bean
    public Binding binding1b(DirectExchange direct,
                             Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1)
                .to(direct)
                .with("black");
    }

    @Bean
    public Binding binding2a(DirectExchange direct,
                             Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1)
                .to(direct)
                .with("green");
    }

    @Bean
    public Binding binding2b(DirectExchange direct,
                             Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1)
                .to(direct)
                .with("black");
    }

    /*================= Topic  ======================*/
    @Bean
    public Binding bindingA(TopicExchange topic,
                             Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1)
                .to(topic)
                .with("red.#");
    }

    @Bean
    public Binding bindingB(TopicExchange topic,
                             Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1)
                .to(topic)
                .with("*.green.*");
    }

    @Bean
    public Binding bindingC(TopicExchange topic,
                            Queue autoDeleteQueue2) {
        return BindingBuilder.bind(autoDeleteQueue2)
                .to(topic)
                .with("*.*.blue");
    }


}
