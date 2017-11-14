package com.stoyanivanov.tastethat.network;

import com.stoyanivanov.tastethat.network.network_models.NextImagesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by stoyan-ivanov on 14.11.17.
 */

public interface TasteThatAPI {

    @GET("{searchedString}")
    Call<NextImagesResponse> getNextImages(@Path("searchedString") String searchedString);
}
