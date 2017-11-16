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
        private ImageView vhImg;

        public ViewHolder(View view) {
            super(view);
            vhImg = (ImageView) view.findViewById(R.id.image_vh_iv);
        }

        public ImageView getVhImg() {
            return vhImg;
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
        Glide.with(TasteThatApplication.getStaticContext())
                .load("https:" + data.get(position).getThumbnailUrl())
                .centerCrop()
                .into(holder.getVhImg());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
