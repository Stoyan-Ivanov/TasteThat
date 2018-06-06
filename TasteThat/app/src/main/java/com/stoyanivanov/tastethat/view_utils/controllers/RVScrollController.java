package com.stoyanivanov.tastethat.view_utils.controllers;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stoyanivanov.tastethat.ui.activities.BaseBottomNavigationActivity;

/**
 * Created by stoyan-ivanov on 30.10.17.
 */

public class RVScrollController {
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;

    public RVScrollController() {}

    public void addControlToBottomNavigation(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        controlBottomNavigation();
    }

    public void addControlToBottomNavigation(RecyclerView recyclerView, FloatingActionButton floatingActionButton) {
        mRecyclerView = recyclerView;
        mFloatingActionButton = floatingActionButton;
        controlBottomNavigation();
    }

    private void controlBottomNavigation() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                BottomNavigationView bottomNavigationView = BaseBottomNavigationActivity.bottomNavigationView;

                if (dy > 0 && bottomNavigationView.isShown()) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else if (dy < 0 || dy == 0) {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
