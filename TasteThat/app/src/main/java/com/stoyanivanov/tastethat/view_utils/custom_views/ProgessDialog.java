package com.stoyanivanov.tastethat.view_utils.custom_views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class ProgessDialog {

    private Dialog mDialog;
    private ProgressBar mProgressBar;

    public ProgessDialog(Context context) {

        mDialog = new Dialog(context);
        mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);


        RelativeLayout relativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams);
        mProgressBar = new ProgressBar(context);
        RelativeLayout.LayoutParams layoutParams_progress = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams_progress.addRule(RelativeLayout.CENTER_IN_PARENT);

        LinearLayout.LayoutParams linearLayoutParams_progress = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        linearLayoutParams_progress.gravity = Gravity.CENTER;
        mProgressBar.setLayoutParams(layoutParams_progress);

        relativeLayout.addView(mProgressBar);

        mDialog.getWindow().setContentView(relativeLayout, layoutParams);
        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void show() {
        if (!mDialog.isShowing() && mDialog != null) {
            mDialog.show();

        }
    }

    public void dismiss() {
        if (mDialog.isShowing() && mDialog != null) {
            mDialog.dismiss();
        }
    }

    public void setCancelable(boolean cancelable) {
        mDialog.setCancelable(cancelable);
    }


    public void setCanceledOnTouchOutside(boolean flag) {
        mDialog.setCanceledOnTouchOutside(flag);
    }

    public void setColor(int colour) {
        mProgressBar.getIndeterminateDrawable().setColorFilter(colour, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public boolean isShowing() {

        return mDialog.isShowing();
    }
}
