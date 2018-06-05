package com.stoyanivanov.tastethat.ui.activities.registration_activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.ui.activities.LoginActivity;
import com.stoyanivanov.tastethat.ui.activities.main_activity.MainActivity;
import com.stoyanivanov.tastethat.ui.base_ui.BaseActivity;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class RegistrationActivity extends BaseActivity<RegistrationActivityPresenter> implements RegistrationActivityContract {
    @BindView(R.id.tv_intro_title) CustomTextView header;
    @BindView(R.id.et_register_email) EditText etEmail;
    @BindView(R.id.et_register_password) EditText etPassword;
    @BindView(R.id.ctv_login_trigger) CustomTextView loginTrigger;
    @BindView(R.id.btn_register) Button btnRegister;

    @OnClick(R.id.btn_register)
    void registerUser() {
        String email = etEmail.getText().toString().trim();
        String password  = etPassword.getText().toString().trim();

        if(mPresenter.checkIfPasswordAndEmailAreCorrect(email, password)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            startNewActivity(MainActivity.getIntent(getBaseContext(),
                                    BottomNavigationOptions.HOME, FragmentTags.HOME_FRAGMENT));
                        } else {
                            showToast(R.string.toast_unable_to_register_user);
                        }
                    });
        }
    }

    @OnClick(R.id.ctv_login_trigger)
    void startLoginActivity() {
        startNewActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mPresenter = new RegistrationActivityPresenter(this);

        Typeface custom_font =Typeface.createFromAsset(getResources().getAssets(), Constants.LOGIN_HEADER_TV_FONT);
        header.setTypeface(custom_font);
    }

    @Override
    public void showToast(int messageId) {
        TasteThatApplication.showToast(TasteThatApplication.getStringFromId(messageId));
    }

    @Override
    public void startNewActivity(Intent intent) {
        startActivity(intent);
        finish();
    }
}
