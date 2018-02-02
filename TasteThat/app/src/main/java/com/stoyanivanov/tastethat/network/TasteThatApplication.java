package com.stoyanivanov.tastethat.network;

import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by stoyan-ivanov on 14.11.17.
 */

public class TasteThatApplication extends Application {
    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }

    public static void showToast(String message) {
        Toast toast=Toast.makeText(applicationContext, message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }

    public static Context getStaticContext() {
        return applicationContext;
    }
}
