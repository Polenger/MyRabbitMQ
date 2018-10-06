package com.loikun.rounting;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect1 {
    private static final String EXCHANGE_NAME = "direct_logs";

    private static final String[] routingKeys = new String[]{"error", "debug"};

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        // 获取队列名称
        String queue = channel.queueDeclare().getQueue();
        // 根据路由器关键字进行绑定
        // 怎么绑定？一次遍历
        for (String routingKey : routingKeys) {
            channel.queueBind(queue, EXCHANGE_NAME, routingKey);
            //
            System.out.println("ReceiveLogsDirect1 exchange:" + EXCHANGE_NAME + ","
                    + "queue:" + queue + ", BindRoutingKey:" + routingKey);
        }
        System.out.println("ReceiveLogsDirect1 Wating for message");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                System.out.println("ReceiveLogsDirect1 Received '"
                        + envelope.getRoutingKey() + "':'" + new String(body, "UTF-8") + "'");
            }
        };
        channel.basicConsume(queue, true, consumer);

    }
}
