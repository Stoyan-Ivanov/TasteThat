package com.stoyanivanov.tastethat.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.stoyanivanov.tastethat.MainActivity;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;


public class UserProfileFragment extends Fragment {

    ImageView ivProfilePic;
    CustomTextView tvUsername;
    Button btnLiked;
    Button btnUploaded;

    FirebaseUser currUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_user_profile, container, false);


        tvUsername = (CustomTextView) view.findViewById(R.id.tv_username);
        btnLiked = (Button) view.findViewById(R.id.btn_liked_combinations);
        btnUploaded = (Button) view.findViewById(R.id.btn_uploaded_combinations);
        ivProfilePic = (ImageView) view.findViewById(R.id.iv_profile_picture);

        currUser = ((MainActivity) getActivity()).getCurrentGoogleUser();

        tvUsername.setText(currUser.getDisplayName());

        String userPhotoUrl = currUser.getPhotoUrl().toString();
        Glide.with(getActivity().getApplicationContext()).load(userPhotoUrl)
                .centerCrop()
                .into(ivProfilePic);

        return view;
    }
}
