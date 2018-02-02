package com.stoyanivanov.tastethat.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by stoyan-ivanov on 14.11.17.
 */

public class Data {
    @SerializedName("result")
    private Result result;

    public Data(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
