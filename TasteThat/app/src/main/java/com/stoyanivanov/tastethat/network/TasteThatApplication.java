package com.stoyanivanov.tastethat.network;

import android.app.Application;
import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by stoyan-ivanov on 14.11.17.
 */

public class TasteThatApplication extends Application {
    private static Context applicationContext;
    private static TasteThatApplication instance;
    private static Retrofit retrofit;
    private static TasteThatAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        applicationContext = getApplicationContext();

        createRetrofitInstance();
    }

    private void createRetrofitInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(NetworkConstants.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();

        api = retrofit.create(TasteThatAPI.class);
    }

    public static Context getStaticContext() {
        return applicationContext;
    }

    public static TasteThatApplication getInstance() {
        return instance;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static TasteThatAPI getApi() {
        return api;
    }
}
