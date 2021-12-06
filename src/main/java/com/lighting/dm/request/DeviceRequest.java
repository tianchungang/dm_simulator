package com.lighting.dm.request;


import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component("single")
public class DeviceRequest extends SimRequest {
    @Override
    public String getUrl() {
        return Constants.SINGLE_DEVICE;
    }

    @Override
    public Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put(Constants.productId, request.getParameter(Constants.productId));
        param.put(Constants.deviceCode, request.getParameter(Constants.deviceCode));
        return param;
    }
}
