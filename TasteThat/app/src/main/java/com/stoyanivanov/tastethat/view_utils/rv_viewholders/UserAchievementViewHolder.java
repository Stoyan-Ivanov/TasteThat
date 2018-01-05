package com.stoyanivanov.tastethat.view_utils.rv_viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.models.Achievement;
import com.stoyanivanov.tastethat.network.TasteThatApplication;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Stoyan on 29.12.2017 г..
 */

public class UserAchievementViewHolder extends RecyclerView.ViewHolder {
    private CircleImageView ivAchievement;

    public UserAchievementViewHolder(View itemView) {
        super(itemView);

        ivAchievement = (CircleImageView) itemView.findViewById(R.id.iv_user_achievement);
    }

    public void bind(Achievement achievement) {
        Glide.with(TasteThatApplication.getStaticContext())
                .load(achievement.getImageUrl())
                .fitCenter()
                .into(ivAchievement);
    }
}
