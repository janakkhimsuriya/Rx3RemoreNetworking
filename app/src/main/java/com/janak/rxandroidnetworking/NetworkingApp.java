package com.janak.rxandroidnetworking;

import android.app.Application;

import com.janak.androidnetworking.AndroidNetworking;
import com.janak.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.janak.androidnetworking.interceptors.curl.CurlLoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class NetworkingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient httpClientBuilder = new OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(new CurlLoggerInterceptor()).build();

        AndroidNetworking.initialize(this, httpClientBuilder);

        if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.NONE);
        }
    }
}
