package com.stoyanivanov.tastethat.ui.activities;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.StartConstants;

public abstract class BaseBottomNavigationActivity extends AppCompatActivity {

    public static BottomNavigationView bottomNavigationView;
    public static String fragmentTag;

    public BaseBottomNavigationActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_bottom_navigation);
    }

    protected void init() {
        Intent intent = getIntent();
        if(intent.getStringExtra(StartConstants.EXTRA_FLAG) == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int bottomNavOption = intent.getIntExtra(StartConstants.EXTRA_NAV_OPTION, 0);
        fragmentTag = intent.getStringExtra(StartConstants.EXTRA_FRAGMENT_TAG);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setSelectedBottomNavOption(bottomNavOption);
    }

    protected void setSelectedBottomNavOption(int bottomNavOption) {
        bottomNavigationView.setSelectedItemId(bottomNavOption);
    }

    protected void clearBackstack() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }
}
