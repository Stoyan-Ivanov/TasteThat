package com.stoyanivanov.tastethat.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.stoyanivanov.tastethat.activities.ImageActivity;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.activities.MainActivity;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.models.Combination;

import java.util.ArrayList;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.*;


public class AddCombinationFragment extends Fragment {
    private EditText firstIngredient;
    private EditText secondIngredient;
    private FirebaseUser currUser;
    private Button addCombination;
    private String firstIng;
    private String secondIng;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_combination, container, false);

        firstIngredient = (EditText) view.findViewById(R.id.et_first_ingredient);
        secondIngredient = (EditText) view.findViewById(R.id.et_second_ingredient);
        addCombination = (Button) view.findViewById(R.id.btn_add_combination);
        currUser = ((MainActivity) getActivity()).getCurrentGoogleUser();

        secondIngredient.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER) && event.getAction() == KeyEvent.ACTION_DOWN) {
                    setDataToDB();
                    return true;
                }

                return false;
            }
        });

        addCombination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataToDB();
            }
        });

        return view;
    }

    private void setDataToDB () {

        firstIng = firstIngredient.getText().toString();
        secondIng = secondIngredient.getText().toString();

        if(firstIng.equals("") || secondIng.equals("")) {
            showFailToast();
        } else {
            String combinationName = firstIng + secondIng;

            Combination newCombination = new Combination(firstIng, secondIng, currUser.getUid(),currUser.getDisplayName() );

            tableCombinations.child(firstIng + secondIng).setValue(newCombination);

            tableUsers.child(currUser.getUid())
                    .child(Constants.USER_UPLOADED_COMBINATIONS)
                    .child(combinationName).setValue(newCombination);

            startImageActivity();
            clearForm();
        }
    }

    private void startImageActivity() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add(firstIng);
        ingredients.add(secondIng);
        startActivity(ImageActivity.getIntent(getActivity(), BottomNavigationOptions.ADD, FragmentTags.CHOOSE_IMAGE_FRAGMENT,ingredients));
    }

    private void clearForm() {
        firstIngredient.setText("");
        secondIngredient.setText("");
    }

    private void showFailToast() {
        Toast toast=Toast.makeText(getActivity(), Constants.TOAST_FAILED_UPLOAD,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }
}
