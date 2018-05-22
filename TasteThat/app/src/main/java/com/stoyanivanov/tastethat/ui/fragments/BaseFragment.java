package com.stoyanivanov.tastethat.ui.fragments;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    Unbinder unbinder;

    protected View inflateCurrentView(int layoutResID, LayoutInflater inflater, ViewGroup container) {
        View view  = inflater.inflate(layoutResID, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    protected void popCurrentFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.popBackStack();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }
}
