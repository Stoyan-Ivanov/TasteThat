package com.stoyanivanov.tastethat.ui.activities.registration_activity;

import android.text.TextUtils;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.ui.base_ui.BasePresenter;

/**
 * Created by Stoya on 5.5.2018 г..
 */

public class RegistrationActivityPresenter extends BasePresenter<RegistrationActivityContract> {

    public RegistrationActivityPresenter(RegistrationActivityContract view) {
        setView(view);
    }

    public void checkIfPasswordIsCorrect(String email, String password) {
        final int MIN_SYMBOLS_FOR_A_VALID_PASSWORD = 6;

        if(TextUtils.isEmpty(email)){
           view.showToast(R.string.toast_provide_email);
            return;
        }

        if(TextUtils.isEmpty(password)){
            view.showToast(R.string.toast_provide_password);
            return;
        }

        if(password.length() < MIN_SYMBOLS_FOR_A_VALID_PASSWORD) {
            view.showToast(R.string.toast_not_long_enough_password);
            return;
        }
    }

}
