package com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.db.DatabaseProvider;
import com.stoyanivanov.tastethat.db.models.Component;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.OnClickViewHolder;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.view_utils.controllers.PopUpMenuController;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by stoyan-ivanov on 16.11.17.
 */

public class CombinationsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.vh_tv_combinationName) TextView combinationName;
    @BindView(R.id.vh_username) TextView username;
    @BindView(R.id.vh_iv_leftImg) ImageView leftImg;
    @BindView(R.id.vh_iv_rightImg) ImageView rightImg;
    @BindView(R.id.vh_options) ImageView options;
    @BindView(R.id.vh_civ_expand_combination) CircleImageView expandCombination;
    @BindView(R.id.btn_rate) TextView rateCombination;
    @BindView(R.id.combination_vh_rating_bar) RatingBar combinationRatingBar;

    private String combinationKey;
    private String rvTag;
    private CombinationsRecyclerViewAdapter adapter;

    public CombinationsViewHolder(View itemView, String rvTag, CombinationsRecyclerViewAdapter adapter) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        this.rvTag = rvTag;
        this.adapter = adapter;
    }

    public void bind(final Combination combination, final OnClickViewHolder listener, final int position) {
        ArrayList<Component> components = combination.getComponents();
        combinationKey = combination.getCombinationKey();

        username.setText("@"+combination.getUsername());
        combinationName.setText(combination.toString());
        loadImage(leftImg, components.get(0).getComponentImageUrl());
        loadImage(rightImg, components.get(1).getComponentImageUrl());


        //DatabaseProvider.getInstance().getCombinationLikes(combination, this);

        //if (!combination.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            rateCombination.setOnClickListener(v -> listener.onRateButtonClicked(combination));
        //}

        itemView.setOnClickListener(view -> listener.onItemClick(combination));

        if(components.size() > Constants.MIN_REQUIRED_COMPONENTS) {
            expandCombination.setVisibility(View.VISIBLE);
        }
    }

//    public void setLikes(String likes) {
//        likeCounter.setText(likes);
//    }

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
        options.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), options);

            PopUpMenuController popUpMenuController = new PopUpMenuController(popupMenu, rvTag,
                                                        CombinationsViewHolder.this);
            popUpMenuController.inflatePopupMenu(position, combinationKey);
        });
    }
}
