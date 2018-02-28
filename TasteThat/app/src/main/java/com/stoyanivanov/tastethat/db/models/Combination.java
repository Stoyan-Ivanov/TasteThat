package com.stoyanivanov.tastethat.db.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 03.10.17.
 */

public class Combination implements Parcelable {
    private String combinationKey, userId, username;
    private ArrayList<Component> components;
    private Object timestamp;

    public Combination(String combinationName, ArrayList<Component> components,
                       String userId, String username, Object timestamp) {

        this.combinationKey = combinationName;
        this.components = components;
        this.userId = userId;
        this.username = username;
        this.timestamp = timestamp;
    }

    public Combination() {
    }

    public String getCombinationKey() {
        return combinationKey;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
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

        for(int i = 0; i < components.size() - 2; i++ ) {
            displayString += components.get(i).getComponentName() + ", ";
        }
        displayString += components.get(components.size() - 2).getComponentName() + " & ";
        displayString += components.get(components.size() - 1).getComponentName();

        return displayString;
    }

    protected Combination(Parcel in) {
        timestamp = (Object) in.readValue(Object.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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