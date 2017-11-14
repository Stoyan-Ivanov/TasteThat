package com.stoyanivanov.tastethat.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.StartActivityConstants;
import com.stoyanivanov.tastethat.view_utils.BottomNavigationViewHelper;

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
        if(intent.getStringExtra(StartActivityConstants.extraFlag) == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int bottomNavOption = intent.getIntExtra(StartActivityConstants.extraNavOption, 0);
        fragmentTag = intent.getStringExtra(StartActivityConstants.extraFragmentTag);

        instantiateBottomNavBar();
        setSelectedBottomNavOption(bottomNavOption);
    }

    protected void setSelectedBottomNavOption(int bottomNavOption) {
        bottomNavigationView.setSelectedItemId(bottomNavOption);
    }

    protected void instantiateBottomNavBar() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
    }
}
