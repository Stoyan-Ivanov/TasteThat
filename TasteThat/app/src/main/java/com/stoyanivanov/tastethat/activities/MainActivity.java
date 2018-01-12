package com.stoyanivanov.tastethat.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.constants.ViewPagerPages;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.ui.AddCombinationFragment;
import com.stoyanivanov.tastethat.ui.AllCombinationsFragment;
import com.stoyanivanov.tastethat.ui.CombinationDetailsFragment;
import com.stoyanivanov.tastethat.ui.OptionsFragment;
import com.stoyanivanov.tastethat.ui.MyProfileFragment;
import com.stoyanivanov.tastethat.view_utils.MyPagerAdapter;

import java.util.ArrayList;

public class MainActivity extends BaseBottomNavigationActivity {
    
    private ViewPager viewPager;
    private MenuItem prevMenuItem;

    public static Intent getIntent(Context context, int bottomNavOption, String fragmentTag) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(StartConstants.EXTRA_NAV_OPTION, bottomNavOption);
        intent.putExtra(StartConstants.EXTRA_FRAGMENT_TAG, fragmentTag);
        intent.putExtra(StartConstants.EXTRA_FLAG, StartConstants.EXTRA_FLAG_VALUE);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        addControlToBottomNavigation();
        instantiateViewPager();
        viewPagerBeginPage();
    }

    private void addControlToBottomNavigation() {

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        item.setEnabled(true);
                        clearBackstack();

                        switch (item.getItemId()) {
                            case R.id.nav_button_add:
                                viewPager.setCurrentItem(ViewPagerPages.ADD);
                                break;

                            case R.id.nav_button_home:
                                viewPager.setCurrentItem(ViewPagerPages.HOME);
                                break;

                            case R.id.nav_button_profile:
                                viewPager.setCurrentItem(ViewPagerPages.MY_PROFILE);
                                break;

                            case R.id.nav_button_options:
                                viewPager.setCurrentItem(ViewPagerPages.OPTIONS);
                                break;
                        }
                        return true;
                    }
                });
    }

    public void instantiateViewPager () {
        ArrayList<Fragment> fragments = getFragments();
        FragmentPagerAdapter fragmentPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(fragments.size());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                hideVirtualKeyboard();
            }

            @Override
            public void onPageSelected(int position) {

                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void viewPagerBeginPage() {
        switch (fragmentTag) {
            case FragmentTags.ADD_FRAGMENT : viewPager.setCurrentItem(ViewPagerPages.ADD); break;
            case FragmentTags.HOME_FRAGMENT : viewPager.setCurrentItem(ViewPagerPages.HOME); break;
            case FragmentTags.MY_PROFILE_FRAGMENT: viewPager.setCurrentItem(ViewPagerPages.MY_PROFILE); break;
            case FragmentTags.OPTIONS_FRAGMENT : viewPager.setCurrentItem(ViewPagerPages.OPTIONS); break;
        }
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(new AddCombinationFragment());
        fragments.add(new AllCombinationsFragment());
        fragments.add(new MyProfileFragment());
        fragments.add(new OptionsFragment());

        return fragments;
    }

    private void hideVirtualKeyboard() {
        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
    }

    private void replaceFragment(Fragment fragment) {
        bottomNavVisibilityCheck();

        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void bottomNavVisibilityCheck() {
        if(bottomNavigationView.getVisibility() == View.GONE) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    public void inflateExtraCombinationFragment(Fragment fragment, Combination combination) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(StartConstants.EXTRA_FRAGMENT_COMBINATION,combination);
            fragment.setArguments(bundle);
            replaceFragment(fragment);
    }
}
