package com.stoyanivanov.tastethat.view_utils.recyclerview_utils.images_recyclerview;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.network.models.Picture;

import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 15.11.17.
 */

public class ImagesRecyclerViewAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    private ArrayList<Picture> data;
    private OnClickImageListener listener;
    private LayoutInflater inflater;

    public ImagesRecyclerViewAdapter(ArrayList<Picture> data, OnClickImageListener onItemClickListener) {
        this.data = data;
        this.listener = onItemClickListener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext())
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
