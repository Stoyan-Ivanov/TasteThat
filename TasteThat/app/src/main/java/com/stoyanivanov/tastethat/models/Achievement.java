package com.stoyanivanov.tastethat.models;

/**
 * Created by stoyan-ivanov on 25.11.17.
 */

public class Achievement {
    private String name;
    private Integer value;

    public Achievement() {
    }

    public Achievement(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }
}
