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
import com.stoyanivanov.tastethat.ui.base_ui.BaseFragment;
import com.stoyanivanov.tastethat.view_utils.controllers.RVScrollController;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.OnClickViewHolder;
import com.stoyanivanov.tastethat.db.models.Achievement;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.CombinationsRecyclerViewAdapter;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.decoration.SpacesItemDecoration;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.user_achievements_recyclerview.UserAchievementsRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.nodeUsers;

public class UserProfileFragment extends BaseFragment {

    @BindView(R.id.rv_horizontal_achievements) RecyclerView mRecyclerViewAchievements;
    @BindView(R.id.rv_user_combinations) RecyclerView mRecyclerViewCombinations;
    @BindView(R.id.tv_user_uploads) TextView mUploadsBtn;
    @BindView(R.id.tv_user_rated) TextView mLikesBtn;
    @BindView(R.id.ctv_username) TextView mUsername;

    private String mUserId;
    private Combination mCurrCombination;
    private String mActivityName;

    private ArrayList<Combination> mUploadedCombinations;
    private ArrayList<Combination> mRatedCombinations;
    private ArrayList<Achievement> mAchievements = new ArrayList<>();

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

    @OnClick(R.id.iv_back_arrow)
    void onBackArrowPressed() {
         popCurrentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_user_profile, inflater, container);

        getExtraArguments();
        mUserId = mCurrCombination.getUserId();

        instantiateButtons();
        populateAchievementsRV();
        populateCombinationsRV();

        mUsername.setText(mCurrCombination.getUsername());

        return view;
    }

    private void instantiateButtons() {
        mUploadsBtn.setOnClickListener(v -> {
            if(mUploadedCombinations == null) {
                getUserUploadedCombinations();
            }

            setPurpleColorToText(mUploadsBtn);
            setBlackColorToText(mLikesBtn);

            adapter.setNewData(mUploadedCombinations);
        });

        mLikesBtn.setOnClickListener(v -> {
            if(mRatedCombinations == null) {
                getUserRatedCombinations();
            }

            setPurpleColorToText(mLikesBtn);
            setBlackColorToText(mUploadsBtn);

            adapter.setNewData(mRatedCombinations);
        });
    }

    private void getExtraArguments() {
        mActivityName = getArguments().getString(StartConstants.EXTRA_ACTIVITY_NAME);
        mCurrCombination = getArguments().getParcelable(StartConstants.EXTRA_FRAGMENT_COMBINATION);
    }

    private void populateAchievementsRV() {
        getUserAchievements();

        mRecyclerViewAchievements.setLayoutManager(new LinearLayoutManager(getActivity(),
                                    LinearLayoutManager.HORIZONTAL, false));

        achievementsAdapter = new UserAchievementsRecyclerViewAdapter(mAchievements);
        mRecyclerViewAchievements.setAdapter(achievementsAdapter);
        mRecyclerViewAchievements.addItemDecoration(new SpacesItemDecoration(8, SpacesItemDecoration.HORIZONTAL));
    }

    private void getUserAchievements() {
        mAchievements.clear();

        if(mUserId != null) {
            nodeUsers.child(mUserId)
                    .child(Constants.USER_ACHIEVEMENTS)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Achievement currAchievement = dataSnapshot.getValue(Achievement.class);
                                mAchievements.add(currAchievement);
                            }
                            achievementsAdapter.setNewData(mAchievements);
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewCombinations.setLayoutManager(linearLayoutManager);

        adapter = new CombinationsRecyclerViewAdapter(Constants.RV_RATED_COMBINATIONS, defaultCombinations, new OnClickViewHolder() {
            @Override
            public void onRateButtonClicked(Combination combination) {
                if(mActivityName.equals(MainActivity.class.getSimpleName())) {
                    ((MainActivity) getActivity()).replaceFragment(RateCombinationFragment.newInstance(combination));
                } else {
                    if(mActivityName.equals(MyProfileActivity.class.getSimpleName())) {
                        ((MyProfileActivity) getActivity()).replaceFragment(RateCombinationFragment.newInstance(combination));
                    }
                }
            }

            @Override
            public void onItemClick(Combination combination) {
                if(mActivityName.equals(MainActivity.class.getSimpleName())) {
                    ((MainActivity) getActivity())
                            .replaceFragment(CombinationDetailsFragment.newInstance(mActivityName, combination));
                } else {
                    if(mActivityName.equals(MyProfileActivity.class.getSimpleName())) {
                        ((MyProfileActivity) getActivity())
                                .replaceFragment(CombinationDetailsFragment.newInstance(mActivityName, combination));
                    }
                }
            }
        });

        mRecyclerViewCombinations.setAdapter(adapter);
        mRecyclerViewCombinations.addItemDecoration(new SpacesItemDecoration(16, SpacesItemDecoration.VERTICAL));
        RVScrollController controller = new RVScrollController();
        controller.addControlToBottomNavigation(mRecyclerViewCombinations);
    }

    private ArrayList<Combination> defaultClickedSection() {
        getUserUploadedCombinations();

        setPurpleColorToText(mUploadsBtn);
        setBlackColorToText(mLikesBtn);

        return mUploadedCombinations;
    }

    private void getUserUploadedCombinations() {
        mUploadedCombinations = new ArrayList<>();

        nodeUsers.child(mUserId)
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        mUploadedCombinations.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Combination currCombination = dataSnapshot.getValue(Combination.class);
                            mUploadedCombinations.add(currCombination);
                        }

                        adapter.setNewData(mUploadedCombinations);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("SII", "onCancelled: error");
                    }
                });
    }

    private void getUserRatedCombinations() {
        mRatedCombinations = new ArrayList<>();

        nodeUsers.child(mUserId)
                .child(Constants.USER_RATED_COMBINATIONS)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            Combination currCombination = dataSnapshot.getValue(Combination.class);
                            mRatedCombinations.add(currCombination);
                        }
                        adapter.setNewData(mRatedCombinations);
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
