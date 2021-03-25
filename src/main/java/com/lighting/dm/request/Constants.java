package com.lighting.dm.request;

public interface Constants {
    String PROTOCOL_HTTP = "http";
    String PROTOCOL_HTTPS = "https://";

    String DEVICE_LINK_INFO_URL = "https://";
    String DEVICE_TOPIC_URL = "https://";

    //auth
    String DEVICE_AUTH = "/api/mqttAuth";
    //api url
    String GET_PRODUCTS = "/api/sim/products";
    String GET_DEVICES = "/api/sim/devices";
    String GET_DEVICE_AUTHS = "/api/sim/deviceAuths";
    String GET_TOPICS = "/api/sim/topics";
    String GET_THING_MODEL = "/api/sim/devices/model";
    //params
    String productId = "productId";
    String deviceCode = "deviceCode";
    String apiUrl = "apiUrl";
    String productKey = "productKey";

    String EMPTY_STRING = "";

}
