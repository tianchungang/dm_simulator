package com.lighting.dm.request;


import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component("product")
public class ProductRequest extends SimRequest {
    @Override
    public String getUrl() {
        return Constants.GET_PRODUCTS;
    }

    @Override
    public Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put(Constants.deviceCode, request.getParameter(Constants.deviceCode));
        return param;
    }
}
