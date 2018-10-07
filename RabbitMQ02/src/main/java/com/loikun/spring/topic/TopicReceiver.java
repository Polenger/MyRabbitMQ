package com.loikun.spring.topic;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class TopicReceiver {

    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receiver1(String in) throws InterruptedException {
        receive(in, 1);
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void receiver2(String in) throws InterruptedException {
        receive(in, 2);
    }

    public void receive(String in, int receiver) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        System.out.println("instance "+ receiver +"[x] Received '"+in+"'");
        doWork(in);
        watch.start();
        System.out.println("instance " + receiver + " [x] Done in "
                + watch.getTotalTimeSeconds() + "s");
    }

    private void doWork(String in) throws InterruptedException {
        for (char ch: in.toCharArray()){
            if (ch =='.'){
                Thread.sleep(1000);
            }
        }
    }


}
