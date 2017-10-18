package com.stoyanivanov.tastethat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stoyanivanov.tastethat.ui.AddCombinationFragment;
import com.stoyanivanov.tastethat.ui.CombinationsFragment;
import com.stoyanivanov.tastethat.ui.UserProfileFragment;
import com.stoyanivanov.tastethat.view_utils.BottomNavigationViewHelper;
import com.stoyanivanov.tastethat.view_utils.MyPagerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private BottomNavigationView bottomNavigationView;
    private ViewPager pager;
    private MenuItem prevMenuItem;

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button logout = (Button) findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        instantiateBottomNavBar();
        instantiateViewPager();
    }

    public FirebaseUser getCurrentGoogleUser() {
        return mAuth.getCurrentUser();
    }

    private void instantiateBottomNavBar() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_button_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        item.setEnabled(true);
                        switch (item.getItemId()) {
                            case R.id.nav_button_add:
                                pager.setCurrentItem(0);
                                break;

                            case R.id.nav_button_home:
                                pager.setCurrentItem(1);
                                break;

                            case R.id.nav_button_profile:
                                pager.setCurrentItem(2);
                                break;

                             case R.id.nav_button_options:
                                 // IMPLEMENT SMALL MENU OVERLAY!
                                  ; break;
                        }
                        return true;
                    }
                });
    }

    public void instantiateViewPager () {
        FragmentPagerAdapter fragmentPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), getFragments());

        pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(fragmentPagerAdapter);
        pager.setCurrentItem(1);

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

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(new AddCombinationFragment());
        fragments.add(new CombinationsFragment());
        fragments.add(new UserProfileFragment());

        return fragments;
    }

    public BottomNavigationView getBottomNavigation() {
        return bottomNavigationView;
    }
}
