package com.stoyanivanov.tastethat.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stoyanivanov.tastethat.db.DatabaseProvider;
import com.stoyanivanov.tastethat.db.models.Achievement;
import com.stoyanivanov.tastethat.ui.activities.LoginActivity;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.ui.activities.MyProfileActivity;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.ui.activities.main_activity.MainActivity;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.decoration.SpacesItemDecoration;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.user_achievements_recyclerview.UserAchievementsRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileFragment extends BaseFragment {

    @BindView(R.id.iv_profile_picture) CircleImageView mIvProfilePic;
    @BindView(R.id.et_username) TextView mTvUsername;
    @BindView(R.id.btn_rated_combinations) Button mBtnLiked;
    @BindView(R.id.btn_uploaded_combinations) Button mBtnUploaded;
    @BindView(R.id.btn_logout) Button mBtnLogout;
    @BindView(R.id.rv_my_achievements) RecyclerView mRecyclerView;

    private FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
    private UserAchievementsRecyclerViewAdapter mAdapter;

    @OnClick(R.id.iv_edit_profile)
    void onEditProfileClicked() {
        ((MainActivity) getActivity()).replaceFragment(EditUserInfoFragment.newInstance());
    }

//    @OnClick(R.id.iv_about)
//    void onAboutClicked() {
//        ((MainActivity) getActivity()).replaceFragment(AboutFragment.newInstance());
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_my_profile, inflater, container);

        loadUserData();
        DatabaseProvider.getInstance().getAchievements(this);

        mBtnLiked.setOnClickListener(clickListener);
        mBtnUploaded.setOnClickListener(clickListener);
        mBtnLogout.setOnClickListener(clickListener);

        return view;
    }

    private void loadUserData() {
        if(currUser.getDisplayName() != null) {
            mTvUsername.setText(currUser.getDisplayName());
        } else {
            mTvUsername.setText(currUser.getEmail());
        }

        String userPhotoUrl = "";
        if(currUser.getPhotoUrl() != null) {
            userPhotoUrl = currUser.getPhotoUrl().toString();
        }

        if(userPhotoUrl.isEmpty()) {
            mIvProfilePic.setImageResource(R.drawable.default_user_picture);
        } else {
            Glide.with(getActivity()
                    .getBaseContext())
                    .load(userPhotoUrl)
                    .into(mIvProfilePic);
        }
    }

    private void instantiateRecyclerView(ArrayList<Achievement> achievements) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(8, SpacesItemDecoration.HORIZONTAL));
        mAdapter = new UserAchievementsRecyclerViewAdapter(achievements);
        mRecyclerView.setAdapter(mAdapter);
    }

    View.OnClickListener clickListener = v -> {
        switch(v.getId()) {
            case R.id.btn_rated_combinations:
                startActivity(MyProfileActivity.getIntent(getActivity(),
                        BottomNavigationOptions.MY_PROFILE, FragmentTags.RATED_COMBINATIONS_FRAGMENT));
                break;

            case R.id.btn_uploaded_combinations:
                startActivity(MyProfileActivity.getIntent(getActivity(),
                        BottomNavigationOptions.MY_PROFILE, FragmentTags.UPLOADS_FRAGMENT));
                break;

            case R.id.btn_logout:
                signOut();
                break;

            default:
                Log.e("SII", "onClick: No such fragment!");
        }
    };

    private void signOut() {
        new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setTitle(getString(R.string.sign_out_dialog_title))
                .setMessage(getString(R.string.sign_out_dialog_message))
                .setPositiveButton(getString(R.string.sign_out_dialog_accept_button), (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                })
                .setNegativeButton(getString(R.string.sign_out_dialog_deny_button), (dialog, which) -> {})
                .show();
    }

    public void onAchievementsGathered(ArrayList<Achievement> mAchievements) {
        if(mAdapter == null) {
            instantiateRecyclerView(mAchievements);
        } else {
            mAdapter.setNewData(mAchievements);
        }
    }
}
