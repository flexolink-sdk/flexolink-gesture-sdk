package com.example.testgestureapplication;

import android.content.Intent;

import com.lzy.okgo.utils.IOUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @ProjectName: SUN9PAY
 * @Package: com.sun9s.mcht.utils.interceptor
 * @ClassName: TokenInterceptor
 * @Description: java类作用描述
 * @Author: huangxy
 * @CreateDate: 2020/1/9 19:43
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/1/9 19:43
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TokenInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset UTF8 = Charset.forName("UTF-8");
        String data = buffer.clone().readString(UTF8);//采用复制的方式读取，否则会导致请求异常!
        int code = 0;
//这判断根据你们后台的具体返回数据进行判断。(我们后台会返回token 失效,我以此进行判断，你们也可以让后台返回特定的code进行判断)
        try {
            JSONObject jsonObject = new JSONObject(data);
            code = jsonObject.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * 根据Response，判断Token是否失效
     *
     * @param response
     * @return
     */
    private boolean isTokenExpired(Response response) {
        Response.Builder builder = response.newBuilder();
        Response clone = builder.build();
        ResponseBody responseBody = clone.body();
        int code = 0;
        byte[] bytes = new byte[0];
        try {
            bytes = IOUtils.toByteArray(responseBody.byteStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaType contentType = responseBody.contentType();
        String body = new String(bytes, getCharset(contentType));
        try {
            JSONObject jsonObject = new JSONObject(body);
            code = jsonObject.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (code == 8) {
            return true;
        }
        return false;
    }

    private static Charset getCharset(MediaType contentType) {
        Charset charset = contentType != null ? contentType.charset(UTF8) : UTF8;
        if (charset == null) charset = UTF8;
        return charset;
    }

}
