package com.lighting.dm.mq;

import com.google.gson.Gson;
import com.lighting.dm.utils.HttpUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.protocol.HTTP;

import java.util.Base64;
import java.util.HashMap;

/**
 * @ClassName RabbitApiCaller.java
 * @Author Tom.Tian
 * @Description RabbitApiCaller
 * @createTime 2021-01-26 14:01:26
 */
public class RabbitApiCaller {
//    private static final String userName = "dm_admin";
    private static final String userName = "guest";
//    private static final String password = "dm_admin123";
    private static final String password = "guest";
    private static final String rabbitMqUserApiUrl = "http://106.14.106.194:15672/api/users";

    public static void main(String[] args) {
        HashMap<String, String> headerMap = new HashMap<>();

        String authorization = "Basic " + new String(Base64.getEncoder().encode((userName+":" + password).getBytes()));
        System.out.println("authorization = " + authorization);

        headerMap.put(HttpHeaders.AUTHORIZATION,authorization);
        try {
//            String datas = HttpUtils.getHttp(rabbitMqUserApiUrl, new HashMap<>(),headerMap);

            HashMap<Object, Object> data = new HashMap<>();
            data.put("password","abc123");
            data.put("tags","administrator");

            String datas = HttpUtils.putJson(rabbitMqUserApiUrl+"/tom",new Gson().toJson(data), headerMap);
            System.out.println("datas = " + datas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
