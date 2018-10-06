package com.loikun.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 服务器总体思路：
 * 建立连接、通道、远程通信队列
 * 考虑需要运行多个服务器进程，为了分散负载服务器压力，设置channel.basicQos(1);
 * 使用basicConsume访问队列，进入循环，在循环中等待请求并处理消息然后发送响应队列。
 */
public class RPCServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    private static int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fib(n - 1) + fib(n - 2);
    }

    public static void main(String[] args) {
        // 默认主机用本地
        ConnectionFactory factory = new ConnectionFactory();

        try (Connection connection =factory.newConnection();
             Channel channel = connection.createChannel()){
            // 声明一个队列
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            channel.basicQos(1);
            System.out.println("[x] Awaiting rpc requests");
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    AMQP.BasicProperties replyProps
                            = new AMQP.BasicProperties
                            .Builder()
                            .correlationId(properties.getCorrelationId())
                            .build();

                    String response = "";
                    try{
                        String message = new String(body, "utf-8");
                        int n = Integer.parseInt(message);
                        System.out.println(" [.] fib(" + message + ")");

                        response += fib(n);
                    }catch (RuntimeException e){

                    }finally {
                        channel.basicPublish("", properties.getReplyTo(),replyProps, response.getBytes("utf-8"));
                        channel.basicAck(envelope.getDeliveryTag(),false);
                        synchronized (this){
                            this.notify();
                        }
                    }

                }
            };
            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
            // 等待和准备消费来自客户端的信息
            while(true){
                synchronized (consumer){
                    try {
                        consumer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch(IOException | TimeoutException e){
            e.printStackTrace();
        }
    }
}
