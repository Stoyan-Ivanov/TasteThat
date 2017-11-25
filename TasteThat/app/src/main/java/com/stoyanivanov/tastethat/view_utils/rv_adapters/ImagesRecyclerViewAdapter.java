package com.stoyanivanov.tastethat.view_utils.rv_adapters;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.interfaces.OnClickItemLikeListener;
import com.stoyanivanov.tastethat.interfaces.OnClickItemListener;
import com.stoyanivanov.tastethat.network.TasteThatApplication;
import com.stoyanivanov.tastethat.network.network_models.Picture;
import com.stoyanivanov.tastethat.view_utils.rv_viewholders.ImageViewHolder;

import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 15.11.17.
 */

public class ImagesRecyclerViewAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    private ArrayList<Picture> data;
    private OnClickItemListener listener;
    private LayoutInflater mInflater;

    public ImagesRecyclerViewAdapter(ArrayList<Picture> data, OnClickItemListener onItemClickListener) {
        this.data = data;
        this.listener = onItemClickListener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.from(parent.getContext())
                .inflate(R.layout.rv_image_holder, parent, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.bind(data.get(position), listener, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
