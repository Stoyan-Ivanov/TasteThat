package com.stoyanivanov.tastethat.db.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 03.10.17.
 */

public class Combination implements Parcelable {
    private String combinationName;
    private String combinationKey;
    private String userId;
    private String username;
    private float rating;
    private float negativeRating;
    private ArrayList<Component> components;
    private Object timestamp;
    private String description;

    public Combination(String combinationName, ArrayList<Component> components,
                       String userId, String username, Object timestamp, String description) {

        this.combinationKey = combinationName;
        this.components = components;
        this.combinationName = createCombinationName();
        this.userId = userId;
        this.username = username;
        this.timestamp = timestamp;
        this.rating = 0;
        this.negativeRating = 0;
        this.description = description;
    }

    public Combination() {
    }

    public String getCombinationName() {
        return combinationName;
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

    public float getRating() {
        return rating;
    }

    public float getNegativeRating() {
        return negativeRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return combinationName;
    }

    private String createCombinationName() {
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < components.size() - 2; i++ ) {
            builder.append(components.get(i).getComponentName()).append(", ");
        }
        builder.append(components.get(components.size() - 2).getComponentName()).append(" & ");
        builder.append(components.get(components.size() - 1).getComponentName());

        return builder.toString();
    }

    protected Combination(Parcel in) {
        combinationName = in.readString();
        combinationKey = in.readString();
        userId = in.readString();
        username = in.readString();
        rating = in.readInt();
        negativeRating = in.readInt();
        if (in.readByte() == 0x01) {
            components = new ArrayList<>();
            in.readList(components, Component.class.getClassLoader());
        } else {
            components = null;
        }
        timestamp = in.readValue(Object.class.getClassLoader());
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(combinationName);
        dest.writeString(combinationKey);
        dest.writeString(userId);
        dest.writeString(username);
        dest.writeFloat(rating);
        dest.writeFloat(negativeRating);
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