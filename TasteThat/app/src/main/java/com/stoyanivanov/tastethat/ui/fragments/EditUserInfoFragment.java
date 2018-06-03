package com.stoyanivanov.tastethat.ui.fragments;


import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.ui.activities.main_activity.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class EditUserInfoFragment extends BaseFragment {

    @BindView(R.id.et_username) EditText mEtUsername;
    @BindView(R.id.tv_email) TextView mTvEmail;
    @BindView(R.id.iv_profile_picture) ImageView mProfilePicture;
    private FirebaseUser mCurrentUser;

    public static EditUserInfoFragment newInstance() {
        return new EditUserInfoFragment();
    }

    @OnClick(R.id.iv_back_arrow)
    void onBackArrowClicked() {
        popCurrentFragment();
    }

    @OnClick(R.id.iv_confirm_changes)
    void onChangesMade() {
        new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setTitle(getString(R.string.save_changes_dialog_title))
                .setMessage(getString(R.string.save_changes_dialog_message))
                .setPositiveButton(getString(R.string.save_changes_dialog_accept_button), (dialog, which) -> saveChanges())
                .setNegativeButton(getString(R.string.save_changes_dialog_deny_button), (dialog, which) -> {})
                .show();
    }

    @OnClick(R.id.btn_upload_picture)
    void onUploadPictureClicked() {
        TasteThatApplication.showToast(getString(R.string.toast_not_available));
    }

    private void saveChanges() {
        String displayName = mEtUsername.getText().toString();

        if(!displayName.equals("")) {
            UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
            builder.setDisplayName(displayName);
            mCurrentUser.updateProfile(builder.build()).addOnCompleteListener(task -> {
                TasteThatApplication.showToast(getString(R.string.toast_picture_uploaded));
                startActivity(MainActivity.getIntent(getContext(), BottomNavigationOptions.OPTIONS, FragmentTags.MY_PROFILE_FRAGMENT));
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        return inflateCurrentView(R.layout.fragment_edit_user_info, inflater, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEtUsername.setText(mCurrentUser.getDisplayName());
        mTvEmail.setText(mCurrentUser.getEmail());

        Glide.with(getActivity()
                .getBaseContext())
                .load(mCurrentUser.getPhotoUrl())
                .into(mProfilePicture);
    }
}
