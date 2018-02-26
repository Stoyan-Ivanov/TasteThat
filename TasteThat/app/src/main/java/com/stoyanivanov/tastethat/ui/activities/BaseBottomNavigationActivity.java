package com.stoyanivanov.tastethat.ui.activities;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.constants.StartConstants;

public abstract class BaseBottomNavigationActivity extends AppCompatActivity {

    public static BottomNavigationView bottomNavigationView;
    public static String fragmentTag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!activityIsProperlyStarted(getIntent())) {
            try {
                throw new Exception("Activity is not properly started!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        initBottomNavigation();
        addControlToBottomNavigation();
    }

    private void initBottomNavigation() {
        Intent intent = getIntent();

        int bottomNavOption = intent.getIntExtra(StartConstants.EXTRA_NAV_OPTION, 0);
        fragmentTag = intent.getStringExtra(StartConstants.EXTRA_FRAGMENT_TAG);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setSelectedBottomNavOption(bottomNavOption);
    }

    protected void setSelectedBottomNavOption(int bottomNavOption) {
        bottomNavigationView.setSelectedItemId(bottomNavOption);
    }

    protected void addControlToBottomNavigation() {

        bottomNavigationView.setOnNavigationItemSelectedListener (
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        item.setEnabled(true);

                        switch (item.getItemId()) {
                            case R.id.nav_button_home:
                                startActivity(MainActivity.getIntent(getBaseContext(),
                                        BottomNavigationOptions.HOME, FragmentTags.HOME_FRAGMENT));
                                break;

                            case R.id.nav_button_profile:
                                startActivity(MainActivity.getIntent(getBaseContext(),
                                        BottomNavigationOptions.MY_PROFILE, FragmentTags.MY_PROFILE_FRAGMENT));
                                break;

                            case R.id.nav_button_options:
                                startActivity(MainActivity.getIntent(getBaseContext(),
                                        BottomNavigationOptions.OPTIONS, FragmentTags.OPTIONS_FRAGMENT));
                                break;
                        }

                        finish();
                        return true;
                    }
                });
    }

    private boolean activityIsProperlyStarted(Intent intent) {
        return intent.getStringExtra(StartConstants.EXTRA_FLAG) != null;
    }

    protected void clearBackstack() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }
}
