package com.stoyanivanov.tastethat;

import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;

/**
 * Created by stoyan-ivanov on 23.10.17.
 */

public interface OnItemClickListener {
    void onItemClick(Combination combination, CustomTextView likeCounter, int position);
}