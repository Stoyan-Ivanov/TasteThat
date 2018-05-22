package com.stoyanivanov.tastethat.db.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import butterknife.Optional;

/**
 * Created by stoyan-ivanov on 03.10.17.
 */

public class Combination implements Parcelable {
    private String combinationKey;
    private String userId;
    private String username;
    private int negativeLikes;
    private ArrayList<Component> components;
    private Object timestamp;
    private String description;

    public Combination(String combinationName, ArrayList<Component> components,
                       String userId, String username, Object timestamp, String description) {

        this.combinationKey = combinationName;
        this.components = components;
        this.userId = userId;
        this.username = username;
        this.timestamp = timestamp;
        this.negativeLikes = 0;
        this.description = description;
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

    public int getNegativeLikes() {
        return negativeLikes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        combinationKey = in.readString();
        userId = in.readString();
        username = in.readString();
        negativeLikes = in.readInt();
        if (in.readByte() == 0x01) {
            components = new ArrayList<Component>();
            in.readList(components, Component.class.getClassLoader());
        } else {
            components = null;
        }
        timestamp = (Object) in.readValue(Object.class.getClassLoader());
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(combinationKey);
        dest.writeString(userId);
        dest.writeString(username);
        dest.writeInt(negativeLikes);
        if (components == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(components);
        }
        dest.writeValue(timestamp);
        dest.writeString(description);
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