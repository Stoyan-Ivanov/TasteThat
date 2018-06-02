package com.stoyanivanov.tastethat.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.db.models.Achievement;
import com.stoyanivanov.tastethat.view_utils.controllers.RVScrollController;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.my_achievements_recyclerview.MyAchievementsRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.tableUsers;

public class MyAchievementsActivity extends BaseBottomNavigationActivity {
    private ArrayList<Achievement> mAchievements;
    private MyAchievementsRecyclerViewAdapter mAdapter;

    @BindView(R.id.iv_achievements_profile_picture) CircleImageView mIvProfilePic;
    @BindView(R.id.ctv_achievements_username) CustomTextView mCtvUserName;
    @BindView(R.id.rv_achievements) RecyclerView mRecyclerView;

    public static Intent getIntent(Context context, int bottomNavOption) {
        Intent intent = new Intent(context, MyAchievementsActivity.class);
        intent.putExtra(StartConstants.EXTRA_NAV_OPTION, bottomNavOption);
        intent.putExtra(StartConstants.EXTRA_FLAG, StartConstants.EXTRA_FLAG_VALUE);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_achievements);

        getAchievements();
        displayUserInfo();
        instantiateRV();
    }

    private void getAchievements() {
        mAchievements = new ArrayList<>();
        tableUsers.child(mCurrentUser.getUid())
                .child(Constants.USER_ACHIEVEMENTS)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Achievement currAchievement = snapshot.getValue(Achievement.class);
                            mAchievements.add(currAchievement);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void displayUserInfo() {
        String userPhotoUrl = "";
        if(mCurrentUser.getPhotoUrl() != null) {
            userPhotoUrl = mCurrentUser.getPhotoUrl().toString();
        }

        Glide.with(this)
                .load(userPhotoUrl)
                .placeholder(R.drawable.default_user_picture)
                .into(mIvProfilePic);

        if(mCurrentUser != null) {
            mCtvUserName.setText(mCurrentUser.getDisplayName());
        }
    }

    private void instantiateRV() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyAchievementsRecyclerViewAdapter(mAchievements);
        mRecyclerView.setAdapter(mAdapter);

        RVScrollController scrollController = new RVScrollController();
        scrollController.addControlToBottomNavigation(mRecyclerView);
    }
}
