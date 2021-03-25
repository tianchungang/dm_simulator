package com.lighting.dm.request;


import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haoqian.wang@signify.com
 * @date 2021/3/24   14:56
 */
@Component("topics")
public class TopicsRequest extends SimRequest {
    @Override
    public String getUrl() {
        return RequestPair.TOPICS.getUrl();
    }

    @Override
    public Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put(Constants.productId, request.getParameter(Constants.productId));
        param.put(Constants.deviceCode, request.getParameter(Constants.deviceCode));
        param.put(Constants.productKey, request.getParameter(Constants.productKey));
        return param;
    }
}
