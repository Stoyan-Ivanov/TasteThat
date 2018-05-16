package com.stoyanivanov.tastethat.ui.fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.ui.activities.main_activity.MainActivity;
import com.stoyanivanov.tastethat.ui.activities.MyProfileActivity;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.OnClickViewHolder;
import com.stoyanivanov.tastethat.db.models.Achievement;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.CombinationsRecyclerViewAdapter;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.user_achievements_recyclerview.UserAchievementsRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.tableUsers;

public class UserProfileFragment extends BaseFragment {

    @BindView(R.id.rv_horizontal_achievements) RecyclerView recyclerViewAchievements;
    @BindView(R.id.rv_user_combinations) RecyclerView recyclerViewCombinations;
    @BindView(R.id.ctv_user_uploads) CustomTextView uploadsBtn;
    @BindView(R.id.ctv_user_likes) CustomTextView likesBtn;
    @BindView(R.id.ctv_username) CustomTextView username;

    private String userId;
    private Combination currCombination;
    private String activityName;

    private ArrayList<Combination> uploadedCombinations;
    private ArrayList<Combination> likedCombinations;
    private ArrayList<Achievement> achievements = new ArrayList<>();

    private CombinationsRecyclerViewAdapter adapter;
    private UserAchievementsRecyclerViewAdapter achievementsAdapter;

     public static UserProfileFragment newInstance(String activityName, Combination combination) {
        Bundle arguments = new Bundle();
        UserProfileFragment fragment = new UserProfileFragment();

        arguments.putString(StartConstants.EXTRA_ACTIVITY_NAME, activityName);
        arguments.putParcelable(StartConstants.EXTRA_FRAGMENT_COMBINATION, combination);

        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_user_profile, inflater, container);

        getExtraArguments();
        userId = currCombination.getUserId();

        instantiateButtons();
        populateAchievementsRV();
        populateCombinationsRV();

        username.setText(currCombination.getUsername());

        return view;
    }

    private void instantiateButtons() {
        uploadsBtn.setOnClickListener(v -> {
            if(uploadedCombinations == null) {
                getUserUploadedCombinations();
            }

            setPurpleColorToText(uploadsBtn);
            setBlackColorToText(likesBtn);

            adapter.setNewData(uploadedCombinations);
        });

        likesBtn.setOnClickListener(v -> {
            if(likedCombinations == null) {
                getUserLikedCombinations();
            }

            setPurpleColorToText(likesBtn);
            setBlackColorToText(uploadsBtn);

            adapter.setNewData(likedCombinations);
        });
    }

    private void getExtraArguments() {
        activityName = getArguments().getString(StartConstants.EXTRA_ACTIVITY_NAME);
        currCombination = getArguments().getParcelable(StartConstants.EXTRA_FRAGMENT_COMBINATION);
    }

    private void populateAchievementsRV() {
        getUserAchievements();

        recyclerViewAchievements.setLayoutManager(new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.HORIZONTAL, false));

        achievementsAdapter = new UserAchievementsRecyclerViewAdapter(achievements);
        recyclerViewAchievements.setAdapter(achievementsAdapter);
    }

    private void getUserAchievements() {
        achievements.clear();

        if(userId != null) {
            tableUsers.child(userId)
                    .child(Constants.USER_ACHIEVEMENTS)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Achievement currAchievement = dataSnapshot.getValue(Achievement.class);
                                achievements.add(currAchievement);
                            }
                            achievementsAdapter.setNewData(achievements);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("SII", "onCancelled: error");
                        }
                    });
        } else {
            throw new NullPointerException("Provide non-null user id!");
        }
    }

    private void populateCombinationsRV() {
        ArrayList<Combination> defaultCombinations = defaultClickedSection();

        recyclerViewCombinations.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new CombinationsRecyclerViewAdapter(Constants.RV_LIKED_COMBINATIONS, defaultCombinations, new OnClickViewHolder() {
            @Override
            public void onItemClick(Combination combination, TextView likeCounter, int position) {}

            @Override
            public void onItemLongClick(Combination combination) {
                if(activityName.equals(MainActivity.class.getSimpleName())) {
                    ((MainActivity) getActivity())
                            .replaceFragment(CombinationDetailsFragment.newInstance(activityName, currCombination));
                } else {
                    if(activityName.equals(MyProfileActivity.class.getSimpleName())) {
                        ((MyProfileActivity) getActivity())
                                .replaceFragment(CombinationDetailsFragment.newInstance(activityName, currCombination));
                    }
                }
            }
        });

        recyclerViewCombinations.setAdapter(adapter);
    }

    private ArrayList<Combination> defaultClickedSection() {
        getUserUploadedCombinations();

        setPurpleColorToText(uploadsBtn);
        setBlackColorToText(likesBtn);

        return uploadedCombinations;
    }

    private void getUserUploadedCombinations() {
        uploadedCombinations = new ArrayList<>();

        tableUsers.child(userId)
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        uploadedCombinations.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Combination currCombination = dataSnapshot.getValue(Combination.class);
                            uploadedCombinations.add(currCombination);
                        }

                        adapter.setNewData(uploadedCombinations);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("SII", "onCancelled: error");
                    }
                });
    }

    private void getUserLikedCombinations() {
        likedCombinations = new ArrayList<>();

        tableUsers.child(userId)
                .child(Constants.USER_LIKED_COMBINATIONS)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            Combination currCombination = dataSnapshot.getValue(Combination.class);
                            likedCombinations.add(currCombination);
                        }
                        adapter.setNewData(likedCombinations);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("SII", "onCancelled: error");
                    }
                });
    }

    private void setPurpleColorToText(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.colorSecondaryPurple));
    }

    private void setBlackColorToText(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.black));
    }
}
