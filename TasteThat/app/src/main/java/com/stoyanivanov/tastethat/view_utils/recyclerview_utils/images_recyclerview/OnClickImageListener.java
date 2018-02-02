package com.stoyanivanov.tastethat.view_utils.recyclerview_utils.images_recyclerview;

import com.stoyanivanov.tastethat.network.models.Picture;

/**
 * Created by stoyan-ivanov on 16.11.17.
 */

public interface OnClickImageListener {
    void onItemClick(int position, Picture picture);
}
