package com.stoyanivanov.tastethat;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

    public void init() {
        Intent intent = getIntent();
        int bottomNavOption = intent.getIntExtra("bottom_nav_option", 0);
        fragmentTag = intent.getStringExtra("fragment_tag");

        instantiateBottomNavBar();
        setSelectedBottomNavOption(bottomNavOption);
    }

    private void setSelectedBottomNavOption(int bottomNavOption) {
        bottomNavigationView.setSelectedItemId(bottomNavOption);
    }

    private void instantiateBottomNavBar() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
    }
}
