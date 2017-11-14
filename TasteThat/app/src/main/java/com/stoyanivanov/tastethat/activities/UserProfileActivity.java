package com.stoyanivanov.tastethat.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.stoyanivanov.tastethat.constants.StartActivityConstants;
import com.stoyanivanov.tastethat.ui.LikedCombinationsFragment;
import com.stoyanivanov.tastethat.ui.UploadedCombinationsFragment;

public class UserProfileActivity extends BaseBottomNavigationActivity {
    public static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    public static Intent getIntent(Context context, int bottomNavOption, String fragmentTag) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(StartActivityConstants.extraNavOption, bottomNavOption);
        intent.putExtra(StartActivityConstants.extraFragmentTag, fragmentTag);
        intent.putExtra(StartActivityConstants.extraFlag, "started_properly");

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };

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
            case "liked_fragment": fragment = new LikedCombinationsFragment(); break;

            case "uploads_fragment" : fragment = new UploadedCombinationsFragment(); break;

           // case "achivements_fragment" : fragment = new AchivementsFragment(); break;
        }

        return fragment;
    }

    private void addControlToBottomNavigation() {

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        item.setEnabled(true);

                        switch (item.getItemId()) {
                            case R.id.nav_button_add:
                                startActivity(MainActivity.getIntent(getBaseContext(), BottomNavigationOptions.ADD, FragmentTags.ADD_FRAGMENT));
                                break;

                            case R.id.nav_button_home:
                                startActivity(MainActivity.getIntent(getBaseContext(), BottomNavigationOptions.HOME, FragmentTags.HOME_FRAGMENT));
                                break;

                            case R.id.nav_button_profile:
                                startActivity(MainActivity.getIntent(getBaseContext(), BottomNavigationOptions.USER_PROFILE, FragmentTags.USER_FRAGMENT));
                                break;

                            case R.id.nav_button_options:
                                startActivity(MainActivity.getIntent(getBaseContext(), BottomNavigationOptions.OPTIONS, FragmentTags.OPTIONS_FRAGMENT));
                                break;
                        }

                        return true;
                    }
                });
    }

    public static FirebaseUser getCurrentGoogleUser() {
        return mAuth.getCurrentUser();
    }

}
