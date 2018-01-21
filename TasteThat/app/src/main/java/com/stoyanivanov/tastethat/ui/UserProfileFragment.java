package com.stoyanivanov.tastethat.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.stoyanivanov.tastethat.activities.MainActivity;
import com.stoyanivanov.tastethat.activities.MyProfileActivity;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.interfaces.OnClickViewHolder;
import com.stoyanivanov.tastethat.models.Achievement;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.rv_adapters.MyRecyclerViewAdapter;
import com.stoyanivanov.tastethat.view_utils.rv_adapters.UserAchievementsRecyclerViewAdapter;

import java.util.ArrayList;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.tableUsers;

public class UserProfileFragment extends Fragment {

    private String userId;
    private Combination currCombination;
    private RecyclerView recyclerViewAchievements;
    private RecyclerView recyclerViewCombinations;
    private ArrayList<Combination> uploadedCombinations;
    private ArrayList<Combination> likedCombinations;
    private ArrayList<Achievement> achievements = new ArrayList<>();
    private CustomTextView uploadsBtn;
    private CustomTextView likesBtn;
    private MyRecyclerViewAdapter adapter;
    private UserAchievementsRecyclerViewAdapter achievementsAdapter;
    private String activityName;

     public static UserProfileFragment newInstance(String activityName, Combination combination) {
        Bundle arguments = new Bundle();
        UserProfileFragment fragment = new UserProfileFragment();

        arguments.putString(StartConstants.EXTRA_ACTIVITY_NAME, activityName);
        arguments.putSerializable(StartConstants.EXTRA_FRAGMENT_COMBINATION, combination);

        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        recyclerViewAchievements = (RecyclerView) view.findViewById(R.id.rv_horizontal_achievements);
        recyclerViewCombinations = (RecyclerView) view.findViewById(R.id.rv_user_combinations);
        uploadsBtn = (CustomTextView) view.findViewById(R.id.ctv_user_uploads);
        likesBtn = (CustomTextView) view.findViewById(R.id.ctv_user_likes);

        getExtraArguments();

        try {
            if(currCombination != null) {
                userId = currCombination.getUserId();
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        instantiateButtons();
        populateAchievementsRV();
        populateCombinationsRV();

        return view;
    }

    private void instantiateButtons() {
        uploadsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadedCombinations == null) {
                    getUserUploadedCombinations();
                }

                setPurpleColorToText(uploadsBtn);
                setBlackColorToText(likesBtn);

                adapter.setNewData(uploadedCombinations);
            }
        });

        likesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(likedCombinations == null) {
                    getUserLikedCombinations();
                }

                setPurpleColorToText(likesBtn);
                setBlackColorToText(uploadsBtn);

                adapter.setNewData(likedCombinations);
            }
        });
    }

    private void getExtraArguments() {
        activityName = getArguments().getString(StartConstants.EXTRA_ACTIVITY_NAME);
        currCombination = (Combination) getArguments().getSerializable(StartConstants.EXTRA_FRAGMENT_COMBINATION);
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

        tableUsers.child(userId)
                .child(Constants.USER_ACHIEVEMENTS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
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
    }

    private void populateCombinationsRV() {
        ArrayList<Combination> defaultCombinations = defaultClickedSection();

        recyclerViewCombinations.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new MyRecyclerViewAdapter(Constants.RV_LIKED_COMBINATIONS, defaultCombinations, new OnClickViewHolder() {
            @Override
            public void onItemClick(Combination combination, CustomTextView likeCounter, int position) {}

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
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
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
                        Log.d("SII", likedCombinations.toString());
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
