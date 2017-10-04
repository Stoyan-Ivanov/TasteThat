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
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.stoyanivanov.tastethat.ui.AddCombinationFragment;
import com.stoyanivanov.tastethat.ui.CombinationsFragment;
import com.stoyanivanov.tastethat.ui.UserProfileFragment;
import com.stoyanivanov.tastethat.view_utils.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    BottomNavigationView bottomNavigationView;

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

        replaceFragment(new CombinationsFragment());


        instantiateBottomNav();


    }

    private void instantiateBottomNav() {
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_button_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_button_add:
                                replaceFragment(new AddCombinationFragment()); break;

                            case R.id.nav_button_home:
                                replaceFragment(new CombinationsFragment()); break;

                            case R.id.nav_button_profile:
                                replaceFragment(new UserProfileFragment()); break;

                             case R.id.nav_button_options:
                                  ; break;
                        }
                        return true;
                    }
                });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
