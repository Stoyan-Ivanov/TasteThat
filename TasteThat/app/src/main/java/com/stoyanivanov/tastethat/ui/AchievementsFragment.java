package com.stoyanivanov.tastethat.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.activities.UserProfileActivity;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.models.Achievement;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.controllers.RVScrollController;
import com.stoyanivanov.tastethat.view_utils.rv_adapters.AchievementsRecyclerViewAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.tableUsers;

public class AchievementsFragment extends Fragment {
    private ArrayList<Achievement> achievements;
    private AchievementsRecyclerViewAdapter adapter;
    private FirebaseUser currUser;
    private CircleImageView ivProfilePic;
    private CustomTextView ctvUserName;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievements, container, false);

        ivProfilePic = (CircleImageView) view.findViewById(R.id.iv_achievements_profile_picture);
        ctvUserName = (CustomTextView) view.findViewById(R.id.ctv_achievements_username);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_achievements);
        currUser = FirebaseAuth.getInstance().getCurrentUser();

        getAchievements();
        displayUserInfo();
        instantiateRV();

        return view;
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
        String userPhotoUrl = currUser.getPhotoUrl().toString();
        Glide.with(getActivity().getApplicationContext()).load(userPhotoUrl)
                //.centerCrop()
                .into(ivProfilePic);

        ctvUserName.setText(currUser.getDisplayName());
    }

    private void instantiateRV() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AchievementsRecyclerViewAdapter(achievements);
        recyclerView.setAdapter(adapter);

        RVScrollController scrollController = new RVScrollController();
        scrollController.addControlToBottomNavigation(recyclerView);
    }
}
