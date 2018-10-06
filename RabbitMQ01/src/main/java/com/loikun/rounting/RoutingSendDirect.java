package com.loikun.rounting;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RoutingSendDirect {
    private static final String EXCHANGE_NAME = "direct_logs";
    private static final String[] rountingKeys = new String[]{"INFO","WARNING","ERROR","DEBUG"};

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明采用direct模式的交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        for (String routingKey : rountingKeys) {
            String message = "RountingSendDirect send the message level : " + routingKey;
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            System.out.println("RoutingSendDirect Send"+routingKey +"':'" + message);
        }
        channel.close();
        connection.close();
    }
}
