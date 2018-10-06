package com.loikun.rpc;


import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * 客户端总体思路：
 * 1、建立连接和通道，声明一个唯一的回调队列
 * 2、订阅回调队列
 * @author loikun
 */
public class RPCClient implements AutoCloseable {
    private Connection connection;
    private Channel channel;
    private static final String REQUEST_QUEUE_NAME = "rpc_queue";
    /**
     * 声明唯一的回调队列的应答（通知）
     */
    private String replyQueueName;

    /**
     * 初始化操作
     */
    public RPCClient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        connection = factory.newConnection();
        channel = connection.createChannel();
        // 获取匿名队列
        replyQueueName = channel.queueDeclare().getQueue();
    }

    /**
     * 用于 发送当前的回调请求
     * @param message 需要发送的消息
     */
    public String call(String message) throws IOException, InterruptedException {
        // 用UUID 生成唯一的关联id，其作用用于表示返回的结果是唯一的。
        final String corrId = UUID.randomUUID().toString();
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();
        channel.basicPublish("", REQUEST_QUEUE_NAME, props, message.getBytes("utf-8"));
        // 生产者和消费者模型的需要，采用阻塞队列，从而保证生产者往里面增加人物，而消费者从线程池里面不断取出任务进行消费。
        // 线程安全的队列，因为当前是多线程的环境下。
        final BlockingQueue<String> response = new ArrayBlockingQueue<>(20);

        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body, "utf-8"));
                }
            }
        });
        // take 方法阻塞线程
        return response.take();
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    public static void main(String[] args) {
        try (RPCClient fibonacciRpc = new RPCClient()) {
            System.out.println(" [x] Requesting fib(30)");
            final String response = fibonacciRpc.call("30");
            System.out.println(" [.] Got '" + response + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
