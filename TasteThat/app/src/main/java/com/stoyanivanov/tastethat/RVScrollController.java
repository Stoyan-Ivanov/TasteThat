package com.stoyanivanov.tastethat;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by stoyan-ivanov on 30.10.17.
 */

public class RVScrollController {

    public RVScrollController() {
    }

    public void addControlToBottomNavigation(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                BottomNavigationView bottomNavigationView = MainActivity.getBottomNavigation();

                if (dy > 0 && bottomNavigationView.isShown()) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else if (dy < 0 || dy == 0) {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
