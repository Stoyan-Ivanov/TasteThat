package com.stoyanivanov.tastethat.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.DatabaseReferences;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.constants.StartActivityConstants;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.network.network_models.Picture;
import com.stoyanivanov.tastethat.ui.ChooseImageFragment;

import java.util.ArrayList;

public class ImageActivity extends BaseBottomNavigationActivity {
    private ArrayList<String> components;
    private ArrayList<Picture> pictures = new ArrayList<>();
    public static int currComponent;

    public static Intent getIntent(Context context, int bottomNavOption, String fragmentTag, ArrayList<String> components) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(StartActivityConstants.EXTRA_NAV_OPTION, bottomNavOption);
        intent.putExtra(StartActivityConstants.EXTRA_FRAGMENT_TAG, fragmentTag);
        intent.putExtra(StartActivityConstants.EXTRA_FLAG, StartActivityConstants.EXTRA_FLAG_VALUE);
        intent.putStringArrayListExtra(StartActivityConstants.EXTRA_COMPONENTS, components);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        init();
        currComponent = 0;
        components = getIntent().getStringArrayListExtra(StartActivityConstants.EXTRA_COMPONENTS);
        replaceFragment(new ChooseImageFragment());
    }

    public void replaceFragment(Fragment fragment) {
        if(currComponent < components.size()) {
            Bundle bundle = new Bundle();
            bundle.putString(StartActivityConstants.EXTRA_FRAGMENT_COMPONENT, components.get(currComponent));
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.image_selection_container, fragment);
            transaction.addToBackStack(null);

            transaction.commit();
        } else {
            saveUrlsToDB();
            showSuccesToast();
            startActivity(MainActivity.getIntent(this, BottomNavigationOptions.HOME, FragmentTags.HOME_FRAGMENT));
        }
    }

    public void savePicture(Picture picture) {
        pictures.add(picture);
    }

    private void saveUrlsToDB() {
        FirebaseUser currUser = MainActivity.getCurrentGoogleUser();
        String combinationName = "";
        StringBuilder combinationNameBuilder = new StringBuilder();
        ArrayList<String> urls = new ArrayList<>();
        String httpPrefix = "https:";

        for(Picture picture : pictures) {
            urls.add(httpPrefix + picture.getThumbnailUrl());
        }

        for(String component : components) {
            combinationNameBuilder.append(component);
        }
        combinationName = combinationNameBuilder.toString();

        Combination newCombination = new Combination(combinationName, components, currUser.getUid(), currUser.getDisplayName(),urls);

        DatabaseReferences.tableCombinations
                .child(combinationName)
                .setValue(newCombination);

        DatabaseReferences.tableUsers.child(MainActivity.getCurrentGoogleUser().getUid())
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .child(combinationName)
                .setValue(newCombination);
    }

    private void showSuccesToast() {
        Toast toast=Toast.makeText(this, Constants.TOAST_SUCCESSFUL_UPLOAD,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }

}
