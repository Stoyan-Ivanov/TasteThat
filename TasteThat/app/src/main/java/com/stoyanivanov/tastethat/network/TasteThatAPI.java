package com.stoyanivanov.tastethat.network;

import com.stoyanivanov.tastethat.network.network_models.NextImagesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by stoyan-ivanov on 14.11.17.
 */

public interface TasteThatAPI {

    @GET("images?count=20&offset=1")
    Call<NextImagesResponse> getNextImages(@Query("q") String searchedString);
}
