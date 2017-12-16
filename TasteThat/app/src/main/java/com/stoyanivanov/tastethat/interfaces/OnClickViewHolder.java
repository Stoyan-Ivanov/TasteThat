package com.stoyanivanov.tastethat.interfaces;

import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;

/**
 * Created by stoyan-ivanov on 23.10.17.
 */

public interface OnClickViewHolder {
    void onItemClick(Combination combination, CustomTextView likeCounter, int position);
}