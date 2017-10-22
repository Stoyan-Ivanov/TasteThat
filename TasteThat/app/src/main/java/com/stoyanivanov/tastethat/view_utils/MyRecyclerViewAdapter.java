package com.stoyanivanov.tastethat.view_utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.MainActivity;
import com.stoyanivanov.tastethat.OnItemClickListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.models.Combination;

import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 03.10.17.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Combination> mData = new ArrayList<>();
    private OnItemClickListener listener;

    private LayoutInflater mInflater;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference dbRef = database.getReference();

    public MyRecyclerViewAdapter(ArrayList<Combination> data, OnItemClickListener listener) {
        this.mData = data;
        this.listener = listener;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView combinationName;
        public CustomTextView likeCounter;
        public ImageView leftImg;
        public ImageView rightImg;

        public ViewHolder(View itemView) {
            super(itemView);
            likeCounter = (CustomTextView) itemView.findViewById(R.id.tv_like_counter);
            combinationName = (CustomTextView) itemView.findViewById(R.id.tv_combinationName);
            leftImg = (ImageView) itemView.findViewById(R.id.iv_leftImg);
            rightImg = (ImageView) itemView.findViewById(R.id.iv_rightImg);
        }

        public void bind(final Combination combination, final OnItemClickListener listener) {
            final String nameOfCombination = combination.getFirstComponent() + " & " + combination.getSecondComponent();
            combinationName.setText(nameOfCombination);
            likeCounter.setText(String.valueOf(combination.getLikes()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(combination, likeCounter);
                }
            });
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
        holder.bind(mData.get(position), listener);
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