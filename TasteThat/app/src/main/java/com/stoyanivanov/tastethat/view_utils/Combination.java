package com.stoyanivanov.tastethat.view_utils;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by stoyan-ivanov on 03.10.17.
 */

public class Combination {
    private String firstComponent;
    private String secondComponent;

    public Combination(String firstComponent, String secodComponent) {
        this.firstComponent = firstComponent;
        this.secondComponent = secodComponent;
    }

    public String getFirstComponent() {
        return firstComponent;
    }

    public String getSecondComponent() {
        return secondComponent;
    }
}
