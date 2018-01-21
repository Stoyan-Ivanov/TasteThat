package com.stoyanivanov.tastethat.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 03.10.17.
 */

public class Combination implements Serializable {
    private String combinationKey, userId, username;
    private ArrayList<String> components;
    private ArrayList<String> urls;
    private Object timestamp;

    public Combination(String combinationName, ArrayList<String> components, String userId, String username,
                       ArrayList<String> urls, Object timestamp) {
        this.combinationKey = combinationName;
        this.components = components;
        this.userId = userId;
        this.username = username;
        this.urls = urls;
        this.timestamp = timestamp;
    }

    public Combination() {
    }

    public String getCombinationKey() {
        return combinationKey;
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

    public Object getTimestamp() {
        return timestamp;
    }
}
