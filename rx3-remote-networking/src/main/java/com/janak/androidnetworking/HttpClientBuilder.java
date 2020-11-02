package com.janak.androidnetworking;

import com.janak.androidnetworking.interceptors.curl.CurlLoggerInterceptor;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

public class HttpClientBuilder {

    private OkHttpClient okHttpClient;

    public static class Builder {

        private final OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder();

        public Builder callTimeout(long timeout, TimeUnit timeUnit) {
            okHttpClient.callTimeout(timeout, timeUnit);
            return this;
        }

        public Builder callTimeout(Duration duration) {
            okHttpClient.callTimeout(duration);
            return this;
        }

        public Builder connectTimeout(long timeout, TimeUnit timeUnit) {
            okHttpClient.connectTimeout(timeout, timeUnit);
            return this;
        }

        public Builder connectTimeout(Duration duration) {
            okHttpClient.connectTimeout(duration);
            return this;
        }

        public Builder readTimeout(long timeout, TimeUnit timeUnit) {
            okHttpClient.readTimeout(timeout, timeUnit);
            return this;
        }

        public Builder readTimeout(Duration duration) {
            okHttpClient.readTimeout(duration);
            return this;
        }

        public Builder writeTimeout(long timeout, TimeUnit timeUnit) {
            okHttpClient.writeTimeout(timeout, timeUnit);
            return this;
        }

        public Builder writeTimeout(Duration duration) {
            okHttpClient.writeTimeout(duration);
            return this;
        }

        public Builder pingInterval(long timeout, TimeUnit timeUnit) {
            okHttpClient.pingInterval(timeout, timeUnit);
            return this;
        }

        public Builder pingInterval(Duration duration) {
            okHttpClient.pingInterval(duration);
            return this;
        }

        public Builder setCurlInterceptor() {
            okHttpClient.addInterceptor(new CurlLoggerInterceptor());
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            okHttpClient.addInterceptor(interceptor);
            return this;
        }

        public Builder addNetworkInterceptor(Interceptor interceptor) {
            okHttpClient.addNetworkInterceptor(interceptor);
            return this;
        }

        public HttpClientBuilder build() {
            HttpClientBuilder httpClientBuilder = new HttpClientBuilder();
            httpClientBuilder.setOkHttpClient(okHttpClient.build());
            return httpClientBuilder;
        }
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }
}
