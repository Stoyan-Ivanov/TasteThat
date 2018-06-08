package com.stoyanivanov.tastethat.view_utils.custom_views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.ui.fragments.CombinationDetailsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditCombinationDescriptionFragment extends DialogFragment {
    @BindView(R.id.et_combination_description)
    EditText mEtDescription;
    private FragmentManager mFragmentManager;
    private CombinationDetailsFragment mFragment;
    public static final int REQUEST_CODE_DESCRIPTION = 24;
    private String mDescription;

    public static EditCombinationDescriptionFragment newInstance(String description) {
        Bundle args = new Bundle();
        args.putString(StartConstants.EXTRA_DESCRIPTION, description);

        EditCombinationDescriptionFragment fragment = new EditCombinationDescriptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void onDoneClicked() {
        String description = mEtDescription.getText().toString();
        if (description.equals("")) {
            description = "Missing combination description";
        }

        Intent intent = new Intent();
        intent.putExtra(StartConstants.EXTRA_DESCRIPTION, description);

        if (mFragment != null) {
            mFragment.onActivityResult(REQUEST_CODE_DESCRIPTION, Activity.RESULT_OK, intent);
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View view = inflater.inflate(R.layout.dialog_edit_combination, null);
        ButterKnife.bind(this, view);

        getExtraArguments();
        mEtDescription.setText(mDescription);

        return new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle("Edit combination description")
                .setView(view)
                .setPositiveButton(getString(R.string.btn_done), (dialog, which) -> onDoneClicked())
                .setNegativeButton(getString(R.string.btn_cancel), (dialog, which) -> {})
                .show();

    }

    public void setTargetFragment(CombinationDetailsFragment fragment, FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
        mFragment = fragment;
    }

    public void show() {
        super.show(mFragmentManager, null);
    }

    private void getExtraArguments() {
        mDescription = getArguments().getString(StartConstants.EXTRA_DESCRIPTION);
    }

}
