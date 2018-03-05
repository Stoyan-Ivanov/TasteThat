package com.stoyanivanov.tastethat.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private ArrayList<Achievement> achievements;
    private MyAchievementsRecyclerViewAdapter adapter;
    private FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

    @BindView(R.id.iv_achievements_profile_picture)
    CircleImageView ivProfilePic;
    @BindView(R.id.ctv_achievements_username)
    CustomTextView ctvUserName;
    @BindView(R.id.rv_achievements)
    RecyclerView recyclerView;

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
        achievements = new ArrayList<>();
        tableUsers.child(currUser.getUid())
                .child(Constants.USER_ACHIEVEMENTS)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Achievement currAchievement = snapshot.getValue(Achievement.class);
                            achievements.add(currAchievement);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void displayUserInfo() {
        String userPhotoUrl = "";
        if(currUser.getPhotoUrl() != null) {
            userPhotoUrl = currUser.getPhotoUrl().toString();
        }

        if(userPhotoUrl.isEmpty()) {
            ivProfilePic.setImageResource(R.drawable.default_user_picture);
        } else {
            Glide.with(this)
                    .load(userPhotoUrl)
                    .into(ivProfilePic);
        }

        if(currUser != null) {
            ctvUserName.setText(currUser.getDisplayName());
        }
    }

    private void instantiateRV() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAchievementsRecyclerViewAdapter(achievements);
        recyclerView.setAdapter(adapter);

        RVScrollController scrollController = new RVScrollController();
        scrollController.addControlToBottomNavigation(recyclerView);
    }
}
