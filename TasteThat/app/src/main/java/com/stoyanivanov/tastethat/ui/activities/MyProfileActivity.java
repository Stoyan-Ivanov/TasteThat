package com.stoyanivanov.tastethat.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.ui.fragments.AchievementsFragment;
import com.stoyanivanov.tastethat.ui.fragments.LikedCombinationsFragment;
import com.stoyanivanov.tastethat.ui.fragments.UploadedCombinationsFragment;

public class MyProfileActivity extends BaseBottomNavigationActivity {

    public static Intent getIntent(Context context, int bottomNavOption, String fragmentTag) {
        Intent intent = new Intent(context, MyProfileActivity.class);
        intent.putExtra(StartConstants.EXTRA_NAV_OPTION, bottomNavOption);
        intent.putExtra(StartConstants.EXTRA_FRAGMENT_TAG, fragmentTag);
        intent.putExtra(StartConstants.EXTRA_FLAG, StartConstants.EXTRA_FLAG_VALUE);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        replaceFragment(fragmentToDisplay());
        addControlToBottomNavigation();
    }


    private Fragment fragmentToDisplay() {
        Fragment fragment = null;

        switch(fragmentTag) {
            case FragmentTags.LIKED_FRAGMENT : fragment = new LikedCombinationsFragment(); break;

            case FragmentTags.UPLOADS_FRAGMENT : fragment = new UploadedCombinationsFragment(); break;

            case FragmentTags.ACHIEVEMENTS_FRAGMENT : fragment = new AchievementsFragment(); break;
        }

        return fragment;
    }

    private void addControlToBottomNavigation() {

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

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.user_fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(MainActivity.getIntent(getBaseContext(),
                BottomNavigationOptions.MY_PROFILE, FragmentTags.MY_PROFILE_FRAGMENT));

        finish();
    }
}
