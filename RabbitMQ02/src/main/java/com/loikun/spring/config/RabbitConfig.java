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
    public static final String DIRECT ="direct";


    @Bean(name = "hello")
    public Queue hello(){
        return new Queue(QUEUE_NAME,false,false,true);
    }

    @Bean("taskQueue")
    public Queue taskQueue(){
        return new Queue(TASK_QUEUE_NAME,false,false,true);
    }

    @Bean("fanout")
    public FanoutExchange fanout(){
        return new FanoutExchange(PUB_SUB);
    }

    // 采用直连的方式
    @Bean
    public DirectExchange direct(){
        return new DirectExchange(DIRECT);
    }

    // 实例化两个任务接受者
    @Bean
    public TaskReceiver receiver1(){
        return new TaskReceiver(1);
    }
    @Bean
    public TaskReceiver receiver2(){
        return new TaskReceiver(2);
    }

    /*=================订阅和发布======================*/

    @Bean
    public Queue autoDeleteQueue1() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue autoDeleteQueue2() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding binding1(FanoutExchange fanout,
                            Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1).to(fanout);
    }

    @Bean
    public Binding binding2(FanoutExchange fanout,
                            Queue autoDeleteQueue2) {
        return BindingBuilder.bind(autoDeleteQueue2).to(fanout);
    }

    /*=================路由转发======================*/
    @Bean
    public Binding binding1a(DirectExchange direct,
                            Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1)
                .to(direct)
                .with("orange");
    }

    @Bean
    public Binding binding1b(DirectExchange direct,
                            Queue autoDeleteQueue1){
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
                             Queue autoDeleteQueue1){
        return BindingBuilder.bind(autoDeleteQueue1)
                .to(direct)
                .with("black");
    }



    //// 两个处理任务的接受者
    //@Bean
    //public TaskReceiver receiver1(){
    //    return new TaskReceiver(1);
    //}

    //// 发布和订阅
    //@Bean
    //public FanoutExchange exchange(){
    //    return new FanoutExchange(PUB_SUB);
    //}
    //
    //@Bean
    //public Queue autoDeleteQueue1() {
    //    return new AnonymousQueue();
    //}
    //
    //@Bean
    //public Queue autoDeleteQueue2() {
    //    return new AnonymousQueue();
    //}
    //
    //@Bean
    //public Binding binding1(FanoutExchange fanout,
    //                        Queue autoDeleteQueue1) {
    //    return BindingBuilder.bind(autoDeleteQueue1).to(fanout);
    //}
    //
    //@Bean
    //public Binding binding2(FanoutExchange fanout,
    //                        Queue autoDeleteQueue2) {
    //    return BindingBuilder.bind(autoDeleteQueue2).to(fanout);
    //}
    //
    //@Bean
    //public ExReceiver receiver() {
    //    return new ExReceiver();
    //}



}
