package com.stoyanivanov.tastethat.view_utils.views_behaviour;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.stoyanivanov.tastethat.ui.activities.BaseBottomNavigationActivity;
import com.stoyanivanov.tastethat.view_utils.controllers.RVScrollController;

/**
 * Created by Stoyan on 20.1.2018 г..
 */

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 1; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem, visibleItemCount, totalItemCount;


    private LinearLayoutManager mLinearLayoutManager;
    private FloatingActionButton floatingActionButton;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager, FloatingActionButton floatingActionButton) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.floatingActionButton = floatingActionButton;
    }

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        // show and hide bottom navigation and FAB
        RVScrollController controller = new RVScrollController();
        controller.addControlToBottomNavigation(recyclerView, floatingActionButton);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && ((totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold))) {
            onLoadMore();
            loading = true;
        }
    }

    public abstract void onLoadMore();
}

