package com.stoyanivanov.tastethat.interfaces;

import com.stoyanivanov.tastethat.network.network_models.Picture;

/**
 * Created by stoyan-ivanov on 16.11.17.
 */

public interface OnClickItemListener {
    void onItemClick(int position, Picture picture);
}
