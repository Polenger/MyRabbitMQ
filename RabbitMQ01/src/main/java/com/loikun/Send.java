package com.loikun;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {
    // 队列名称
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        while (true) {
            /**
             * 创建连接到rabbitmq
             *
             */
            ConnectionFactory factory = new ConnectionFactory();
            // 设置MQ 所在的主机ip 或者主机名
            factory.setHost("localhost");
            // 创建一个连接
            Connection connection = factory.newConnection();
            // 开辟一个频道
            Channel channel = connection.createChannel();
            // 指定一个消息队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // 要发送的消息
            String message = "helloworld";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            System.out.println("发送的消息是 ： " + message);
            // 关闭频道
            channel.close();
            // 关闭连接
            connection.close();
            Thread.sleep(1000);
        }
    }
}
