package com.stoyanivanov.tastethat.view_utils.recyclerview_utils.user_achievements_recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.db.models.Achievement;
import com.stoyanivanov.tastethat.TasteThatApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Stoyan on 29.12.2017 г..
 */

public class UserAchievementViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_user_achievement) CircleImageView mIvAchievement;

    public UserAchievementViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(Achievement achievement) {
        Glide.with(TasteThatApplication.getStaticContext())
                .load(achievement.getImageUrl())
                .fitCenter()
                .into(mIvAchievement);

        itemView.setOnClickListener(view -> TasteThatApplication.showToast(achievement.getName()));
    }
}
