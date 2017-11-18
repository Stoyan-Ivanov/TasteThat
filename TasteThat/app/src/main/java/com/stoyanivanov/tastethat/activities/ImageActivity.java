package com.stoyanivanov.tastethat.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.DatabaseReferences;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.constants.StartActivityConstants;
import com.stoyanivanov.tastethat.network.TasteThatAPI;
import com.stoyanivanov.tastethat.network.TasteThatApplication;
import com.stoyanivanov.tastethat.network.network_models.NextImagesResponse;
import com.stoyanivanov.tastethat.network.network_models.Picture;
import com.stoyanivanov.tastethat.ui.ChooseImageFragment;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class ImageActivity extends BaseBottomNavigationActivity {
    private ArrayList<String> ingredients;
    private ArrayList<Picture> pictures = new ArrayList<>();
    public static int ingredientNum;

    public static Intent getIntent(Context context, int bottomNavOption, String fragmentTag, ArrayList<String> ingredients) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(StartActivityConstants.extraNavOption, bottomNavOption);
        intent.putExtra(StartActivityConstants.extraFragmentTag, fragmentTag);
        intent.putExtra(StartActivityConstants.extraFlag, "started_properly");
        intent.putStringArrayListExtra("ingredients", ingredients);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        init();
        ingredientNum = 0;
        ingredients = getIntent().getStringArrayListExtra("ingredients");
        replaceFragment(new ChooseImageFragment());
    }

    public void replaceFragment(Fragment fragment) {

        if(ingredientNum <= 1) {
            Bundle bundle = new Bundle();
            bundle.putString("extraIngredient", ingredients.get(ingredientNum));
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

        firstComponentUrl = pictures.get(0).getThumbnailUrl();
        secondComponentUrl = pictures.get(1).getThumbnailUrl();
        combinationName = ingredients.get(0) + ingredients.get(1);

        DatabaseReferences.tableCombinations.child(combinationName).child("firstComponentUrl").setValue(firstComponentUrl);
        DatabaseReferences.tableCombinations.child(combinationName).child("secondComponentUrl").setValue(secondComponentUrl);

        DatabaseReferences.tableUsers.child(MainActivity.getCurrentGoogleUser().getUid())
                            .child("uploadedCombinations").child(combinationName).child("firstComponentUrl").setValue(firstComponentUrl);
        DatabaseReferences.tableUsers.child(MainActivity.getCurrentGoogleUser().getUid())
                .child("uploadedCombinations").child(combinationName).child("secondComponentUrl").setValue(secondComponentUrl);
    }

    private void showSuccesToast() {
        Toast toast=Toast.makeText(this, Constants.TOAST_SUCCESSFUL_UPLOAD,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }

}
