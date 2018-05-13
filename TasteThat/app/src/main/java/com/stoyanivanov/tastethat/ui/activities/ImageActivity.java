package com.stoyanivanov.tastethat.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

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
import com.stoyanivanov.tastethat.network.models.Picture;
import com.stoyanivanov.tastethat.ui.activities.main_activity.MainActivity;
import com.stoyanivanov.tastethat.ui.fragments.ChooseImageFragment;

import java.util.ArrayList;

public class ImageActivity extends BaseFragmentContainerActivity {
    private ArrayList<Component> components;
    private ArrayList<Picture> pictures = new ArrayList<>();

    public static Intent getIntent(Context context, String fragmentTag, ArrayList<Component> components) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(StartConstants.EXTRA_FRAGMENT_TAG, fragmentTag);
        intent.putExtra(StartConstants.EXTRA_FLAG, StartConstants.EXTRA_FLAG_VALUE);
        intent.putParcelableArrayListExtra(StartConstants.EXTRA_COMPONENTS, components);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_layout);

        getExtras();
        replaceFragment(ChooseImageFragment.newInstance(components, 0));
    }

    private void getExtras() {
        components = getIntent().getParcelableArrayListExtra(StartConstants.EXTRA_COMPONENTS);
    }


    public void replaceFragment(Fragment fragment) {
            super.replaceFragment(fragment);
    }

    public void saveCombinationToDB(ArrayList<Component> components) {

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        String combinationKey = DatabaseReferences.tableCombinations.push().getKey();

        final Combination newCombination = new Combination(combinationKey, components, currUser.getUid(),
                currUser.getDisplayName(), ServerValue.TIMESTAMP);

        DatabaseProvider.getInstance().saveCombination(newCombination);

        startActivity(MainActivity.getIntent(this, BottomNavigationOptions.HOME, FragmentTags.HOME_FRAGMENT));
    }
}
