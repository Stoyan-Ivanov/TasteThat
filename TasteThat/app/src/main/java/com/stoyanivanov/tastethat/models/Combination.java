package com.stoyanivanov.tastethat.models;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by stoyan-ivanov on 03.10.17.
 */

public class Combination {
    String firstComponent, secondComponent, userId;
    int likes;

    public Combination() {

    }

    public Combination(String firstComponent, String secodComponent, String userId, int likes) {
        this.firstComponent = firstComponent;
        this.secondComponent = secodComponent;
        this.userId = userId;
        this.likes = likes;
    }

    public String getFirstComponent() {
        return firstComponent;
    }

    public String getSecondComponent() {
        return secondComponent;
    }

    public String getUserId() {
        return userId;
    }

    public int getLikes() {
        return likes;
    }

}
