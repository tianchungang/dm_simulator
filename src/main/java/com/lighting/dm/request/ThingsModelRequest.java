package com.lighting.dm.request;


import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component("thingModel")
public class ThingsModelRequest extends SimRequest {
    @Override
    public String getUrl() {
        return Constants.GET_THING_MODEL;
    }

    @Override
    public Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put(Constants.deviceCode, request.getParameter(Constants.deviceCode));
        return param;
    }
}
