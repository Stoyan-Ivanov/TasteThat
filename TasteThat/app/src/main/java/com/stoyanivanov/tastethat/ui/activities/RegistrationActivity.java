package com.stoyanivanov.tastethat.ui.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.BottomNavigationOptions;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.FragmentTags;
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends AppCompatActivity {
    @BindView(R.id.ctv_intro_header) CustomTextView header;
    @BindView(R.id.et_register_email) EditText etEmail;
    @BindView(R.id.et_register_password) EditText etPassword;
    @BindView(R.id.ctv_login_trigger) CustomTextView loginTrigger;
    @BindView(R.id.btn_register) Button btnRegister;

    @OnClick(R.id.btn_register)
    void registerUser() {
        String email = etEmail.getText().toString().trim();
        String password  = etPassword.getText().toString().trim();
        final int minSymbolsForValidPassword = 6;

        if(TextUtils.isEmpty(email)){
            TasteThatApplication.showToast(TasteThatApplication
                    .getStringFromId(R.string.toast_provide_email));
            return;
        }

        if(TextUtils.isEmpty(password)){
            TasteThatApplication.showToast(TasteThatApplication
                    .getStringFromId(R.string.toast_provide_password));
            return;
        }

        if(password.length() < minSymbolsForValidPassword) {
            TasteThatApplication.showToast(TasteThatApplication
                    .getStringFromId(R.string.toast_not_long_enough_password));
            return;
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(MainActivity.getIntent(getBaseContext(),
                                    BottomNavigationOptions.HOME, FragmentTags.HOME_FRAGMENT));
                        }else{
                            TasteThatApplication.showToast(TasteThatApplication
                                    .getStringFromId(R.string.toast_unable_to_register_user));
                        }
                    }
                });
    }

    @OnClick(R.id.ctv_login_trigger)
    void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ButterKnife.bind(this);

        Typeface custom_font =Typeface.createFromAsset(getResources().getAssets(), Constants.LOGIN_HEADER_TV_FONT);
        header.setTypeface(custom_font);
    }
}
