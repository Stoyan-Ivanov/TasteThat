package com.stoyanivanov.tastethat.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 14.11.17.
 */

public class Result {
    @SerializedName("items")
    private ArrayList<Picture> pictures;

    public Result(ArrayList<Picture> pictures) {
        this.pictures = pictures;
    }

    public ArrayList<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<Picture> pictures) {
        this.pictures = pictures;
    }
}
