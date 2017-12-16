package com.stoyanivanov.tastethat.view_utils.rv_adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanivanov.tastethat.interfaces.OnClickViewHolder;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.view_utils.CustomFilter;
import com.stoyanivanov.tastethat.view_utils.rv_viewholders.NormalViewHolder;

import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 03.10.17.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<NormalViewHolder> {
    private ArrayList<Combination> mData = new ArrayList<>();
    private OnClickViewHolder listener;
    private String rvTag;
    private CustomFilter customFilter;

    private LayoutInflater mInflater;

    public MyRecyclerViewAdapter(String rvTag, ArrayList<Combination> data, OnClickViewHolder listener) {
        this.rvTag = rvTag;
        this.mData = data;
        this.listener = listener;

        customFilter = new CustomFilter(this);
    }

    public void setNewData(ArrayList<Combination> data) {
        this.mData = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    // inflates the cell layout from xml when needed
    @Override
    public NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.from(parent.getContext())
                .inflate(R.layout.rv_holder, parent, false);

        return new NormalViewHolder(view, rvTag, this);
    }

    // binds the data to the textview in each cel
    @Override
    public void onBindViewHolder(NormalViewHolder holder, int position) {
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