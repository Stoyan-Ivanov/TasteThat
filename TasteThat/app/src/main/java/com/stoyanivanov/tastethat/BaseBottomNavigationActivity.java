package com.stoyanivanov.tastethat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BaseBottomNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_bottom_navigation);
    }
}
