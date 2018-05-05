package com.stoyanivanov.tastethat.ui.activities.main_activity;

import android.support.v4.app.Fragment;

import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.constants.ViewPagerPages;
import com.stoyanivanov.tastethat.ui.base_ui.BasePresenter;
import com.stoyanivanov.tastethat.ui.fragments.AllCombinationsFragment;
import com.stoyanivanov.tastethat.ui.fragments.MyProfileFragment;
import com.stoyanivanov.tastethat.ui.fragments.OptionsFragment;

import java.util.ArrayList;

/**
 * Created by Stoya on 5.5.2018 г..
 */

public class MainActivityPresenter extends BasePresenter {

    public MainActivityPresenter() {

    }

    public ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(new AllCombinationsFragment());
        fragments.add(new MyProfileFragment());
        fragments.add(new OptionsFragment());

        return fragments;
    }

    public int determineViewPagerBeginPage(String fragmentTag) {
        switch (fragmentTag) {
            case FragmentTags.HOME_FRAGMENT : return ViewPagerPages.HOME;
            case FragmentTags.MY_PROFILE_FRAGMENT: return ViewPagerPages.MY_PROFILE;
            case FragmentTags.OPTIONS_FRAGMENT : return ViewPagerPages.OPTIONS;
            default: return ViewPagerPages.HOME;
        }
    }
}
