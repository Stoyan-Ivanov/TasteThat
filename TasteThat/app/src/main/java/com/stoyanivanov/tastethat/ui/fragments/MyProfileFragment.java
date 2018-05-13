package com.stoyanivanov.tastethat.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stoyanivanov.tastethat.ui.activities.LoginActivity;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.ui.activities.MyAchievementsActivity;
import com.stoyanivanov.tastethat.ui.activities.MyProfileActivity;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileFragment extends BaseFragment {

    @BindView(R.id.iv_profile_picture) CircleImageView ivProfilePic;
    @BindView(R.id.tv_username) CustomTextView tvUsername;
    @BindView(R.id.btn_liked_combinations) Button btnLiked;
    @BindView(R.id.btn_uploaded_combinations) Button btnUploaded;
    @BindView(R.id.btn_achievements) Button btnAchievements;
    @BindView(R.id.btn_logout) Button btnLogout;

    private FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_my_profile, inflater, container);

        loadUserData();

        btnLiked.setOnClickListener(clickListener);
        btnUploaded.setOnClickListener(clickListener);
        btnAchievements.setOnClickListener(clickListener);
        btnLogout.setOnClickListener(clickListener);

        return view;
    }

    private void loadUserData() {
        if(currUser.getDisplayName() != null) {
            tvUsername.setText(currUser.getDisplayName());
        } else {
            tvUsername.setText(currUser.getEmail());
        }

        String userPhotoUrl = "";
        if(currUser.getPhotoUrl() != null) {
            userPhotoUrl = currUser.getPhotoUrl().toString();
        }

        if(userPhotoUrl.isEmpty()) {
            ivProfilePic.setImageResource(R.drawable.default_user_picture);
        } else {
            Glide.with(getActivity()
                    .getApplicationContext())
                    .load(userPhotoUrl)
                    .into(ivProfilePic);
        }
    }

    View.OnClickListener clickListener = v -> {
        switch(v.getId()) {
            case R.id.btn_liked_combinations:
                startActivity(MyProfileActivity.getIntent(getActivity(),
                        BottomNavigationOptions.MY_PROFILE, FragmentTags.LIKED_FRAGMENT));
                break;

            case R.id.btn_uploaded_combinations:
                startActivity(MyProfileActivity.getIntent(getActivity(),
                        BottomNavigationOptions.MY_PROFILE, FragmentTags.UPLOADS_FRAGMENT));
                break;

            case R.id.btn_achievements:
                startActivity(MyAchievementsActivity.getIntent(getActivity(),
                        BottomNavigationOptions.MY_PROFILE));
                break;

            case R.id.btn_logout:
                signOut();
                break;

            default:
                Log.e("SII", "onClick: No such fragment!");
        }
    };

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}
