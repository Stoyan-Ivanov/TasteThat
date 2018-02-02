package com.stoyanivanov.tastethat.db.models;

/**
 * Created by Stoyan on 2.2.2018 г..
 */

public class Pair {
    String componentName;
    String componentUrl;

    public Pair() {
    }

    public Pair(String componentName, String componentUrl) {
        this.componentName = componentName;
        this.componentUrl = componentUrl;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentUrl() {
        return componentUrl;
    }

    public void setComponentUrl(String componentUrl) {
        this.componentUrl = componentUrl;
    }
}
