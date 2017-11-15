package com.stoyanivanov.tastethat.view_utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.network.TasteThatApplication;
import com.stoyanivanov.tastethat.network.network_models.Picture;

import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 15.11.17.
 */

public class ImagesRecyclerviewAdapter extends RecyclerView.Adapter<ImagesRecyclerviewAdapter.ViewHolder> {
    private ArrayList<Picture> data;
    private LayoutInflater mInflater;

    public ImagesRecyclerviewAdapter(ArrayList<Picture> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView leftImg;
        public ImageView centerImg;
        public ImageView rightImg;

        public ViewHolder(View view) {
            super(view);

            leftImg = (ImageView) view.findViewById(R.id.image_vh_iv_left);
            centerImg = (ImageView) view.findViewById(R.id.image_vh_iv_center);
            rightImg = (ImageView) view.findViewById(R.id.image_vh_iv_right);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.from(parent.getContext())
                .inflate(R.layout.rv_image_holder, parent, false);

        return new ImagesRecyclerviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageView[] imageviews = {holder.rightImg, holder.centerImg, holder.leftImg};

        for (int i = 0; i < imageviews.length; i++) {
            Glide.with(TasteThatApplication.getStaticContext())
                    .load("https:" + data.get(i).getThumbnailUrl())
                    .fitCenter()
                    .into(imageviews[i]);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
