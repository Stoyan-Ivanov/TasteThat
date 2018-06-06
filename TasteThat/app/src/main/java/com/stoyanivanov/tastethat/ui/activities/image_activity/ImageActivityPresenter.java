package com.stoyanivanov.tastethat.ui.activities.image_activity;

import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ServerValue;
import com.stoyanivanov.tastethat.constants.DatabaseReferences;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.db.DatabaseProvider;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.db.models.Component;
import com.stoyanivanov.tastethat.ui.base_ui.BasePresenter;
import com.stoyanivanov.tastethat.ui.fragments.ChooseImageFragment;

import java.util.ArrayList;

public class ImageActivityPresenter extends BasePresenter {
    private ArrayList<Component> components;
    private String description;
    private ImageActivityContract mView;

    public ImageActivityPresenter(ImageActivityContract view) {
        this.mView = view;
    }

    public void setExtras(Intent intent) {
        components = intent.getParcelableArrayListExtra(StartConstants.EXTRA_COMPONENTS);
        description = intent.getStringExtra(StartConstants.EXTRA_DESCRIPTION);
    }

    public void inflateFirstFragment() {
        final int FIRST_COMPONENT = 0;
        mView.replaceFragment(ChooseImageFragment.newInstance(components, FIRST_COMPONENT));
    }

    public void saveCombinationToDB(ArrayList<Component> components) {

        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        String combinationKey = DatabaseReferences.nodeCombinations.push().getKey();

        final Combination newCombination = new Combination(combinationKey, components, currUser.getUid(),
                currUser.getDisplayName(), ServerValue.TIMESTAMP, description);

        DatabaseProvider.getInstance().saveCombination(newCombination);
    }
}
