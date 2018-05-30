package com.stoyanivanov.tastethat.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.ui.fragments.RatedCombinationsFragment;
import com.stoyanivanov.tastethat.ui.fragments.UploadedCombinationsFragment;

public class MyProfileActivity extends BaseFragmentContainerActivity {

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
        setContentView(R.layout.activity_base_layout);

        replaceFragment(fragmentToDisplay());
    }


    private Fragment fragmentToDisplay() {
        Fragment fragment = null;

        switch(fragmentTag) {
            case FragmentTags.RATED_COMBINATIONS_FRAGMENT: fragment = new RatedCombinationsFragment(); break;

            case FragmentTags.UPLOADS_FRAGMENT : fragment = new UploadedCombinationsFragment(); break;
        }

        return fragment;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
