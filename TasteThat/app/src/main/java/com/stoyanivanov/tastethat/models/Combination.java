package com.stoyanivanov.tastethat.models;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by stoyan-ivanov on 03.10.17.
 */

public class Combination {
    private String firstComponent, secondComponent, userId;

    public Combination() {

    }

    public Combination(String firstComponent, String secodComponent, String userId) {
        this.firstComponent = firstComponent;
        this.secondComponent = secodComponent;
        this.userId = userId;
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

}
