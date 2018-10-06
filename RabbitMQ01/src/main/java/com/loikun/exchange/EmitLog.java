package com.loikun.exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLog {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception{
        // 默认是本地主机
        ConnectionFactory factory = new ConnectionFactory();
        /* 设置主机 factory.setHost("localhost"); */
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // fanout表示分发，所有的消费者得到同样的队列信息
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        for (int i= 0; i<5; i++){
            String message = "hello world"+i;
            channel.basicPublish(EXCHANGE_NAME,"", null,message.getBytes());
            System.out.println("EmitLog Sent '" + message + "'");
        }
        channel.close();
        connection.close();
    }
}
