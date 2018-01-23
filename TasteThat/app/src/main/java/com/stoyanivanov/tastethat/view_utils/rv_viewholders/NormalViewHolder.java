package com.stoyanivanov.tastethat.view_utils.rv_viewholders;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.interfaces.OnClickViewHolder;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.network.TasteThatApplication;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.controllers.PopUpMenuController;
import com.stoyanivanov.tastethat.view_utils.rv_adapters.MyRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.tableLikes;

/**
 * Created by stoyan-ivanov on 16.11.17.
 */

public class NormalViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.vh_tv_combinationName) CustomTextView combinationName;
    @BindView(R.id.vh_tv_like_counter) CustomTextView likeCounter;
    @BindView(R.id.vh_username) TextView username;
    @BindView(R.id.vh_iv_leftImg) ImageView leftImg;
    @BindView(R.id.vh_iv_rightImg) ImageView rightImg;
    @BindView(R.id.vh_options) ImageView options;
    @BindView(R.id.vh_civ_expand_combinatiov) CircleImageView expandCombination;

    private String combinationKey;
    private String rvTag;
    private MyRecyclerViewAdapter adapter;

    public NormalViewHolder(View itemView, String rvTag, MyRecyclerViewAdapter adapter) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        this.rvTag = rvTag;
        this.adapter = adapter;
    }

    public void bind(final Combination combination, final OnClickViewHolder listener, final int position) {
        StringBuilder displayNameBuilder = new StringBuilder();
        ArrayList<String> urls = combination.getUrls();
        ArrayList<String> components = combination.getComponents();

        for(int i = 0; i < components.size(); i++) {
            if(i < components.size() - 1) {
                displayNameBuilder.append(components.get(i));
                displayNameBuilder.append(" & ");
            } else {
                displayNameBuilder.append(components.get(i));
            }
        }

        final String displayNameOfCombination = displayNameBuilder.toString();

        username.setText("@"+combination.getUsername());
        combinationName.setText(displayNameOfCombination);
        loadImage(leftImg, urls.get(0));
        loadImage(rightImg, urls.get(1));

        tableLikes.child(combination.getCombinationKey())
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

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onItemLongClick(combination);
                return true;
            }
        });

        if(components.size() > Constants.MIN_REQUIRED_COMPONENTS) {
            expandCombination.setVisibility(View.VISIBLE);
        }
    }

    private void loadImage(ImageView imageView, String url) {
        Glide.with(TasteThatApplication.getStaticContext())
                .load(url)
                .centerCrop()
                .into(imageView);
    }

    public void deleteViewHolderFromRV(final int position) {
        adapter.deleteViewHolderFromRV(position);
    }

    public void setPopUpMenu(final int position) {
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), options);

                PopUpMenuController popUpMenuController = new PopUpMenuController(popupMenu, rvTag,
                                                            NormalViewHolder.this);
                popUpMenuController.inflatePopupMenu(position, combinationKey);
            }
        });
    }
}
