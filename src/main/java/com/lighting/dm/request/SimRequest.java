package com.lighting.dm.request;

import com.google.common.base.Joiner;
import com.lighting.dm.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * @author haoqian.wang@signify.com
 * @date 2021/3/24   14:54
 */
@Slf4j
public abstract class SimRequest {
    /**
     * API URL
     *
     * @return
     */
    abstract String getUrl();

    /**
     * params
     *
     * @param request
     * @return
     */
    abstract Map<String, Object> getParams(HttpServletRequest request);

    /**
     * main
     *
     * @param request
     * @param serverUrl
     * @return
     * @throws Exception
     */
    public final String exec(HttpServletRequest request, String serverUrl) throws Exception {

        Map<String, Object> params = getParams(request);
        serverUrl = serverUrl + getUrl() + wrap(params);

        String responseStr = HttpUtils.getHttps(serverUrl);
        log.info("responseStr={}", responseStr);

        return responseStr;
    }

    private String wrap(Map<String, Object> param) {
        String result = "";
        if (MapUtils.isNotEmpty(param)) {
            if (param.containsKey(Constants.apiUrl)) {
                return String.valueOf(param.get(Constants.apiUrl));
            } else {
                for (Map.Entry<String, Object> entry : param.entrySet()) {
                    result += "&" + entry.getKey() + "=" + entry.getValue();
                }
            }
        }
        return  "?1=1" + result;
    }

}
