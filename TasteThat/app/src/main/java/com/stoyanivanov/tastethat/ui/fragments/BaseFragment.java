package com.stoyanivanov.tastethat.ui.fragments;


import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseFragment extends Fragment {
    Unbinder unbinder;

    protected View inflateCurrentView(int layoutResID, LayoutInflater inflater, ViewGroup container) {
        View view  = inflater.inflate(layoutResID, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }
}
