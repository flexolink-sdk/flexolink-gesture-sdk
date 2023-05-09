package com.example.testgestureapplication;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileDownloader.init(getApplicationContext());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put("token", "");
        HttpParams params = new HttpParams();
        params.put("token", "");    //header不支持中文，不允许有特殊字符

        OkHttpClient.Builder builder = new OkHttpClient.Builder().retryOnConnectionFailure(true);
        //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        TokenInterceptor tokenInterceptor = new TokenInterceptor();
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        builder.addInterceptor(tokenInterceptor);
        builder.readTimeout(60000, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(60000, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(60000, TimeUnit.MILLISECONDS);   //全局的连接超时时间
        OkGo.getInstance()
                .init(this)
                .addCommonParams(params)
                .addCommonHeaders(httpHeaders)
                .setOkHttpClient(builder.build()) //设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                .setRetryCount(3);

    }
}
