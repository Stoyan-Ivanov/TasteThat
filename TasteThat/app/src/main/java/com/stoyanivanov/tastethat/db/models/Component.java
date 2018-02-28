package com.stoyanivanov.tastethat.db.models;

/**
 * Created by Stoyan on 2.2.2018 г..
 */

public class Component {
    private String componentName;
    private String imageUrl;

    public Component() {
    }

    public Component(String componentName, String imageUrl) {
        this.componentName = componentName;
        this.imageUrl = imageUrl;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getComponentImageUrl() {
        return imageUrl;
    }
}
