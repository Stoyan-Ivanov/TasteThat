package com.stoyanivanov.tastethat.network;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.stoyanivanov.tastethat.constants.Constants;

/**
 * Created by stoyan-ivanov on 14.11.17.
 */

public class TasteThatApplication extends Application {
    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        configureDataCaching();
    }

    private void configureDataCaching() {
        final SharedPreferences sharedPrefs = TasteThatApplication
                .getStaticContext()
                .getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        FirebaseDatabase.getInstance().setPersistenceEnabled(
                sharedPrefs.getBoolean(Constants.CACHING_KEY, false));
    }

    public static void showToast(String message) {
        Toast toast=Toast.makeText(applicationContext, message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }

    public static void hideVirtualKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if(imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showVirtualKeyboard() {
        InputMethodManager imm = (InputMethodManager) applicationContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    public static Context getStaticContext() {
        return applicationContext;
    }

    public static String getStringFromId(int id) {
        return applicationContext.getResources().getString(id);
    }
}
