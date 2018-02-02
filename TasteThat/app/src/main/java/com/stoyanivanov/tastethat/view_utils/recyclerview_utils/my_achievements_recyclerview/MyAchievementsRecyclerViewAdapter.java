package com.stoyanivanov.tastethat.view_utils.recyclerview_utils.my_achievements_recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.db.models.Achievement;

import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 25.11.17.
 */

public class MyAchievementsRecyclerViewAdapter extends RecyclerView.Adapter<MyAchievementViewHolder> {
    private ArrayList<Achievement> data;
    private LayoutInflater inflater;

    public MyAchievementsRecyclerViewAdapter(ArrayList<Achievement> data) {
        this.data = data;
    }

    @Override
    public MyAchievementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.rv_achievement_holder, parent, false);

        return new MyAchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAchievementViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
