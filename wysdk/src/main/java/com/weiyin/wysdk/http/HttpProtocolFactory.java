package com.weiyin.wysdk.http;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HttpProtocolFactory {

    private static final Map<String, Object> sHttpProtocolMap = new ConcurrentHashMap<>();

    public static <T> T getProtocol(String host, Class<T> clazz) {
        if (sHttpProtocolMap.containsKey(clazz.getName())) {
            return (T) sHttpProtocolMap.get(clazz.getName());
        }

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(15, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(15, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(15, TimeUnit.SECONDS);
        okHttpClientBuilder.addInterceptor(new MyRequestInterceptor());

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());

            okHttpClientBuilder.sslSocketFactory(sc.getSocketFactory());

        } catch (Exception e) {
            e.printStackTrace();
        }

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClientBuilder.build());
        builder.baseUrl(host);
        builder.addConverterFactory(GsonConverterFactory.create());


        Retrofit retrofit = builder.build();

        T t = retrofit.create(clazz);
        sHttpProtocolMap.put(clazz.getName(), t);
        return t;
    }

    public static void clear() {
        sHttpProtocolMap.clear();
    }

    private static class MyRequestInterceptor implements Interceptor {

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {

            Request oldRequest = chain.request();

            // 新的请求
            Request newRequest = oldRequest.newBuilder().
                    addHeader("User-Agent", "Client-Android-SDK").
                    addHeader("Content-Type", "application/json")
                    .build();

            return chain.proceed(newRequest);
        }
    }
}
