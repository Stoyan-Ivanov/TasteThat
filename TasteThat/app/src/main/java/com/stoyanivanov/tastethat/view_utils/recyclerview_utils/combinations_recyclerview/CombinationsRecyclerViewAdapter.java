package com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.OnClickViewHolder;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.view_utils.CustomFilter;

import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 03.10.17.
 */

public class CombinationsRecyclerViewAdapter extends RecyclerView.Adapter<CombinationsViewHolder> {
    private ArrayList<Combination> mData;
    private OnClickViewHolder listener;
    private String rvTag;
    private CustomFilter customFilter;

    public CombinationsRecyclerViewAdapter(String rvTag, ArrayList<Combination> data, OnClickViewHolder listener) {
        this.rvTag = rvTag;
        this.mData = data;
        this.listener = listener;

        customFilter = new CustomFilter(this);
    }

    public void setNewData(ArrayList<Combination> data) {
        this.mData = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @Override
    public CombinationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_combination_holder, parent, false);

        return new CombinationsViewHolder(view, rvTag, this);
    }

    @Override
    public void onBindViewHolder(CombinationsViewHolder holder, int position) {
        holder.bind(mData.get(position), listener, position);
        holder.setPopUpMenu(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void deleteViewHolderFromRV(final int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void filterData(String searched) {
        customFilter.filter(mData, searched);
    }
}