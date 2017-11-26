package com.stoyanivanov.tastethat.models;

/**
 * Created by stoyan-ivanov on 25.11.17.
 */

public class Achievement {
    private String name, imageUrl;
    private Integer value;

    public Achievement() {
    }

    public Achievement(String name, Integer value, String imageUrl) {
        this.name = name;
        this.value = value;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
