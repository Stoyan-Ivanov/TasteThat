package com.stoyanivanov.tastethat.db.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 03.10.17.
 */

public class Combination implements Parcelable {
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

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        String displayString = "";

        for(int i = 0; i < components.size() - 1; i++ ) {
            displayString += components.get(i) + ", ";
        }
        displayString += components.get(components.size() -1);

        return displayString;
    }

    protected Combination(Parcel in) {
        if (in.readByte() == 0x01) {
            components = new ArrayList<String>();
            in.readList(components, String.class.getClassLoader());
        } else {
            components = null;
        }
        if (in.readByte() == 0x01) {
            urls = new ArrayList<String>();
            in.readList(urls, String.class.getClassLoader());
        } else {
            urls = null;
        }
        timestamp = (Object) in.readValue(Object.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (components == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(components);
        }
        if (urls == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(urls);
        }
        dest.writeValue(timestamp);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Combination> CREATOR = new Parcelable.Creator<Combination>() {
        @Override
        public Combination createFromParcel(Parcel in) {
            return new Combination(in);
        }

        @Override
        public Combination[] newArray(int size) {
            return new Combination[size];
        }
    };
}