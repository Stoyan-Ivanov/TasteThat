package com.stoyanivanov.tastethat.ui.fragments;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanivanov.tastethat.R;
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

    private String componentName;

    public static ChooseImageFragment newInstance(String component) {
        ChooseImageFragment fragment = new ChooseImageFragment();
        Bundle arguments = new Bundle();

        arguments.putString(StartConstants.EXTRA_FRAGMENT_COMPONENT, component);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_choose_image, inflater, container);

        componentName = getArguments().getString(StartConstants.EXTRA_FRAGMENT_COMPONENT);
        header.setText(componentName);

        NetworkManager.getInstance().getNextImages(componentName, this);
        return view;
    }

    public void onImagesGathered(ArrayList<Picture> pictures) {
        configRecyclerview(pictures);
    }

    private void configRecyclerview(ArrayList<Picture> pictures) {
        recyclerView.setLayoutManager(new GridLayoutManager(TasteThatApplication.getStaticContext(),
                                                            Constants.NUMBER_OF_IMAGE_COLUMNS));

        recyclerView.setAdapter(new ImagesRecyclerViewAdapter(pictures, new OnClickImageListener() {
            @Override
            public void onItemClick(int position, Picture picture) {
                ((ImageActivity) getActivity()).savePicture(picture);
                ImageActivity.currComponent++;
                ((ImageActivity) getActivity()).replaceFragment();
            }
        }));

        RVScrollController scrollController = new RVScrollController();
        scrollController.addControlToBottomNavigation(recyclerView);
    }
}
