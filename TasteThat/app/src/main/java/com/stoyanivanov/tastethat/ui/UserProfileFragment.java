package com.stoyanivanov.tastethat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stoyanivanov.tastethat.activities.LoginActivity;
import com.stoyanivanov.tastethat.activities.MainActivity;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.activities.UserProfileActivity;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {

    private CircleImageView ivProfilePic;
    private CustomTextView tvUsername;
    private FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        ivProfilePic = (CircleImageView) view.findViewById(R.id.iv_profile_picture);
        tvUsername = (CustomTextView) view.findViewById(R.id.tv_username);
        Button btnLiked = (Button) view.findViewById(R.id.btn_liked_combinations);
        Button btnUploaded = (Button) view.findViewById(R.id.btn_uploaded_combinations);
        Button btnAchievements = (Button) view.findViewById(R.id.btn_achievements);
        Button logout = (Button) view.findViewById(R.id.btn_logout);

        loadUserData();

        btnLiked.setOnClickListener(clickListener);
        btnUploaded.setOnClickListener(clickListener);
        btnAchievements.setOnClickListener(clickListener);
        logout.setOnClickListener(clickListener);

        return view;
    }

    private void loadUserData() {
        tvUsername.setText(currUser.getDisplayName());

        String userPhotoUrl = currUser.getPhotoUrl().toString();
        Glide.with(getActivity().getApplicationContext()).load(userPhotoUrl)
                //.centerCrop()
                .into(ivProfilePic);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {

            switch(v.getId()) {
                case R.id.btn_liked_combinations:
                    startActivity(UserProfileActivity.getIntent(getActivity(),
                            BottomNavigationOptions.USER_PROFILE, FragmentTags.LIKED_FRAGMENT));
                    break;

                case R.id.btn_uploaded_combinations:
                    startActivity(UserProfileActivity.getIntent(getActivity(),
                            BottomNavigationOptions.USER_PROFILE, FragmentTags.UPLOADS_FRAGMENT));
                    break;

                case R.id.btn_achievements:
                    startActivity(UserProfileActivity.getIntent(getActivity(),
                            BottomNavigationOptions.USER_PROFILE, FragmentTags.ACHIEVEMENTS_FRAGMENT));
                    break;

                case R.id.btn_logout:
                    signOut();
                    break;
            }
        }
    };

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

}
