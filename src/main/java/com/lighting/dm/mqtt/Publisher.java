package com.lighting.dm.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

@Slf4j
public class Publisher {
    private String deviceCode;
    private String productKey;
    private MqttClient mqttClient;
    private MqttTopic mqttTopic;

    public Publisher(String host, String clientId, String productKey, String token) throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        mqttClient = new MqttClient(host, clientId, new MemoryPersistence());
        this.deviceCode = clientId;
        this.productKey = productKey;
        connect(clientId, token);
    }

    private void connect(String userName, String passWord) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            mqttClient.setCallback(new PushCallback());
            mqttClient.connect(options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publish(String message) throws MqttException {
        String topic = "/shadow/update/{productKey}/{deviceCode}";
        this.publish(topic, message);
    }

    public void publish(String topic, String message) throws MqttException {

        String topicStr = topic.replace("{deviceCode}", deviceCode);
        topicStr = topicStr.replace("{productKey}", productKey);

        this.mqttTopic = mqttClient.getTopic(topicStr);

        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(1);
        mqttMessage.setRetained(true);
        mqttMessage.setPayload(message.getBytes());
        MqttDeliveryToken token = this.mqttTopic.publish(mqttMessage);
        token.waitForCompletion();

        log.info("message is published completely! " + token.isComplete());
    }
}
