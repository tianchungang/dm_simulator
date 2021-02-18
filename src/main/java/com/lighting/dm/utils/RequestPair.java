package com.lighting.dm.utils;

public enum RequestPair {
    DEVICE_AUTH_URL("device_auth", Constants.DEVICE_AUTH_URL),
    LIST("list", Constants.GET_DEVICES_LIST_URL),
    PRODUCT("product", Constants.GET_PRODUCT_LIST_URL),
    SINGLE("single", Constants.GET_SINGLE_DEVICE_URL),
    THING_MODEL("thingModel", Constants.GET_DEVICE_THING_MODEL_URL);

    private String type;
    private String url;

    RequestPair(String type, String url) {
        this.type = type;
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}
