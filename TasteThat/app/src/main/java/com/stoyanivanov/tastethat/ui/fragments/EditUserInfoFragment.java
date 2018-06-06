package com.stoyanivanov.tastethat.ui.fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.ui.activities.main_activity.MainActivity;
import com.stoyanivanov.tastethat.ui.base_ui.BaseFragment;
import com.stoyanivanov.tastethat.view_utils.custom_views.ProgressDialog;

import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class EditUserInfoFragment extends BaseFragment {

    @BindView(R.id.et_username) EditText mEtUsername;
    @BindView(R.id.tv_email) TextView mTvEmail;
    @BindView(R.id.iv_profile_picture) ImageView mProfilePicture;
    private FirebaseUser mCurrentUser;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private StorageReference mStorageReference = mStorage.getReference();
    private ProgressDialog mProgressDialog;

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
                .setPositiveButton(getString(R.string.save_changes_dialog_accept_button), (dialog, which) -> prepareDataForSaving())
                .setNegativeButton(getString(R.string.save_changes_dialog_deny_button), (dialog, which) -> {})
                .show();
    }

    @OnClick(R.id.btn_upload_picture)
    void onUploadPictureClicked() {
        chooseImage();
    }

    private void prepareDataForSaving() {
        String displayName = mEtUsername.getText().toString();

        if (displayName.equals("")) {
            popCurrentFragment();
        } else {
            uploadImage(displayName);
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.image_selection_dialog_title)), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                mProfilePicture.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(String displayName) {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.show();

        if(filePath != null) {
            StorageReference ref = mStorageReference.child(Constants.STORAGE_PROFILE_PICTURES + UUID.randomUUID().toString());
            UploadTask uploadTask = ref.putFile(filePath);



            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    saveNewUserData(displayName, downloadUri);
                } else {
                    TasteThatApplication.showToast(getString(R.string.error_while_uploading_profile_picture));
                }
            });
        } else {
            saveNewUserData(displayName, null);
        }
    }

    private void saveNewUserData(String displayName, Uri downloadUri) {
        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
        builder.setDisplayName(displayName);

        if(downloadUri != null) {
            builder.setPhotoUri(downloadUri);
        }
        mCurrentUser.updateProfile(builder.build()).addOnCompleteListener(task -> {
            mProgressDialog.dismiss();
            TasteThatApplication.showToast(getString(R.string.toast_picture_uploaded));
            startActivity(MainActivity.getIntent(getContext(), BottomNavigationOptions.OPTIONS, FragmentTags.MY_PROFILE_FRAGMENT));
        });
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
                .placeholder(R.drawable.default_user_picture)
                .into(mProfilePicture);
    }
}
