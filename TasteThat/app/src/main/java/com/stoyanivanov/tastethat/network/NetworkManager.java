package com.stoyanivanov.tastethat.network;


import android.util.Log;

import com.stoyanivanov.tastethat.network.models.NextImagesResponse;
import com.stoyanivanov.tastethat.network.models.Picture;
import com.stoyanivanov.tastethat.ui.fragments.ChooseImageFragment;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Stoyan on 2.2.2018 г..
 */

public class NetworkManager {
    private static NetworkManager instance;
    private static Retrofit retrofit;
    private static QwantAPI api;

    public static NetworkManager getInstance() {
        if(instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    private NetworkManager() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.qwant.com/api/search/")
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();

        api = retrofit.create(QwantAPI.class);
    }

    public void getNextImages(final String component, final ChooseImageFragment fragment) {
        Call<NextImagesResponse> call = api.getNextImages(component);

        call.enqueue(new Callback<NextImagesResponse>() {
            @Override
            public void onResponse(Call<NextImagesResponse> call, Response<NextImagesResponse> response) {
                NextImagesResponse nextImagesResponse = response.body();
                ArrayList<Picture> pictures = nextImagesResponse.getData().getResult().getPictures();
                Log.d("SII", "onResponse: " + component);
                fragment.onImagesGathered(pictures);
            }

            @Override
            public void onFailure(Call<NextImagesResponse> call, Throwable t) {
                Log.d("SII", "onFailure: getNextImages()");
            }
        });
    }
}
