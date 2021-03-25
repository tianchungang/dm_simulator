package com.lighting.dm.request;


import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haoqian.wang@signify.com
 * @date 2021/3/24   14:56
 */
@Component("thingModel")
public class ThingsModelRequest extends SimRequest {
    @Override
    public String getUrl() {
        return RequestPair.THING_MODEL.getUrl();
    }

    @Override
    public Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put(Constants.deviceCode, request.getParameter(Constants.deviceCode));
        return param;
    }
}
