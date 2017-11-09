package com.stoyanivanov.tastethat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.ui.LikedCombinationsFragment;
import com.stoyanivanov.tastethat.ui.UploadedCombinationsFragment;

public class UserProfileActivity extends BaseBottomNavigationActivity {
    public static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


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
                        Intent intent = null;

                        switch (item.getItemId()) {
                            case R.id.nav_button_add:
                                intent = getIntentWithExtras(BottomNavigationOptions.ADD, FragmentTags.ADD_FRAGMENT);
                                break;

                            case R.id.nav_button_home:
                                intent = getIntentWithExtras(BottomNavigationOptions.HOME, FragmentTags.HOME_FRAGMENT);
                                break;

                            case R.id.nav_button_profile:
                                intent = getIntentWithExtras(BottomNavigationOptions.USER_PROFILE, FragmentTags.USER_FRAGMENT);
                                break;

                            case R.id.nav_button_options:
                                intent = getIntentWithExtras(BottomNavigationOptions.OPTIONS, FragmentTags.OPTIONS_FRAGMENT);
                                break;
                        }
                        startActivity(intent);

                        return true;
                    }
                });
    }

    private Intent getIntentWithExtras(final int bottomNavOption, final String fragmentTag) {
        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
        intent.putExtra("bottom_nav_option", bottomNavOption);
        intent.putExtra("fragment_tag", fragmentTag);

        return intent;
    }

    public static FirebaseUser getCurrentGoogleUser() {
        return mAuth.getCurrentUser();
    }

}
