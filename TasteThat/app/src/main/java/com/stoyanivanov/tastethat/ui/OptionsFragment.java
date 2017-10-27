package com.stoyanivanov.tastethat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.stoyanivanov.tastethat.LoginActivity;
import com.stoyanivanov.tastethat.MainActivity;
import com.stoyanivanov.tastethat.R;

public class OptionsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        return view;
    }
}
