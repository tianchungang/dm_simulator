
package com.lighting.dm.mqtt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DmMqttRegisterMetaVo {
    private String password;
    private String mqttEndpoint;
}
