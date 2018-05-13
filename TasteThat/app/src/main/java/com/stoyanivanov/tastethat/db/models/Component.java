package com.stoyanivanov.tastethat.db.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Stoyan on 2.2.2018 г..
 */

public class Component implements Parcelable {
    private String componentName;
    private String componentImageUrl;

    public Component() { }

    public Component(String componentName) {
        this.componentName = componentName;
    }

    public Component(String componentName, String imageUrl) {
        this.componentName = componentName;
        this.componentImageUrl = imageUrl;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getComponentImageUrl() {
        return componentImageUrl;
    }

    public void setComponentImageUrl(String componentImageUrl) {
        this.componentImageUrl = componentImageUrl;
    }

    protected Component(Parcel in) {
        componentName = in.readString();
        componentImageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(componentName);
        dest.writeString(componentImageUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Component> CREATOR = new Parcelable.Creator<Component>() {
        @Override
        public Component createFromParcel(Parcel in) {
            return new Component(in);
        }

        @Override
        public Component[] newArray(int size) {
            return new Component[size];
        }
    };
}
