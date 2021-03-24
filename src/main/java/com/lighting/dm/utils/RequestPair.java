package com.lighting.dm.utils;

public enum RequestPair {
    DEVICE_AUTH("device_auth", Constants.DEVICE_AUTH),
    DEVICES("devices", Constants.GET_DEVICES),
    PRODUCTS("product", Constants.GET_PRODUCTS),
    SINGLE("single", Constants.GET_DEVICE_AUTHS),
    TOPICS("topic", Constants.GET_TOPICS),
    CUSTOM_API("custom_api", null),
    THING_MODEL("thingModel", Constants.GET_THING_MODEL);

    private String step;
    private String url;

    RequestPair(String step, String url) {
        this.step = step;
        this.url = url;
    }

    public String getStep() {
        return step;
    }

    public String getUrl() {
        return url;
    }
}
