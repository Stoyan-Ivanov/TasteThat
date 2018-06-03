package com.stoyanivanov.tastethat.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanivanov.tastethat.R;

public class EditUserInfoFragment extends BaseFragment {

    public static EditUserInfoFragment newInstance() {
        return new EditUserInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflateCurrentView(R.layout.fragment_edit_user_info, inflater, container);
    }

}
