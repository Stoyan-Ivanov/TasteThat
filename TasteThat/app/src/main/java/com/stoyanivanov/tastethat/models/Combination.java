package com.stoyanivanov.tastethat.models;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 03.10.17.
 */

public class Combination implements Serializable {
    private String combinationName, userId, username;
    private ArrayList<String> components;
    private ArrayList<String> urls;

    public Combination(String combinationName, ArrayList<String> components, String userId, String username,
                       ArrayList<String> urls) {
        this.combinationName = combinationName;
        this.components = components;
        this.userId = userId;
        this.username = username;
        this.urls = urls;
    }

    public Combination() {
    }

    public String getCombinationName() {
        return combinationName;
    }

    public ArrayList<String> getComponents() {
        return components;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }
}
