package com.framgia.soundcloud.ui.activity.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.framgia.soundcloud.ui.activity.Const.BASE_URL;

/**
 * Created by K on 5/12/2017.
 */

public class ServiceGenerator {
    private static Retrofit retrofit = null;
    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor);

    private static OkHttpClient okHttpClient = okHttpClientBuilder.build();

    public static <T> T createService(Class<T> serviceClass) {
        if (retrofit == null) {
            retrofit = builder.client(okHttpClient).build();
        }
        return retrofit.create(serviceClass);
    }
}

