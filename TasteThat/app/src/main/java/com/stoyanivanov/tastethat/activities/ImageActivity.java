package com.stoyanivanov.tastethat.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.DatabaseReferences;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.constants.StartActivityConstants;
import com.stoyanivanov.tastethat.network.network_models.Picture;
import com.stoyanivanov.tastethat.ui.ChooseImageFragment;

import java.util.ArrayList;

public class ImageActivity extends BaseBottomNavigationActivity {
    private ArrayList<String> ingredients;
    private ArrayList<Picture> pictures = new ArrayList<>();
    public static int ingredientNum;

    public static Intent getIntent(Context context, int bottomNavOption, String fragmentTag, ArrayList<String> ingredients) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(StartActivityConstants.EXTRA_NAV_OPTION, bottomNavOption);
        intent.putExtra(StartActivityConstants.EXTRA_FRAGMENT_TAG, fragmentTag);
        intent.putExtra(StartActivityConstants.EXTRA_FLAG, StartActivityConstants.EXTRA_FLAG_VALUE);
        intent.putStringArrayListExtra(StartActivityConstants.EXTRA_INGREDIENTS, ingredients);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        init();
        ingredientNum = 0;
        ingredients = getIntent().getStringArrayListExtra(StartActivityConstants.EXTRA_INGREDIENTS);
        replaceFragment(new ChooseImageFragment());
    }

    public void replaceFragment(Fragment fragment) {

        if(ingredientNum <= 1) {
            Bundle bundle = new Bundle();
            bundle.putString(StartActivityConstants.EXTRA_FRAGMENT_INGREDIENT, ingredients.get(ingredientNum));
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

    // TODO: REFACTOR ALL OF THAT
    private void saveUrlsToDB() {
        String combinationName;
        String firstComponentUrl;
        String secondComponentUrl;
        String httpPrefix = "https:";

        firstComponentUrl = pictures.get(0).getThumbnailUrl();
        secondComponentUrl = pictures.get(1).getThumbnailUrl();
        combinationName = ingredients.get(0) + ingredients.get(1);

        DatabaseReferences.tableCombinations
                .child(combinationName)
                .child(Constants.COMBINATION_FIRST_COMPONENT_URL).setValue(httpPrefix + firstComponentUrl);

        DatabaseReferences.tableCombinations
                .child(combinationName)
                .child(Constants.COMBINATION_SECOND_COMPONENT_URL).setValue(httpPrefix + secondComponentUrl);

        DatabaseReferences.tableUsers
                .child(MainActivity.getCurrentGoogleUser().getUid())
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .child(combinationName)
                .child(Constants.COMBINATION_FIRST_COMPONENT_URL).setValue(httpPrefix + firstComponentUrl);

        DatabaseReferences.tableUsers
                .child(MainActivity.getCurrentGoogleUser().getUid())
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .child(combinationName)
                .child(Constants.COMBINATION_SECOND_COMPONENT_URL).setValue(httpPrefix + secondComponentUrl);
    }

    private void showSuccesToast() {
        Toast toast=Toast.makeText(this, Constants.TOAST_SUCCESSFUL_UPLOAD,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }

}
