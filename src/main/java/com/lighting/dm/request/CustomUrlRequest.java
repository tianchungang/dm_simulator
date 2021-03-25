package com.lighting.dm.request;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haoqian.wang@signify.com
 * @date 2021/3/24   14:56
 */
@Component("custom_api")
public class CustomUrlRequest extends SimRequest {
    @Override
    public String getUrl() {
        return RequestPair.CUSTOM_API.getUrl();
    }

    @Override
    public Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put(Constants.apiUrl, request.getParameter(Constants.apiUrl));
        return param;
    }
}
