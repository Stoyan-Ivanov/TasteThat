package com.stoyanivanov.tastethat.view_utils.rv_adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.models.Achievement;
import com.stoyanivanov.tastethat.view_utils.rv_viewholders.AchievementViewHolder;

import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 25.11.17.
 */

public class AchievementsRecyclerViewAdapter extends RecyclerView.Adapter<AchievementViewHolder> {
    private ArrayList<Achievement> data;
    private LayoutInflater inflater;

    public AchievementsRecyclerViewAdapter(ArrayList<Achievement> data) {
        this.data = data;
    }

    @Override
    public AchievementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.rv_achievement_holder, parent, false);

        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AchievementViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
