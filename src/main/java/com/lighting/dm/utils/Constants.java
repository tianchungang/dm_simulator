package com.lighting.dm.utils;

public interface Constants {
    String PROTOCOL_HTTP = "http";
    String PROTOCOL_HTTPS = "https://";

    String DEVICE_LINK_INFO_URL = "https://";
    String DEVICE_AUTH_URL = "/api/mqttAuth";
    String DEVICE_TOPIC_URL = "https://";

    String GET_PRODUCT_LIST_URL = "/api/sim/products/mqtt";
    String GET_DEVICES_LIST_URL = "/api/sim/getDevicesByProductId/{0}";
    String GET_SINGLE_DEVICE_URL = "/api/sim/deviceAuths/deviceSecret/{0}/{1}";
    String GET_DEVICE_THING_MODEL_URL = "/api/sim/devices/model/{0}";
}
