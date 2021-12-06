package com.lighting.dm.request;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component("custom_api")
public class CustomUrlRequest extends SimRequest {
    @Override
    public String getUrl() {
        return Constants.EMPTY_STRING;
    }

    @Override
    public Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put(Constants.apiUrl, request.getParameter(Constants.apiUrl));
        return param;
    }
}
