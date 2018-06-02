package com.stoyanivanov.tastethat.ui.activities.main_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.constants.ViewPagerPages;
import com.stoyanivanov.tastethat.ui.activities.BaseFragmentContainerActivity;
import com.stoyanivanov.tastethat.view_utils.MyFragmentPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseFragmentContainerActivity {

    @BindView(R.id.view_pager) ViewPager mViewPager;
    private MenuItem mPrevMenuItem;
    private MainActivityPresenter mPresenter;

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

        mPresenter = new MainActivityPresenter();

        addControlToBottomNavigation();
        instantiateViewPager();
        mViewPager.setCurrentItem(mPresenter.determineViewPagerBeginPage(fragmentTag));
    }

    @Override
    protected void addControlToBottomNavigation() {

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    item.setEnabled(true);
                    clearBackstack();

                    switch (item.getItemId()) {
                        case R.id.nav_button_home:
                            mViewPager.setCurrentItem(ViewPagerPages.HOME); break;

                        case R.id.nav_button_profile:
                            mViewPager.setCurrentItem(ViewPagerPages.MY_PROFILE); break;

                        case R.id.nav_button_options:
                            mViewPager.setCurrentItem(ViewPagerPages.OPTIONS); break;
                    }
                    return true;
                });
    }

    private void instantiateViewPager () {
        ArrayList<Fragment> fragments = mPresenter.getFragments();
        FragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);

        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(fragments.size());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                TasteThatApplication.hideVirtualKeyboard(mViewPager);
                bottomNavVisibilityCheck();
            }

            @Override
            public void onPageSelected(int position) {

                if (mPrevMenuItem != null) {
                    mPrevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                mPrevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    public void replaceFragment(Fragment fragment) {
        bottomNavVisibilityCheck();
        super.replaceFragment(fragment);
    }

    private void bottomNavVisibilityCheck() {
        if(bottomNavigationView.getVisibility() == View.GONE) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }
}
