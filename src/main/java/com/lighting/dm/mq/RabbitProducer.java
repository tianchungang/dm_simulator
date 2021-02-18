package com.lighting.dm.mq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName RabbitProducer.java
 * @Author Tom.Tian
 * @Description RabbitProducer
 * @createTime 2021-01-25 15:00:23
 */
public class RabbitProducer {
    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routingkey_demo/abc";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "106.14.106.194";
    private static final int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("user1");
        factory.setPassword("user1");
        //创建连接
        Connection connection = factory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();

        //创建一个type="direct"、持久化的、非自动删除的交换器
        HashMap<String, Object> map = new HashMap<>();
        map.put("x-queue-type", "classic");
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
//创建一个持久化、非排他的、非自动删除的队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, map);
//将交换器与队列通过路由键绑定
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
//发送一条持久化的消息：hello world！
        String message = "Hello World,This is a test value!";
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
//关闭资源
        channel.close();
        connection.close();
    }
}
