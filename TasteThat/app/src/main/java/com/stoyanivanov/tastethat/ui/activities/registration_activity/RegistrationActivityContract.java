package com.stoyanivanov.tastethat.ui.activities.registration_activity;

import android.content.Intent;

import com.stoyanivanov.tastethat.ui.base_ui.BaseViewContract;

/**
 * Created by Stoya on 5.5.2018 г..
 */

public interface RegistrationActivityContract extends BaseViewContract {

    void showToast(int messageId);

    void startNewActivity(Intent intent);
}
