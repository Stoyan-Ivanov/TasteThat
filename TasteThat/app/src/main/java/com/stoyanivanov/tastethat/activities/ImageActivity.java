package com.stoyanivanov.tastethat.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.StartActivityConstants;
import com.stoyanivanov.tastethat.network.TasteThatAPI;
import com.stoyanivanov.tastethat.network.TasteThatApplication;
import com.stoyanivanov.tastethat.network.network_models.NextImagesResponse;
import com.stoyanivanov.tastethat.ui.ChooseImageFragment;

import java.util.ArrayList;

import retrofit2.Call;

public class ImageActivity extends BaseBottomNavigationActivity {
    private ArrayList<String> ingredients;

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
        ingredients = getIntent().getStringArrayListExtra("ingredients");
        replaceFragment(new ChooseImageFragment());
    }

    public void replaceFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("extraIngredient", ingredients.get(0));
        fragment.setArguments(bundle);

        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.image_selection_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
