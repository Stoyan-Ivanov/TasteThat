package com.stoyanivanov.tastethat.view_utils.recyclerview_utils.images_recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.network.models.Picture;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by stoyan-ivanov on 16.11.17.
 */

public class ImageViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.image_vh_iv) ImageView image;
    private final String httpPrefix = "https:";

    public ImageViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bind(final Picture picture, final OnClickImageListener listener, final int position) {
        Glide.with(TasteThatApplication.getStaticContext())
                .load(httpPrefix + picture.getThumbnailUrl())
                .centerCrop()
                .into(image);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position, picture);
            }
        });

    }
}
