package com.stoyanivanov.tastethat.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.activities.MainActivity;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;

import java.util.ArrayList;

public class v2AddCombination extends Fragment {
    private FirebaseUser currUser;
    private PercentRelativeLayout addFieldsContainer;
    private Button addCombination;
    private ArrayList<View> allFields = new ArrayList<>();
    private View firstIngredientField;
    private View secondIngredientField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_add, container, false);

        currUser = ((MainActivity) getActivity()).getCurrentGoogleUser();
        addFieldsContainer = (PercentRelativeLayout) view.findViewById(R.id.prl_add_fields_container);
        addCombination = (Button) view.findViewById(R.id.btn_add_combination);
        firstIngredientField = view.findViewById(R.id.view_first_ingredient);
        secondIngredientField = view.findViewById(R.id.view_second_ingredient);
        
        allFields.clear();
        configureFirstTwoFields();

        return view;
    }

    private void configureFirstTwoFields() {
        Button newFieldBtnFirst = (Button) firstIngredientField.findViewById(R.id.btn_add_new_et);
        CustomTextView fieldCounterFirst = (CustomTextView) firstIngredientField.findViewById(R.id.ctv_et_counter);
        allFields.add(firstIngredientField);

        newFieldBtnFirst.setVisibility(View.INVISIBLE);
        fieldCounterFirst.setText(generateStringForFieldCounter());


        Button newFieldBtnSecond = (Button) secondIngredientField.findViewById(R.id.btn_add_new_et);
        CustomTextView fieldCounterSecond = (CustomTextView) secondIngredientField.findViewById(R.id.ctv_et_counter);
        allFields.add(secondIngredientField);

        newFieldBtnSecond.setOnClickListener(newFieldBtnOnclick);
        fieldCounterSecond.setText(generateStringForFieldCounter());
    }

    View.OnClickListener newFieldBtnOnclick =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setVisibility(View.INVISIBLE);
            inflateNewField();
        }
    };

    private void inflateNewField() {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View newField = inflater.inflate(R.layout.add_field_inflatable,null);
        allFields.add(newField);

        Button newFieldBtn = (Button) newField.findViewById(R.id.btn_add_new_et);
        CustomTextView fieldCounter = (CustomTextView) newField.findViewById(R.id.ctv_et_counter);

        if(allFields.size() >= Constants.MAX_ADD_FIELDS) {
            newFieldBtn.setVisibility(View.INVISIBLE);
        } else {
            newFieldBtn.setOnClickListener(newFieldBtnOnclick);
        }

        fieldCounter.setText(generateStringForFieldCounter());
    }

    private String generateStringForFieldCounter() {
      return Integer.toString(allFields.size()) + "/" + Integer.toString(Constants.MAX_ADD_FIELDS);
    }

}
