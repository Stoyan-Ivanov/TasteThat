package com.stoyanivanov.tastethat.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.interfaces.OnClickItemListener;
import com.stoyanivanov.tastethat.network.TasteThatApplication;
import com.stoyanivanov.tastethat.network.network_models.NextImagesResponse;
import com.stoyanivanov.tastethat.network.network_models.Picture;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.rv_adapters.ImagesRecyclerviewAdapter;
import com.stoyanivanov.tastethat.view_utils.controllers.RVScrollController;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChooseImageFragment extends Fragment {
    private CustomTextView header;
    private RecyclerView recyclerView;
    private NextImagesResponse nextImagesResponse;
    private String ingredient;
    public static final String EXTRA_INGREDIENT = "extraIngredient";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_image, container, false);

        ingredient = getArguments().getString(EXTRA_INGREDIENT);
        header = (CustomTextView) view.findViewById(R.id.tv_image_selection_header);
        header.setText(ingredient);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_images);

        getNextImages();
        return view;
    }

    public void getNextImages() {
        Call<NextImagesResponse> call = TasteThatApplication.getApi().getNextImages(ingredient);

        call.enqueue(new Callback<NextImagesResponse>() {
            @Override
            public void onResponse(Call<NextImagesResponse> call, Response<NextImagesResponse> response) {
                nextImagesResponse = response.body();
                ArrayList<Picture> pictures = nextImagesResponse.getData().getResult().getPictures();
                configRecyclerview(pictures);
            }

            @Override
            public void onFailure(Call<NextImagesResponse> call, Throwable t) {
                Log.d("SII", "onFailure: getNextImages()");
            }
        });
    }

    private void configRecyclerview(ArrayList<Picture> pictures) {
        recyclerView.setLayoutManager(new GridLayoutManager(TasteThatApplication.getStaticContext(), 3));
        recyclerView.setAdapter(new ImagesRecyclerviewAdapter(pictures, new OnClickItemListener() {
            @Override
            public void onItemClick(int position) {

            }
        }));

        RVScrollController scrollController = new RVScrollController();
        scrollController.addControlToBottomNavigation(recyclerView);
    }

}
