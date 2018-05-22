package com.stoyanivanov.tastethat.view_utils.recyclerview_utils.decoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by stoyan.ivanov2 on 4/11/2018.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpacing;
    private int mDisplayMode;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int GRID = 2;

    public SpacesItemDecoration(int spacing) {
        this(spacing, -1);
    }

    public SpacesItemDecoration(int spacing, int displayMode) {
        this.mSpacing = spacing;
        this.mDisplayMode = displayMode;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        int itemCount = state.getItemCount();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        setSpacingForDirection(outRect, layoutManager, position, itemCount);
    }

    private void setSpacingForDirection(Rect outRect,
                                        RecyclerView.LayoutManager layoutManager,
                                        int position,
                                        int itemCount) {

        // Resolve display mode automatically
        if (mDisplayMode == -1) {
            mDisplayMode = resolveDisplayMode(layoutManager);
        }

        switch (mDisplayMode) {
            case HORIZONTAL:
                outRect.left = mSpacing;
                outRect.right = position == itemCount - 1 ? mSpacing : 0;
                outRect.top = mSpacing;
                outRect.bottom = mSpacing;
                break;
            case VERTICAL:
                outRect.left = mSpacing / 2;
                outRect.right = mSpacing / 2;
                outRect.top = mSpacing;
                outRect.bottom = position == itemCount - 1 ? mSpacing : 0;
                break;
            case GRID:
                if (layoutManager instanceof GridLayoutManager) {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                    int cols = gridLayoutManager.getSpanCount();
                    int rows = itemCount / cols;

                    outRect.left = mSpacing;
                    outRect.right = position % cols == cols - 1 ? mSpacing : 0;
                    outRect.top = mSpacing;
                    outRect.bottom = position / cols == rows - 1 ? mSpacing : 0;
                }
                break;
        }
    }

    private int resolveDisplayMode(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) return GRID;
        if (layoutManager.canScrollHorizontally()) return HORIZONTAL;
        return VERTICAL;
    }
}