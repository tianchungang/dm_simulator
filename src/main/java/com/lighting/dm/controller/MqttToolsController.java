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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.*;

@RestController
public class MqttToolsController {
    @RequestMapping(value = "/api/getData", method = RequestMethod.POST)
    @ResponseBody
    public String getData(String serverUrl, String deviceCode, String productId, String step) throws Exception {

        serverUrl = getServerUrl(serverUrl);
        if (step.equals(RequestPair.LIST.getType())) {
            serverUrl += RequestPair.LIST.getUrl();
            serverUrl = MessageFormat.format(serverUrl, productId);
        } else if (step.equals(RequestPair.SINGLE.getType())) {
            serverUrl += RequestPair.SINGLE.getUrl();
            serverUrl = MessageFormat.format(serverUrl, deviceCode, productId);
        } else if (step.equals(RequestPair.THING_MODEL.getType())) {
            serverUrl += RequestPair.THING_MODEL.getUrl();
            serverUrl = MessageFormat.format(serverUrl, deviceCode);
        } else if (step.equals(RequestPair.PRODUCT.getType())) {
            serverUrl += RequestPair.PRODUCT.getUrl();
            serverUrl = MessageFormat.format(serverUrl, deviceCode);
        }
        String responseStr = HttpUtils.getHttps(serverUrl);
        System.out.println(responseStr);
        return responseStr;
    }

    @RequestMapping(value = "/api/publish", method = RequestMethod.POST)
    @ResponseBody
    public String publish(@RequestBody RequestParams requestParams) throws Exception {

        String serverUrl = getServerUrl(requestParams.getServerUrl());

        serverUrl += Constants.DEVICE_AUTH_URL;

        HashMap dataMap = new HashMap(2);
        String deviceCode = requestParams.getDeviceCode();
        String arrayFields = requestParams.getArrayFields();
        String deviceSecret = requestParams.getDeviceSecret();
        String productKey = requestParams.getProductKey();
        dataMap.put("deviceCode", deviceCode);
        dataMap.put("secret", deviceSecret);

        String json = HttpUtils.postJsonHttps(serverUrl, new Gson().toJson(dataMap));
        if (json.contains("\"status\":404")) {
            throw new RuntimeException("Error logging into MQTT server, please check account password");
        }

        DmMqttRegisterMetaVo dmMqttRegisterMetaVo = new Gson().fromJson(json, DmMqttRegisterMetaVo.class);
        String token = dmMqttRegisterMetaVo.getPassword();
        String endPoint = dmMqttRegisterMetaVo.getMqttEndpoint();

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
            if (data.get(s) == null || StringUtils.isEmpty(data.get(s))) {
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
        return json;
    }

    private String getServerUrl(String serverUrl) {
        if (!serverUrl.startsWith("http")) {
            serverUrl = "https://" + serverUrl;
        }
        return serverUrl;
    }

    public static void main(String[] args) {
        final byte[] encode = Base64.getEncoder().encode("guest:guest".getBytes());
        try {
            System.out.println("encode = " + new String(encode, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
