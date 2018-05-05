package com.stoyanivanov.tastethat.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.db.models.Component;
import com.stoyanivanov.tastethat.ui.activities.main_activity.MainActivity;
import com.stoyanivanov.tastethat.ui.activities.MyProfileActivity;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class CombinationDetailsFragment extends BaseFragment {

    @BindView(R.id.ctv_combination_details_header) CustomTextView combinationNameHeader;
    @BindView(R.id.iv_back_arrow) ImageView backArrow;
    @BindView(R.id.ctv_details_username) CustomTextView authorName;
    @BindView(R.id.ctv_combination_details_description) CustomTextView combinationDescription;
    @BindViews({R.id.iv_top_left, R.id.iv_top_right,
            R.id.iv_bottom_left, R.id.iv_bottom_right}) List<ImageView> images;

    private Combination currCombination;
    private String activityName;

    public static Fragment newInstance(String activityName, Combination combination) {
        Bundle arguments = new Bundle();
        CombinationDetailsFragment fragment = new CombinationDetailsFragment();

        arguments.putString(StartConstants.EXTRA_ACTIVITY_NAME, activityName);
        arguments.putParcelable(StartConstants.EXTRA_FRAGMENT_COMBINATION, combination);
        fragment.setArguments(arguments);

        return fragment;
    }

    @OnClick(R.id.ctv_details_username)
        void inflateNewUserProfileFragment() {
            if(activityName.equals(MainActivity.class.getSimpleName())) {
                ((MainActivity) getActivity())
                        .replaceFragment(UserProfileFragment.newInstance(activityName, currCombination));
            } else {
                if(activityName.equals(MyProfileActivity.class.getSimpleName())) {
                    ((MyProfileActivity) getActivity())
                            .replaceFragment(UserProfileFragment.newInstance(activityName, currCombination));
                }
            }
        }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_combination_details, inflater, container);

        getExtraArguments();
        loadCombinationName();
        loadAuthorName();
        loadImages();
        loadDescription();
        configureButtons();

        return view;
    }

    private void getExtraArguments() {
        activityName = getArguments().getString(StartConstants.EXTRA_ACTIVITY_NAME);

        currCombination = getArguments().getParcelable(StartConstants.EXTRA_FRAGMENT_COMBINATION);
    }

    private void configureButtons() {
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    fragmentManager.popBackStack();
                }
            }
        });
    }

    private void loadCombinationName() {
        combinationNameHeader.setText(currCombination.toString());
    }

    private void loadAuthorName() {
        String authorField = getString(R.string.author_field) + currCombination.getUsername();
        authorName.setText(authorField);
    }

    private void loadImages() {
        ArrayList<Component> components = currCombination.getComponents();

        hideImageviewsIfNotUsed(components.size());

        for(int i = 0; i < components.size(); i++) {
            Glide.with(TasteThatApplication.getStaticContext())
                    .load(components.get(i).getComponentImageUrl())
                    .centerCrop()
                    .into(images.get(i));
        }
    }

    private void loadDescription() {
    }

    //ToDo: REFACTOR AS SOON AS POSSIBLE
    private void hideImageviewsIfNotUsed(int numOfPics) {
        if (numOfPics < 3) {
            images.get(2).setVisibility(View.GONE);
            images.get(3).setVisibility(View.GONE);
        }
    }
}
