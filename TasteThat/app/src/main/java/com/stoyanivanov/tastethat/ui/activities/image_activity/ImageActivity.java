package com.stoyanivanov.tastethat.ui.activities.image_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ServerValue;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.DatabaseReferences;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.db.DatabaseProvider;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.db.models.Component;
import com.stoyanivanov.tastethat.ui.activities.BaseFragmentContainerActivity;
import com.stoyanivanov.tastethat.ui.activities.main_activity.MainActivity;
import com.stoyanivanov.tastethat.ui.fragments.ChooseImageFragment;

import java.util.ArrayList;

public class ImageActivity extends BaseFragmentContainerActivity implements  ImageActivityContract{
    private ImageActivityPresenter mPresenter;

    public static Intent getIntent(Context context, String fragmentTag, ArrayList<Component> components, String description) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(StartConstants.EXTRA_FRAGMENT_TAG, fragmentTag);
        intent.putExtra(StartConstants.EXTRA_FLAG, StartConstants.EXTRA_FLAG_VALUE);
        intent.putParcelableArrayListExtra(StartConstants.EXTRA_COMPONENTS, components);
        intent.putExtra(StartConstants.EXTRA_DESCRIPTION, description);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_layout);

        mPresenter = new ImageActivityPresenter(this);
        mPresenter.setExtras(getIntent());
        mPresenter.inflateFirstFragment();
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        super.replaceFragment(fragment);
    }

    public void saveCombinationToDB(ArrayList<Component> components) {
        mPresenter.saveCombinationToDB(components);
        startActivity(MainActivity.getIntent(this, BottomNavigationOptions.HOME, FragmentTags.HOME_FRAGMENT));
    }
}
