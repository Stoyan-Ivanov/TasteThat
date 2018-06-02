package com.stoyanivanov.tastethat.ui.fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.db.models.Component;
import com.stoyanivanov.tastethat.ui.activities.image_activity.ImageActivity;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.StartConstants;
import com.stoyanivanov.tastethat.network.NetworkManager;
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.network.models.Picture;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.images_recyclerview.ImagesRecyclerViewAdapter;
import com.stoyanivanov.tastethat.view_utils.views_behaviour.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


public class ChooseImageFragment extends BaseFragment {

    @BindView(R.id.tv_image_selection_header) TextView mTitleBar;
    @BindView(R.id.rv_images) RecyclerView recyclerView;

    private ArrayList<Component> mComponents;
    private int mComponentsPosition;
    private String mComponentName;
    private ArrayList<Picture> allPictures;
    private final int STARTING_OFFSET = 0;
    private final int OFFSET_STEP = 30;
    private int offset = STARTING_OFFSET;

    public static ChooseImageFragment newInstance(ArrayList<Component> components, int componentPosition) {
        ChooseImageFragment fragment = new ChooseImageFragment();
        Bundle arguments = new Bundle();

        arguments.putParcelableArrayList(StartConstants.EXTRA_FRAGMENT_COMPONENT, components);
        arguments.putInt(StartConstants.EXTRA_FRAGMENT_COMPONENT_POSITION, componentPosition);
        fragment.setArguments(arguments);

        return fragment;
    }

    @OnClick(R.id.iv_back_arrow)
    void onBackArrowPressed() {
        ((ImageActivity) getActivity()).finishByBackButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_choose_image, inflater, container);
        getExtras();
        allPictures = new ArrayList<>();
        mComponentName = mComponents.get(mComponentsPosition).getComponentName();

        NetworkManager.getInstance().getNextImages(mComponentName, this, offset);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleBar.setText(mComponentName);
    }

    private void getExtras() {
        mComponents = getArguments().getParcelableArrayList(StartConstants.EXTRA_FRAGMENT_COMPONENT);
        mComponentsPosition = getArguments().getInt(StartConstants.EXTRA_FRAGMENT_COMPONENT_POSITION);
    }

    public void onImagesGathered(ArrayList<Picture> pictures) {
        if(recyclerView.getAdapter() == null) {
            allPictures.addAll(pictures);
            configureRecyclerView(allPictures);
        } else {
            allPictures.addAll(pictures);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void configureRecyclerView(ArrayList<Picture> pictures) {
        GridLayoutManager layoutManager = new GridLayoutManager(TasteThatApplication.getStaticContext(),
                Constants.NUMBER_OF_IMAGE_COLUMNS);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new ImagesRecyclerViewAdapter(pictures, (position, picture) -> {
            mComponents.get(mComponentsPosition).setComponentImageUrl("https:" + picture.getThumbnailUrl());

            if(mComponents.size() - 1 == mComponentsPosition) {
                ((ImageActivity) getActivity()).saveCombinationToDB(mComponents);
            } else {
                ((ImageActivity) getActivity()).replaceFragment(ChooseImageFragment.newInstance(mComponents, ++mComponentsPosition));
            }
        }));

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                loadMoreImages();
            }
        });
    }

    private void loadMoreImages() {
        offset += OFFSET_STEP;
        NetworkManager.getInstance().getNextImages(mComponentName, this, offset);
    }
}
