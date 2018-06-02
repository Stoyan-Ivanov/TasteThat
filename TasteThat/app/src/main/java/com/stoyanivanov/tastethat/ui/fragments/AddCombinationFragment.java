package com.stoyanivanov.tastethat.ui.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.db.models.Component;
import com.stoyanivanov.tastethat.ui.activities.image_activity.ImageActivity;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class AddCombinationFragment extends BaseFragment {

    @BindView(R.id.ll_add_fields_container) LinearLayout mAddFieldsContainer;
    @BindView(R.id.btn_add_combination) Button mAddCombination;
    @BindView(R.id.btn_add_discard) ImageView mIvDiscardChanges;
    @BindView(R.id.view_first_ingredient) View mFirstIngredientField;
    @BindView(R.id.view_second_ingredient) View mSecondIngredientField;
    @BindView(R.id.iv_back_arrow) ImageView mBackArrow;
    @BindView(R.id.et_description) EditText mEtDescription;

    private ArrayList<View> mAllFields = new ArrayList<>();
    private ViewGroup mContainer;

    public static AddCombinationFragment newInstance() {
        return new AddCombinationFragment();
    }

    @OnClick(R.id.btn_add_combination)
        void startNewImageActivity() {
            ArrayList<Component> components = getAllComponents();
            String description = getDescription();

            if(components.size() >= Constants.MIN_REQUIRED_COMPONENTS) {
                startActivity(ImageActivity.getIntent(getActivity(),
                        FragmentTags.CHOOSE_IMAGE_FRAGMENT, components, description));
            } else {
                TasteThatApplication.showToast(getString(R.string.toast_invalid_input));
            }
        }

    @OnClick(R.id.btn_add_discard)
        void discardChanges(View view) {
            TasteThatApplication.hideVirtualKeyboard(view);
            showWarningDialog();
        }

    private void showWarningDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.discard_dialog_title))
                .setMessage(getString(R.string.discard_dialog_message))
                .setPositiveButton(getString(R.string.discard_dialog_accept_button), (dialog, which) -> discardUserChanges())
                .setNegativeButton(getString(R.string.discard_dialog_deny_button), (dialog, which) -> {})
                .show();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_add_combination, inflater, container);

        this.mContainer = container;

        mAllFields.clear();
        configureFirstTwoFields();
        configureBackButton();

        return view;
    }

    private void configureBackButton() {
        mBackArrow.setOnClickListener(view -> getFragmentManager().popBackStack());
    }
    
    private void discardUserChanges() {
        for(View field : mAllFields) {
            EditText componentField =  field.findViewById(R.id.et_component);
            componentField.setText("");
        }

        mAllFields.get(0).requestFocus();
    }

    private void configureFirstTwoFields() {
        Button newFieldBtnFirst =  mFirstIngredientField.findViewById(R.id.btn_add_new_et);
        CustomTextView fieldCounterFirst = mFirstIngredientField.findViewById(R.id.ctv_et_counter);
        mAllFields.add(mFirstIngredientField);

        newFieldBtnFirst.setVisibility(View.INVISIBLE);
        fieldCounterFirst.setText(generateStringForFieldCounter());

        Button newFieldBtnSecond = mSecondIngredientField.findViewById(R.id.btn_add_new_et);
        CustomTextView fieldCounterSecond = mSecondIngredientField.findViewById(R.id.ctv_et_counter);
        mAllFields.add(mSecondIngredientField);

        newFieldBtnSecond.setOnClickListener(newFieldBtnOnclick);
        fieldCounterSecond.setText(generateStringForFieldCounter());
    }

    View.OnClickListener newFieldBtnOnclick = v -> {
        v.setVisibility(View.INVISIBLE);
        inflateNewField();
    };

    private void inflateNewField() {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View newField = inflater.inflate(R.layout.add_field_inflatable, mContainer,false);
        if (newField != null) {
            mAllFields.add(newField);
            configureNewField(newField);
            mAddFieldsContainer.addView(newField);
        }
    }

    private void configureNewField(View newField) {
        Button newFieldBtn = newField.findViewById(R.id.btn_add_new_et);
        CustomTextView fieldCounter = newField.findViewById(R.id.ctv_et_counter);

        newField.findViewById(R.id.et_component).requestFocus();

        if (mAllFields.size() >= Constants.MAX_ADD_FIELDS) {
            newFieldBtn.setVisibility(View.INVISIBLE);
        } else {
            newFieldBtn.setOnClickListener(newFieldBtnOnclick);
        }

        fieldCounter.setText(generateStringForFieldCounter());

        newField.requestFocus();
    }

    @NonNull
    private String generateStringForFieldCounter() {
        return Integer.toString(mAllFields.size()) + "/" + Integer.toString(Constants.MAX_ADD_FIELDS);
    }

    private ArrayList<Component> getAllComponents() {
        ArrayList<Component> components = new ArrayList<>();

        for (View field: mAllFields) {
            EditText editText = field.findViewById(R.id.et_component);
            String componentName =  editText.getText().toString();

            if(!componentName.equals("")) {
                components.add(new Component(componentName));
            }
        }

        return components;
    }

    public String getDescription() {
        String description = mEtDescription.getText().toString();

        if(description.equals("")) {
            description = "Missing combination description!";
        }
        return description;
    }
}
