package com.stoyanivanov.tastethat.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by stoyan-ivanov on 14.11.17.
 */

public class NextImagesResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private Data data;

    public NextImagesResponse(String status, Data data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
