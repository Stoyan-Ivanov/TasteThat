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
    private LayoutInflater inflater;
    private ArrayList<Achievement> data;

    public UserAchievementsRecyclerViewAdapter(ArrayList<Achievement> data) {
        this.data = data;
    }

    @Override
    public UserAchievementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.rv_user_achievement_holder, parent, false);

        return new UserAchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserAchievementViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setNewData(ArrayList<Achievement> achievements) {
        data = new ArrayList<>(achievements);
        notifyDataSetChanged();
    }
}
