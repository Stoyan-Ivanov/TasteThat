package com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanivanov.tastethat.db.DatabaseProvider;
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
    private OnClickViewHolder mOnClickListener;
    private String mRvTag;
    private CustomFilter customFilter;

    public CombinationsRecyclerViewAdapter(String rvTag, ArrayList<Combination> data, OnClickViewHolder listener) {
        this.mRvTag = rvTag;
        this.mData = data;
        this.mOnClickListener = listener;

        customFilter = new CustomFilter(this, rvTag);
    }

    public void setNewData(ArrayList<Combination> data) {
        this.mData = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @Override
    public CombinationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_combination, parent, false);

        return new CombinationsViewHolder(view, mRvTag, this);
    }

    @Override
    public void onBindViewHolder(CombinationsViewHolder holder, int position) {
        holder.bind(mData.get(position), mOnClickListener, position);
        holder.setPopUpMenu(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void deleteViewHolderFromRV(Combination combination, final int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());

        DatabaseProvider.getInstance().deleteCombination(combination.getCombinationKey());
    }

    public void filterData(String searched) {
        customFilter.filter(searched);
    }
}