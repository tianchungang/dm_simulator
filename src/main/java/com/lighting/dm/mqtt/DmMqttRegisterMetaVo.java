
package com.lighting.dm.mqtt;


public class DmMqttRegisterMetaVo {
    private String password;
    private String mqttEndpoint;


    public DmMqttRegisterMetaVo(String password, String mqttEndpoint) {
        this.password = password;
        this.mqttEndpoint = mqttEndpoint;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMqttEndpoint() {
        return mqttEndpoint;
    }

    public void setMqttEndpoint(String mqttEndpoint) {
        this.mqttEndpoint = mqttEndpoint;
    }
}
