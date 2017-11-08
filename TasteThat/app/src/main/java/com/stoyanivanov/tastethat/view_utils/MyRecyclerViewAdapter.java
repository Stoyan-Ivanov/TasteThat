package com.stoyanivanov.tastethat.view_utils;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.interfaces.OnItemClickListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.models.Combination;

import java.util.ArrayList;

import static com.stoyanivanov.tastethat.DatabaseReferences.*;

/**
 * Created by stoyan-ivanov on 03.10.17.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Combination> mData = new ArrayList<>();
    private OnItemClickListener listener;
    private String rvTag;

    private LayoutInflater mInflater;

    public MyRecyclerViewAdapter(String rvTag, ArrayList<Combination> data, OnItemClickListener listener) {
        this.rvTag = rvTag;
        this.mData = data;
        this.listener = listener;

    }

    public void setNewData(ArrayList<Combination> data) {
        this.mData = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView combinationName;
        public CustomTextView likeCounter;
        public CustomTextView user;
        public ImageView leftImg;
        public ImageView rightImg;
        public ImageView options;
        public String combinationNameKey;

        public ViewHolder(View itemView) {
            super(itemView);
            likeCounter = (CustomTextView) itemView.findViewById(R.id.vh_tv_like_counter);
            combinationName = (CustomTextView) itemView.findViewById(R.id.vh_tv_combinationName);
            user = (CustomTextView) itemView.findViewById(R.id.vh_username);
            leftImg = (ImageView) itemView.findViewById(R.id.vh_iv_leftImg);
            rightImg = (ImageView) itemView.findViewById(R.id.vh_iv_rightImg);
            options = (ImageView) itemView.findViewById(R.id.vh_options);
        }

        public void bind(final Combination combination, final OnItemClickListener listener, final int position) {
            combinationNameKey = combination.getFirstComponent() + combination.getSecondComponent();
            final String nameOfCombination = combination.getFirstComponent() + " & " + combination.getSecondComponent();
            combinationName.setText(nameOfCombination);

            tableUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            tableLikes
                    .child(combination.getFirstComponent()+combination.getSecondComponent())
                    .addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    likeCounter.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(combination, likeCounter, position);
                }
            });

        }

        private void setPopUpMenu(final int position) {
            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), options);

                    PopUpMenuController popUpMenuController = new PopUpMenuController(popupMenu, rvTag, ViewHolder.this);
                    popUpMenuController.inflatePopupMenu(position, combinationNameKey);
                }
            });
        }

        public void deleteCombinationFromRV(final int position) {
            mData.remove(position);
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(position, getItemCount());
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
        holder.bind(mData.get(position), listener, position);
        holder.setPopUpMenu(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public Combination getItem(int position) {
        return mData.get(position);
    }

//    public int getItemViewType(int position) {
//        return position;
//    }

}