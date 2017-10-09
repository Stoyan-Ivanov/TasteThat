package com.stoyanivanov.tastethat.view_utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.models.Combination;

import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 03.10.17.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Combination> mData = new ArrayList<>();
    private LayoutInflater mInflater;

    public MyRecyclerViewAdapter(ArrayList<Combination> data) {
        this.mData = data;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView combinationName;
        public ImageView leftImg;
        public ImageView rightImg;

        public ViewHolder(View itemView) {
            super(itemView);
            combinationName = (TextView) itemView.findViewById(R.id.tv_combinationName);
            leftImg = (ImageView) itemView.findViewById(R.id.iv_leftImg);
            rightImg = (ImageView) itemView.findViewById(R.id.iv_rightImg);
        }
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.from(parent.getContext())
                .inflate(R.layout.rv_holder, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cel
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Combination currCombination = mData.get(position);

        holder.combinationName.setText(currCombination.getFirstComponent() + " & " + currCombination.getSecondComponent());
//        holder.leftImg.setImageDrawable(currCombination.getLeftImage());
//        holder.rightImg.setImageDrawable(currCombination.getRightImage());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    // convenience method for getting data at click position
    public Combination getItem(int id) {
        return mData.get(id);
    }
}