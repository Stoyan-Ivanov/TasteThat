package com.stoyanivanov.tastethat.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.TasteThatApplication;

import butterknife.BindView;

public class OptionsFragment extends BaseFragment {
    @BindView(R.id.sw_caching) Switch cachingSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_options, inflater, container);

        configureCaching();
        return view;
    }

    private void configureCaching() {

        final SharedPreferences sharedPrefs = TasteThatApplication
                .getStaticContext()
                .getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = sharedPrefs.edit();
        cachingSwitch.setChecked(sharedPrefs.getBoolean(Constants.CACHING_KEY, false));


        cachingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(Constants.CACHING_KEY, isChecked);
                editor.apply();

                if (isChecked) {
                    TasteThatApplication.showToast(TasteThatApplication
                            .getStringFromId(R.string.toast_caching_enabled));
                } else {
                    TasteThatApplication.showToast(TasteThatApplication
                            .getStringFromId(R.string.toast_caching_disabled));
                }
            }
        });

    }
}
