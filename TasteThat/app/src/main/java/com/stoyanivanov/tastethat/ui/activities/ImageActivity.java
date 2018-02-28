package com.stoyanivanov.tastethat.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

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
import com.stoyanivanov.tastethat.ui.fragments.ChooseImageFragment;

import java.util.ArrayList;

public class ImageActivity extends BaseBottomNavigationActivity {
    private ArrayList<String> componentsNames;
    private ArrayList<String> componentsUrls = new ArrayList<>();
    private ArrayList<Picture> pictures = new ArrayList<>();
    public static int currComponent;

    public static Intent getIntent(Context context, String fragmentTag, ArrayList<String> components) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(StartConstants.EXTRA_FRAGMENT_TAG, fragmentTag);
        intent.putExtra(StartConstants.EXTRA_FLAG, StartConstants.EXTRA_FLAG_VALUE);
        intent.putStringArrayListExtra(StartConstants.EXTRA_COMPONENTS, components);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_layout);

        currComponent = 0;
        componentsNames = getIntent().getStringArrayListExtra(StartConstants.EXTRA_COMPONENTS);
        replaceFragment();
    }

    public void replaceFragment() {
        if(currComponent < componentsNames.size()) {
            ChooseImageFragment nextFragment = ChooseImageFragment.newInstance(componentsNames.get(currComponent));

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.fragment_container, nextFragment);
            transaction.commit();

        } else {
            saveCombinationToDB();
            startActivity(MainActivity.getIntent(this, BottomNavigationOptions.HOME, FragmentTags.HOME_FRAGMENT));
        }
    }

    public void savePicture(Picture picture) {
        pictures.add(picture);
    }

    private void saveCombinationToDB() {
        getComponentsUrls();

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        String combinationKey = DatabaseReferences.tableCombinations.push().getKey();

        ArrayList<Component> components = new ArrayList<>();

        for(int i = 0; i < componentsNames.size(); i++) {
            components.add(new Component(componentsNames.get(i), componentsUrls.get(i)));
        }

        final Combination newCombination = new Combination(combinationKey, components, currUser.getUid(),
                currUser.getDisplayName(), ServerValue.TIMESTAMP);

        DatabaseProvider.getInstance().saveCombination(newCombination);
    }

    private void getComponentsUrls() {
        final String httpPrefix = "https:";

        for (Picture picture : pictures) {
            componentsUrls.add(httpPrefix + picture.getThumbnailUrl());
        }
    }
}
