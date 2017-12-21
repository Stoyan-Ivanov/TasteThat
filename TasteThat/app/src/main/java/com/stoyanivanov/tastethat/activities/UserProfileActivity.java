package com.stoyanivanov.tastethat.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.ui.AchievementsFragment;
import com.stoyanivanov.tastethat.ui.CombinationDetailsFragment;
import com.stoyanivanov.tastethat.ui.LikedCombinationsFragment;
import com.stoyanivanov.tastethat.ui.UploadedCombinationsFragment;

public class UserProfileActivity extends BaseBottomNavigationActivity {

    public static Intent getIntent(Context context, int bottomNavOption, String fragmentTag) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(StartConstants.EXTRA_NAV_OPTION, bottomNavOption);
        intent.putExtra(StartConstants.EXTRA_FRAGMENT_TAG, fragmentTag);
        intent.putExtra(StartConstants.EXTRA_FLAG, StartConstants.EXTRA_FLAG_VALUE);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        init();
        replaceFragment(fragmentToDisplay());
        addControlToBottomNavigation();
    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.user_fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
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
                            case R.id.nav_button_add:
                                startActivity(MainActivity.getIntent(getBaseContext(),
                                        BottomNavigationOptions.ADD, FragmentTags.ADD_FRAGMENT));
                                break;

                            case R.id.nav_button_home:
                                startActivity(MainActivity.getIntent(getBaseContext(),
                                        BottomNavigationOptions.HOME, FragmentTags.HOME_FRAGMENT));
                                break;

                            case R.id.nav_button_profile:
                                startActivity(MainActivity.getIntent(getBaseContext(),
                                        BottomNavigationOptions.USER_PROFILE, FragmentTags.USER_FRAGMENT));
                                break;

                            case R.id.nav_button_options:
                                startActivity(MainActivity.getIntent(getBaseContext(),
                                        BottomNavigationOptions.OPTIONS, FragmentTags.OPTIONS_FRAGMENT));
                                break;
                        }

                        return true;
                    }
                });
    }

    public void inflateDetailsFragment(CombinationDetailsFragment fragment, Combination combination) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(StartConstants.EXTRA_FRAGMENT_COMBINATION,combination);
            fragment.setArguments(bundle);
            replaceFragment(fragment);
    }
}
