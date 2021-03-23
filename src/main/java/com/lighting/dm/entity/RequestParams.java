package com.lighting.dm.entity;

import lombok.Data;

import java.util.Map;
@Data
public class RequestParams {
    private String deviceCode;
    private String productKey;
    private String deviceSecret;
    private String topic;
    private String payload;
    private String serverUrl;
    private String arrayFields;
    private Map<String,Object> data;
}
