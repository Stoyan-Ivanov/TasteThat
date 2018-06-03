package com.stoyanivanov.tastethat.view_utils.recyclerview_utils.user_achievements_recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.db.models.Achievement;

import java.util.ArrayList;


/**
 * Created by Stoya on 29.12.2017 г..
 */

public class UserAchievementsRecyclerViewAdapter extends RecyclerView.Adapter<UserAchievementViewHolder> {
    private ArrayList<Achievement> mAchievements;

    public UserAchievementsRecyclerViewAdapter(ArrayList<Achievement> data) {
        this.mAchievements = data;
    }

    @Override
    public UserAchievementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_user_achievement_holder, parent, false);

        return new UserAchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserAchievementViewHolder holder, int position) {
        holder.bind(mAchievements.get(position));
    }

    @Override
    public int getItemCount() {
        return mAchievements.size();
    }

    public void setNewData(ArrayList<Achievement> achievements) {
        mAchievements = new ArrayList<>(achievements);
        notifyDataSetChanged();
    }
}
