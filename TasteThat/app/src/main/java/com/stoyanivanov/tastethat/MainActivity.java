package com.stoyanivanov.tastethat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stoyanivanov.tastethat.constants.StartActivityConstants;
import com.stoyanivanov.tastethat.constants.ViewPagerPages;
import com.stoyanivanov.tastethat.ui.AddCombinationFragment;
import com.stoyanivanov.tastethat.ui.CombinationsFragment;
import com.stoyanivanov.tastethat.ui.OptionsFragment;
import com.stoyanivanov.tastethat.ui.UserProfileFragment;
import com.stoyanivanov.tastethat.view_utils.MyPagerAdapter;

import java.util.ArrayList;

public class MainActivity extends BaseBottomNavigationActivity {

    public static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ViewPager pager;
    private MenuItem prevMenuItem;

    public static Intent getIntent(Context context, int bottomNavOption, String fragmentTag) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(StartActivityConstants.extraNavOption, bottomNavOption);
        intent.putExtra(StartActivityConstants.extraFragmentTag, fragmentTag);
        intent.putExtra(StartActivityConstants.extraFlag, "started_properly");

        return intent;
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };

        addControlToBottomNavigation();
        instantiateViewPager();
        beginViewPagerPage();
    }

    private void addControlToBottomNavigation() {

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        item.setEnabled(true);
                        switch (item.getItemId()) {
                            case R.id.nav_button_add:
                                pager.setCurrentItem(ViewPagerPages.ADD);
                                break;

                            case R.id.nav_button_home:
                                pager.setCurrentItem(ViewPagerPages.HOME);
                                break;

                            case R.id.nav_button_profile:
                                pager.setCurrentItem(ViewPagerPages.USER_PROFILE);
                                break;

                            case R.id.nav_button_options:
                                pager.setCurrentItem(ViewPagerPages.OPTIONS);
                                break;
                        }
                        return true;
                    }
                });
    }

    public static FirebaseUser getCurrentGoogleUser() {
        return mAuth.getCurrentUser();
    }


    public void instantiateViewPager () {
        FragmentPagerAdapter fragmentPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), getFragments());

        pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(fragmentPagerAdapter);
        pager.setOffscreenPageLimit(4);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {

                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void beginViewPagerPage() {
        switch (fragmentTag) {
            case "add_fragment" : pager.setCurrentItem(ViewPagerPages.ADD); break;
            case "home_fragment" : pager.setCurrentItem(ViewPagerPages.HOME); break;
            case "user_fragment" : pager.setCurrentItem(ViewPagerPages.USER_PROFILE); break;
            case "options_fragment" : pager.setCurrentItem(ViewPagerPages.OPTIONS); break;
        }
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(new AddCombinationFragment());
        fragments.add(new CombinationsFragment());
        fragments.add(new UserProfileFragment());
        fragments.add(new OptionsFragment());

        return fragments;
    }
}
