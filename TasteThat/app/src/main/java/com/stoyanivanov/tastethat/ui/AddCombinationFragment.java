package com.stoyanivanov.tastethat.ui;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
        DatabaseReference myRef = database.getReference();

        String firstIng = firstIngredient.getText().toString();
        String secondIng = secondIngredient.getText().toString();

        Combination newCombination = new Combination(firstIng, secondIng, currUser.getUid());

        myRef.child("combinations").child(firstIng + secondIng).setValue(newCombination);
        myRef.child("users").child(currUser.getUid()).child("userCombinations").push().setValue(firstIng + secondIng);
        
        clearForm();
        showSuccesToast();
    }

    private void clearForm() {
        firstIngredient.setText("");
        secondIngredient.setText("");
    }
    
    private void showSuccesToast() {
        Toast toast=Toast.makeText(getActivity(),"Combination uploaded!",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }

}
