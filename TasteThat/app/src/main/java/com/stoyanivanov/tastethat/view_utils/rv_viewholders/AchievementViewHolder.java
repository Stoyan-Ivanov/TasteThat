package com.stoyanivanov.tastethat.view_utils.rv_viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.models.Achievement;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by stoyan-ivanov on 25.11.17.
 */

public class AchievementViewHolder extends RecyclerView.ViewHolder {
    private CircleImageView ivAchievement;
    private CustomTextView ctvAchievementName;

    public AchievementViewHolder(View itemView) {
        super(itemView);

        ivAchievement = (CircleImageView) itemView.findViewById(R.id.iv_achievement);
        ctvAchievementName = (CustomTextView) itemView.findViewById(R.id.tv_achievement_name);
    }

    public void bind(Achievement achievement) {
        ctvAchievementName.setText(achievement.getName());
    }
}
