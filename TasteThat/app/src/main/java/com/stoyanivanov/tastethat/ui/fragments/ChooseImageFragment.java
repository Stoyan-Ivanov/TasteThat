package com.stoyanivanov.tastethat.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.db.models.Component;
import com.stoyanivanov.tastethat.ui.activities.ImageActivity;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.images_recyclerview.OnClickImageListener;
import com.stoyanivanov.tastethat.network.NetworkManager;
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.network.models.Picture;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.images_recyclerview.ImagesRecyclerViewAdapter;
import com.stoyanivanov.tastethat.view_utils.controllers.RVScrollController;

import java.util.ArrayList;

import butterknife.BindView;


public class ChooseImageFragment extends BaseFragment {

    @BindView(R.id.tv_image_selection_header) CustomTextView header;
    @BindView(R.id.rv_images) RecyclerView recyclerView;

    private ArrayList<Component> components;
    private int componentsPosition;

    public static ChooseImageFragment newInstance(ArrayList<Component> components, int componentPosition) {
        ChooseImageFragment fragment = new ChooseImageFragment();
        Bundle arguments = new Bundle();

        arguments.putParcelableArrayList(StartConstants.EXTRA_FRAGMENT_COMPONENT, components);
        arguments.putInt("componentPosition", componentPosition);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getExtras();
        String componentName = components.get(componentsPosition).getComponentName();
        header.setText(componentName);

        NetworkManager.getInstance().getNextImages(componentName, this);
        return inflateCurrentView(R.layout.fragment_choose_image, inflater, container);
    }

    private void getExtras() {
        components = getArguments().getParcelableArrayList(StartConstants.EXTRA_FRAGMENT_COMPONENT);
        componentsPosition = getArguments().getInt("componentPosition");
    }

    public void onImagesGathered(ArrayList<Picture> pictures) {
        configureRecyclerView(pictures);
    }

    private void configureRecyclerView(ArrayList<Picture> pictures) {
        recyclerView.setLayoutManager(new GridLayoutManager(TasteThatApplication.getStaticContext(),
                                                            Constants.NUMBER_OF_IMAGE_COLUMNS));

        recyclerView.setAdapter(new ImagesRecyclerViewAdapter(pictures, (position, picture) -> {
            components.get(componentsPosition).setComponentImageUrl("https:" + picture.getThumbnailUrl());

            if(components.size() - 1 == componentsPosition) {
                ((ImageActivity) getActivity()).saveCombinationToDB(components);
            } else {
                ((ImageActivity) getActivity()).replaceFragment(ChooseImageFragment.newInstance(components, ++componentsPosition));
            }
        }));

        RVScrollController scrollController = new RVScrollController();
        scrollController.addControlToBottomNavigation(recyclerView);
    }
}
