package com.stoyanivanov.tastethat.ui;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stoyanivanov.tastethat.Constants;
import com.stoyanivanov.tastethat.MainActivity;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.models.Combination;


public class AddCombinationFragment extends Fragment {
    private EditText firstIngredient;
    private EditText secondIngredient;
    private FirebaseUser currUser;
    private Button addCombination;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_combination, container, false);

        firstIngredient = (EditText) view.findViewById(R.id.et_first_ingredient);
        secondIngredient = (EditText) view.findViewById(R.id.et_second_ingredient);

        currUser = ((MainActivity) getActivity()).getCurrentGoogleUser();

        secondIngredient.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                    setDataToDB();
                    return true;
                }

                return false;
            }
        });

        addCombination = (Button) view.findViewById(R.id.btn_add_combination);
        addCombination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataToDB();
            }
        });

        return view;
    }

    private void setDataToDB () {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseCombination = database.getReference().child(Constants.COMBINATIONS_DATABASE);
        DatabaseReference mDatabaseUsers = database.getReference().child(Constants.USER_DATABASE);

        String firstIng = firstIngredient.getText().toString();
        String secondIng = secondIngredient.getText().toString();

        if(firstIng.equals("") || secondIng.equals("")) {
            showFailToast();
        } else {
            String combinationName = firstIng + secondIng;

            Combination newCombination = new Combination(firstIng, secondIng, currUser.getUid());

            mDatabaseCombination.child(firstIng + secondIng).setValue(newCombination);
            mDatabaseUsers.child(currUser.getUid()).child(Constants.USER_UPLOADED_COMBINATIONS).push().setValue(newCombination);

            clearForm();
            showSuccesToast();
        }
    }

    private void clearForm() {
        firstIngredient.setText("");
        secondIngredient.setText("");
    }
    
    private void showSuccesToast() {
        Toast toast=Toast.makeText(getActivity(), Constants.TOAST_SUCCESSFUL_UPLOAD,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }

    private void showFailToast() {
        Toast toast=Toast.makeText(getActivity(), Constants.TOAST_FAILED_UPLOAD,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }
}
