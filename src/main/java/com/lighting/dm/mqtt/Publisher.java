package com.lighting.dm.mqtt;


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Publisher {
//    public static final String HOST = "tcp://localhost:1883";
//    public static final String TOPIC = "toclient/124";
//    public static final String TOPIC125 = "toclient/125";
//    private static final String clientid = "myMqtt";

    public static final String TOPIC = "/shadow/update/{productKey}/{deviceCode}";
    private String deviceCode;
    private String productKey;


    private MqttClient client;
    private MqttTopic topic;
    private MqttTopic topic125;
//    private String userName = "root";
//    private String passWord = "root";

    private MqttMessage message;

    public Publisher(String host,String clientId,String productKey,String token) throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(host, clientId, new MemoryPersistence());
        this.deviceCode = clientId;
        this.productKey = productKey;
        connect(clientId,token);
    }

    private void connect(String userName,String passWord) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(new PushCallback());
            client.connect(options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publish(String message) throws  MqttException {
        String topicStr = TOPIC.replace("{deviceCode}",deviceCode);
        topicStr = topicStr.replace("{productKey}",productKey);
        topic = client.getTopic(topicStr);

        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(1);
        mqttMessage.setRetained(true);
        mqttMessage.setPayload(message.getBytes());
        MqttDeliveryToken token = topic.publish(mqttMessage);
        token.waitForCompletion();
        System.out.println("message is published completely! "
                + token.isComplete());
    }

//    public static void main(String[] args) throws MqttException {
//        Publisher server = new Publisher();
//
//        server.message = new MqttMessage();
//        server.message.setQos(1);
//        server.message.setRetained(true);
//        server.message.setPayload("给客户端123推送的信息".getBytes());
//        server.publish(server.topic , server.message);
//
//        server.message = new MqttMessage();
//        server.message.setQos(2);
//        server.message.setRetained(true);
//        server.message.setPayload("给客户端125推送的信息".getBytes());
//        server.publish(server.topic125 , server.message);
//
//        System.out.println(server.message.isRetained() + "------ratained状态");
//    }
}
