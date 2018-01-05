package com.stoyanivanov.tastethat.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.R;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        recyclerViewAchievements = (RecyclerView) view.findViewById(R.id.rv_horizontal_achievements);
        recyclerViewCombinations = (RecyclerView) view.findViewById(R.id.rv_user_combinations);
        uploadsBtn = (CustomTextView) view.findViewById(R.id.ctv_user_uploads);
        likesBtn = (CustomTextView) view.findViewById(R.id.ctv_user_likes);

        currCombination = (Combination) getArguments().getSerializable(StartConstants.EXTRA_FRAGMENT_COMBINATION);
        userId = currCombination.getUserId();

        Log.d("SII", "onCreateView: " + userId);

        instantiateAchievementsRV();
//        getUploadedCombinations();
//        instantiateCombinationsRV();
//
//        uploadsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(uploadedCombinations == null) {
//                    getUploadedCombinations();
//                }
//                adapter.setNewData(uploadedCombinations);
//            }
//        });
//
//        likesBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(likedCombinations == null) {
//                    getLikedCombinations();
//                }
//                adapter.setNewData(likedCombinations);
//            }
//        });

        return view;
    }

    private void instantiateAchievementsRV() {
        recyclerViewAchievements.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        getAchievements();
        achievementsAdapter = new UserAchievementsRecyclerViewAdapter(achievements);
        recyclerViewAchievements.setAdapter(achievementsAdapter);
    }

    private void getAchievements() {
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

    private void instantiateCombinationsRV() {
        recyclerViewCombinations.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new MyRecyclerViewAdapter(Constants.RV_LIKED_COMBINATIONS, uploadedCombinations, new OnClickViewHolder() {
            @Override
            public void onItemClick(Combination combination, CustomTextView likeCounter, int position) {
            }

            @Override
            public void onItemLongClick(Combination combination) {
                ((MyProfileActivity) getActivity())
                        .inflateDetailsFragment(new CombinationDetailsFragment(), combination);
            }
        });

        recyclerViewCombinations.setAdapter(adapter);
    }

    private void getUploadedCombinations() {
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

    private void getLikedCombinations() {
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
                        Log.d("SII", likedCombinations.toString());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("SII", "onCancelled: error");
                    }
                });
    }
}
