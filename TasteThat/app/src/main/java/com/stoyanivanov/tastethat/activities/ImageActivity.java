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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.DatabaseReferences;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.network.network_models.Picture;
import com.stoyanivanov.tastethat.ui.ChooseImageFragment;

import java.util.ArrayList;
import java.util.Map;

public class ImageActivity extends BaseBottomNavigationActivity {
    private ArrayList<String> components;
    private ArrayList<Picture> pictures = new ArrayList<>();
    public static int currComponent;

    public static Intent getIntent(Context context, int bottomNavOption, String fragmentTag, ArrayList<String> components) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(StartConstants.EXTRA_NAV_OPTION, bottomNavOption);
        intent.putExtra(StartConstants.EXTRA_FRAGMENT_TAG, fragmentTag);
        intent.putExtra(StartConstants.EXTRA_FLAG, StartConstants.EXTRA_FLAG_VALUE);
        intent.putStringArrayListExtra(StartConstants.EXTRA_COMPONENTS, components);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        init();
        currComponent = 0;
        components = getIntent().getStringArrayListExtra(StartConstants.EXTRA_COMPONENTS);
        replaceFragment(new ChooseImageFragment());
    }

    public void replaceFragment(Fragment fragment) {
        if(currComponent < components.size()) {
            Bundle arguments = new Bundle();
            arguments.putString(StartConstants.EXTRA_FRAGMENT_COMPONENT, components.get(currComponent));
            fragment.setArguments(arguments);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.image_selection_container, fragment);

            transaction.commit();
        } else {
            saveCombinationToDB();
            showSuccessToast();
            startActivity(MainActivity.getIntent(this, BottomNavigationOptions.HOME, FragmentTags.HOME_FRAGMENT));
        }
    }

    public void savePicture(Picture picture) {
        pictures.add(picture);
    }

    private void saveCombinationToDB() {
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<String> urls = new ArrayList<>();
        String httpPrefix = "https:";

        for(Picture picture : pictures) {
            urls.add(httpPrefix + picture.getThumbnailUrl());
        }

        String combinationKey = DatabaseReferences.tableCombinations.push().getKey();

        Combination newCombination = new Combination(combinationKey, components, currUser.getUid(),
                                                        currUser.getDisplayName(),urls, ServerValue.TIMESTAMP);

        DatabaseReferences.tableCombinations.child(combinationKey)
                .setValue(newCombination);

        // create negative timestamp for sorting purpose
        final DatabaseReference timestampReference = DatabaseReferences.tableCombinations.child(combinationKey).child("timestamp");
        timestampReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    if (!(Long.parseLong(dataSnapshot.getValue().toString()) < 0)) {
                        timestampReference.setValue(0 - Long.parseLong(dataSnapshot.getValue().toString()));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("SII", databaseError.getMessage());
            }
        });

        DatabaseReferences.tableUsers.child(currUser.getUid())
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .child(combinationKey)
                .setValue(newCombination);
    }

    private void showSuccessToast() {
        Toast toast=Toast.makeText(this, Constants.TOAST_SUCCESSFUL_UPLOAD,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }

}
