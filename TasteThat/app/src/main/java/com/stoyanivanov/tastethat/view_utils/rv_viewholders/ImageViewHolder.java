package com.stoyanivanov.tastethat.view_utils.rv_viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.interfaces.OnClickItemListener;
import com.stoyanivanov.tastethat.network.TasteThatApplication;
import com.stoyanivanov.tastethat.network.network_models.Picture;

/**
 * Created by stoyan-ivanov on 16.11.17.
 */

public class ImageViewHolder extends RecyclerView.ViewHolder{
    private ImageView vhImg;

    public ImageViewHolder(View view) {
        super(view);
        vhImg = (ImageView) view.findViewById(R.id.image_vh_iv);
    }

    public ImageView getVhImg() {
        return vhImg;
    }

    public void bind(final Picture picture, final OnClickItemListener listener, final int position) {
        Glide.with(TasteThatApplication.getStaticContext())
                .load("https:" + picture.getThumbnailUrl())
                .centerCrop()
                .into(vhImg);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position, picture);
            }
        });

    }
}
