package com.janak.rxandroidnetworking;

import android.app.Application;

import com.janak.androidnetworking.AndroidNetworking;
import com.janak.androidnetworking.HttpClientBuilder;
import com.janak.androidnetworking.interceptors.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

public class NetworkingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        HttpClientBuilder.Builder httpClientBuilder = new HttpClientBuilder.Builder()
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .setCurlInterceptor();

        AndroidNetworking.initialize(this, httpClientBuilder.build());

        if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.NONE);
        }
    }
}
