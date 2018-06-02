package com.stoyanivanov.tastethat.ui.base_ui;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.ButterKnife;

/**
 * Created by stoyan.ivanov on 3/19/2018.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {
    protected P mPresenter;
    protected FirebaseUser mCurrentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        ButterKnife.bind(this);


        // TODO: ADD STATUS BAR COLOR
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(getResources().getColor());
//        }
    }

    @Override
    protected void onDestroy() {
        if(mPresenter != null) {
            mPresenter.onViewDestroy();
        }
        super.onDestroy();
    }
}
