package com.stoyanivanov.tastethat.view_utils.rv_viewholders;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.interfaces.OnClickItemLikeListener;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.controllers.PopUpMenuController;
import com.stoyanivanov.tastethat.view_utils.rv_adapters.MyRecyclerViewAdapter;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.tableLikes;
import static com.stoyanivanov.tastethat.constants.DatabaseReferences.tableUsers;

/**
 * Created by stoyan-ivanov on 16.11.17.
 */

public class NormalViewHolder extends RecyclerView.ViewHolder {
    private CustomTextView combinationName;
    private CustomTextView likeCounter;
    private CustomTextView user;
    private ImageView leftImg;
    private ImageView rightImg;
    private ImageView options;
    private String combinationNameKey;
    private String rvTag;
    private MyRecyclerViewAdapter adapter;

    public NormalViewHolder(View itemView, String rvTag, MyRecyclerViewAdapter adapter) {
        super(itemView);
        likeCounter = (CustomTextView) itemView.findViewById(R.id.vh_tv_like_counter);
        combinationName = (CustomTextView) itemView.findViewById(R.id.vh_tv_combinationName);
        user = (CustomTextView) itemView.findViewById(R.id.vh_username);
        leftImg = (ImageView) itemView.findViewById(R.id.vh_iv_leftImg);
        rightImg = (ImageView) itemView.findViewById(R.id.vh_iv_rightImg);
        options = (ImageView) itemView.findViewById(R.id.vh_options);
        this.rvTag = rvTag;
        this.adapter = adapter;
    }

    public void bind(final Combination combination, final OnClickItemLikeListener listener, final int position) {
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

        tableLikes.child(combination.getFirstComponent()+combination.getSecondComponent())
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

    public void deleteViewHolderFromRV(final int position) {
        adapter.deleteViewHolderFromRV(position);
    }

    public void setPopUpMenu(final int position) {
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), options);

                PopUpMenuController popUpMenuController = new PopUpMenuController(popupMenu, rvTag, NormalViewHolder.this);
                popUpMenuController.inflatePopupMenu(position, combinationNameKey);
            }
        });
    }
}
