package com.stoyanivanov.tastethat.view_utils.recyclerview_utils;

import android.widget.TextView;

import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;

/**
 * Created by stoyan-ivanov on 23.10.17.
 */

public interface OnClickViewHolder {
    void onRateButtonClicked(Combination combination);

    void onItemClick(Combination combination);
}