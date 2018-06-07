package com.stoyanivanov.tastethat.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.db.models.Component;
import com.stoyanivanov.tastethat.ui.activities.main_activity.MainActivity;
import com.stoyanivanov.tastethat.ui.activities.MyProfileActivity;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.ui.base_ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class CombinationDetailsFragment extends BaseFragment {

    @BindView(R.id.tv_combination_details_header) TextView mTvCombinationNameHeader;
    @BindView(R.id.iv_back_arrow) ImageView mIvBackArrow;
    @BindView(R.id.tv_details_username) TextView mTvAuthorName;
    @BindView(R.id.tv_combination_details_description) TextView mTvCombinationDescription;
    @BindView(R.id.fab_rate_combination) FloatingActionButton mFabRateCombination;
    @BindView(R.id.iv_edit_combination) ImageView mIvEditCombination;
    @BindViews({R.id.iv_top_left, R.id.iv_top_right,
            R.id.iv_bottom_left, R.id.iv_bottom_right}) List<ImageView> mImages;

    private Combination mCurrCombination;
    private String mActivityName;

    public static Fragment newInstance(String activityName, Combination combination) {
        Bundle arguments = new Bundle();
        CombinationDetailsFragment fragment = new CombinationDetailsFragment();

        arguments.putString(StartConstants.EXTRA_ACTIVITY_NAME, activityName);
        arguments.putParcelable(StartConstants.EXTRA_FRAGMENT_COMBINATION, combination);

        fragment.setArguments(arguments);

        return fragment;
    }

    @OnClick(R.id.fab_rate_combination)
    void onRateCombinationClicked() {
        replaceFragment(RateCombinationFragment.newInstance(mCurrCombination));
    }

    @OnClick(R.id.tv_details_username)
    void inflateNewUserProfileFragment() {
        replaceFragment(UserProfileFragment.newInstance(mActivityName, mCurrCombination));
    }

    private void replaceFragment(BaseFragment fragment) {
        if(mActivityName.equals(MainActivity.class.getSimpleName())) {
            ((MainActivity) getActivity()).replaceFragment(fragment);
        } else {
            if(mActivityName.equals(MyProfileActivity.class.getSimpleName())) {
                ((MyProfileActivity) getActivity()).replaceFragment(fragment);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_combination_details, inflater, container);

        getExtraArguments();

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(mCurrCombination.getUserId())) {
            mFabRateCombination.setVisibility(View.GONE);
            mIvEditCombination.setVisibility(View.VISIBLE);
        }

        loadCombinationName();
        loadAuthorName();
        loadImages();
        loadDescription();
        configureButtons();

        return view;
    }

    private void getExtraArguments() {
        mActivityName = getArguments().getString(StartConstants.EXTRA_ACTIVITY_NAME);
        mCurrCombination = getArguments().getParcelable(StartConstants.EXTRA_FRAGMENT_COMBINATION);
    }

    private void configureButtons() {
        mIvBackArrow.setOnClickListener(v -> {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                fragmentManager.popBackStack();
            }
        });
    }

    private void loadCombinationName() {
        mTvCombinationNameHeader.setText(mCurrCombination.toString());
    }

    private void loadAuthorName() {
        mTvAuthorName.setText(getString(R.string.author_field, mCurrCombination.getUsername()));
    }

    private void loadImages() {
        ArrayList<Component> components = mCurrCombination.getComponents();

        hideImageviewsIfNotUsed(components.size());

        for(int i = 0; i < components.size(); i++) {
            Glide.with(TasteThatApplication.getStaticContext())
                    .load(components.get(i).getComponentImageUrl())
                    .centerCrop()
                    .into(mImages.get(i));
        }
    }

    private void loadDescription() {
        mTvCombinationDescription.setText(mCurrCombination.getDescription());
    }

    //ToDo: REFACTOR AS SOON AS POSSIBLE
    private void hideImageviewsIfNotUsed(int numOfPics) {
        if (numOfPics < 3) {
            mImages.get(2).setVisibility(View.GONE);
            mImages.get(3).setVisibility(View.GONE);
        }
    }
}
