package com.loikun;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Receive {
    // 队列名称
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        // 设置MQ 所在的主机ip 或者主机名
        factory.setHost("localhost");
        // 创建一个连接
        Connection connection = factory.newConnection();
        // 开辟一个频道
        Channel channel = connection.createChannel();
        //声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // 创建消费队列
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"utf-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
