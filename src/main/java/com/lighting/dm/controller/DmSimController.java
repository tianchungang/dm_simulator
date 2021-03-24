package com.lighting.dm.controller;

import com.google.gson.Gson;
import com.lighting.dm.entity.DeviceAttribute;
import com.lighting.dm.entity.DeviceAttributeData;
import com.lighting.dm.entity.RequestParams;
import com.lighting.dm.mqtt.DmMqttRegisterMetaVo;
import com.lighting.dm.mqtt.Publisher;
import com.lighting.dm.utils.Constants;
import com.lighting.dm.utils.HttpUtils;
import com.lighting.dm.utils.RequestPair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Slf4j
@RestController
public class DmSimController {
    @Autowired
    private HttpServletRequest request;

    @PostMapping(value = "/api/getData")
    public String getData(String serverUrl, String deviceCode, String productId, String step) throws Exception {
        serverUrl = getServerUrl(serverUrl);

        if (step.equals(RequestPair.PRODUCTS.getStep())) {
            serverUrl = serverUrl + RequestPair.PRODUCTS.getUrl() + "&deviceCode=" + deviceCode;
        }
        if (step.equals(RequestPair.DEVICES.getStep())) {
            serverUrl = serverUrl + RequestPair.DEVICES.getUrl() + "&productId=" + productId;
        }

        if (step.equals(RequestPair.SINGLE.getStep())) {
            serverUrl = serverUrl + RequestPair.SINGLE.getUrl() + "&deviceCode=" + deviceCode + "&productId=" + productId;
        }

        if (step.equals(RequestPair.THING_MODEL.getStep())) {
            serverUrl = serverUrl + RequestPair.THING_MODEL.getUrl() + "&deviceCode=" + deviceCode;
        }

        if (step.equals(RequestPair.TOPICS.getStep())) {
            String productKey = request.getParameter("productKey");
            serverUrl = serverUrl + RequestPair.TOPICS.getUrl() + "&productId=" + productId + "&productKey=" + productKey + "&deviceCode=" + deviceCode;
        }

        if (step.equals(RequestPair.CUSTOM_API.getStep())) {
            String apiUrl = request.getParameter("apiUrl");
            serverUrl += apiUrl;
        }

        String responseStr = HttpUtils.getHttps(serverUrl);
        log.info(responseStr);
        return responseStr;
    }

    @PostMapping(value = "/api/publish")
    public String publish(@RequestBody RequestParams requestParams) throws Exception {

        String serverUrl = getServerUrl(requestParams.getServerUrl());

        serverUrl += Constants.DEVICE_AUTH;

        HashMap dataMap = new HashMap(2);

        String arrayFields = requestParams.getArrayFields();
        String productKey = requestParams.getProductKey();

        String topic = requestParams.getTopic();
        String payload = requestParams.getPayload();

        String deviceCode = requestParams.getDeviceCode();
        String deviceSecret = requestParams.getDeviceSecret();

        dataMap.put("deviceCode", deviceCode);
        dataMap.put("secret", deviceSecret);

        String json = HttpUtils.postJsonHttps(serverUrl, new Gson().toJson(dataMap));
        if (json.contains("\"status\":404")) {
            throw new RuntimeException("Error logging into MQTT server, please check account password");
        }

        DmMqttRegisterMetaVo dmMqttRegisterMetaVo = new Gson().fromJson(json, DmMqttRegisterMetaVo.class);
        String token = dmMqttRegisterMetaVo.getPassword();
        String endPoint = dmMqttRegisterMetaVo.getMqttEndpoint();

        if (StringUtils.isNotEmpty(topic)) {
            new Publisher(endPoint, deviceCode, productKey, token).publish(topic, payload);
        } else {
            Map<String, Object> data = requestParams.getData();
            DeviceAttributeData deviceAttributeData = new DeviceAttributeData();
            deviceAttributeData.setIotId(deviceCode);
            deviceAttributeData.setDeviceSecret(deviceSecret);
            deviceAttributeData.setProductKey(productKey);
            deviceAttributeData.setUtcTime(System.currentTimeMillis());

            List<String> arrayFieldList = List.of(arrayFields.split(","));

            List<DeviceAttribute> items = new ArrayList<>();
            for (String s : data.keySet()) {
                DeviceAttribute deviceAttribute = new DeviceAttribute();
                deviceAttribute.setCode(s);
                deviceAttribute.setName(s);
                if (data.get(s) == null || StringUtils.isEmpty(data.get(s) + "")) {
                    continue;
                }
                //list
                if (arrayFieldList.contains(s)) {
                    deviceAttribute.setValue(List.of(((String) data.get(s)).split(",")));
                } else {
                    deviceAttribute.setValue(data.get(s));
                }
                items.add(deviceAttribute);
            }
            deviceAttributeData.setItems(items);
            new Publisher(endPoint, deviceCode, productKey, token).publish(new Gson().toJson(deviceAttributeData));
        }
        return json;
    }

    private String getServerUrl(String serverUrl) {
        if (!serverUrl.startsWith("http")) {
            serverUrl = "https://" + serverUrl;
        }
        while (serverUrl.endsWith("/")) {
            serverUrl = serverUrl.substring(0, serverUrl.length() - 1);
        }
        return serverUrl;
    }
}
