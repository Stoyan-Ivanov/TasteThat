package com.stoyanivanov.tastethat.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.db.DatabaseProvider;
import com.stoyanivanov.tastethat.db.models.Combination;

import butterknife.BindView;
import butterknife.OnClick;

public class RateCombinationFragment extends BaseFragment {
    @BindView(R.id.tv_combination_name) TextView tvCombinationName;
    @BindView(R.id.btn_submit_rating) TextView btnSubmitRating;
    @BindView(R.id.rating_bar) RatingBar ratingBar;

    private Combination mCombination;

    @OnClick(R.id.btn_submit_rating)
    void submitRating() {
        float rating = ratingBar.getRating();

        DatabaseProvider.getInstance().saveRatingForCombination(mCombination, rating);

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.popBackStack();
        }
    }


    public static RateCombinationFragment newInstance(Combination combination) {
        Bundle args = new Bundle();
        args.putParcelable(StartConstants.EXTRA_FRAGMENT_COMBINATION, combination);
        RateCombinationFragment fragment = new RateCombinationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_rate_combination, inflater, container);
        getExtraArguments();

        tvCombinationName.setText(mCombination.toString());


        return view;
    }

    private void getExtraArguments() {
        mCombination = getArguments().getParcelable(StartConstants.EXTRA_FRAGMENT_COMBINATION);
    }
}
