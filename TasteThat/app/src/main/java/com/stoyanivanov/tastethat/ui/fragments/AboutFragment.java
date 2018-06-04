package com.stoyanivanov.tastethat.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stoyanivanov.tastethat.BuildConfig;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.ui.base_ui.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class AboutFragment extends BaseFragment {
    @BindView(R.id.tv_app_version) TextView mTvAppVersion;
    @BindView(R.id.tv_about_title) TextView mTvAboutTitle;

    public static AboutFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    
    @OnClick(R.id.iv_back_arrow)
    void onBackArrowPressed() {
        popCurrentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflateCurrentView(R.layout.fragment_about, inflater, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        mTvAppVersion.setText(getString(R.string.app_version, BuildConfig.VERSION_NAME));
        mTvAboutTitle.setText(R.string.about_fragment_header);
    }
}
