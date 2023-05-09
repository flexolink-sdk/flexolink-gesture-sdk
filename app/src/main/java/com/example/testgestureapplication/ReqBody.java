package com.example.testgestureapplication;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * time:    on 2019/7/30 0030 16:42
 * author:  huangxinyu
 * mail:    huangxinyu93@163.com
 */
public class ReqBody {

    /**
     *  获取请求体
     *
     * */
    public static RequestBody getReqBody(Map<String, Object> map){
        Gson gson = new Gson();
        String jsonString = gson.toJson(map);
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonString);
        return body;
    }

    public static String getReqString(Map<String, String> map){
        JSONObject jsonObject = new JSONObject();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
//            jsonObject.put("access_token", AppConfig.token.get());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
