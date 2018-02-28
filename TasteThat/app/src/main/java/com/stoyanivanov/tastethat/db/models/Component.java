package com.stoyanivanov.tastethat.db.models;

/**
 * Created by Stoyan on 2.2.2018 г..
 */

public class Component {
    private String name;
    private String imageUrl;

    public Component() {
    }

    public Component(String componentName, String componentUrl) {
        this.name = componentName;
        this.imageUrl = componentUrl;
    }

    public String getComponentName() {
        return name;
    }

    public String getComponentUrl() {
        return imageUrl;
    }
}
