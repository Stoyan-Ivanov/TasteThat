package com.stoyanivanov.tastethat.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.network.TasteThatApplication;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;

import java.util.ArrayList;

public class CombinationDetailsFragment extends Fragment {
    private ImageView imageTopLeft;
    private ImageView imageTopRight;
    private ImageView imageBottomLeft;
    private ImageView imageBottomRight;
    private CustomTextView combinationNameHeader;
    private CustomTextView authorName;
    private CustomTextView combinationDescription;
    private Combination currCombination;
    private ImageView [] images;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_combination_details, container, false);

        combinationNameHeader = (CustomTextView) view.findViewById(R.id.ctv_combination_details_header);
        authorName = (CustomTextView) view.findViewById(R.id.ctv_details_username);
        imageTopLeft = (ImageView) view.findViewById(R.id.iv_top_left);
        imageTopRight = (ImageView) view.findViewById(R.id.iv_top_right);
        imageBottomLeft = (ImageView) view.findViewById(R.id.iv_bottom_left);
        imageBottomRight = (ImageView) view.findViewById(R.id.iv_bottom_right);
        combinationDescription = (CustomTextView) view.findViewById(R.id.ctv_combination_details_description);

        currCombination = (Combination) getArguments().getSerializable("currCombination");

        images= new ImageView[] {imageTopLeft, imageTopRight, imageBottomLeft, imageBottomRight};

        Log.d("SII", "onCreateView: " + currCombination);

        loadCombinationName();
        loadImages();
        loadDescription();

        return view;
    }

    private void loadCombinationName() {
        StringBuilder displayNameBuilder = new StringBuilder();
        ArrayList<String> components = currCombination.getComponents();

        for(int i = 0; i < components.size(); i++) {
            if(i < components.size() - 1) {
                displayNameBuilder.append(components.get(i));
                displayNameBuilder.append(" & ");
            } else {
                displayNameBuilder.append(components.get(i));
            }
        }
        combinationNameHeader.setText(displayNameBuilder.toString());

        String authorField = "Author: " + "@" + currCombination.getUsername();
        authorName.setText(authorField);
    }

    private void loadImages() {
        ArrayList<String> urls = currCombination.getUrls();

        for(int i = 0; i < urls.size(); i++) {
            Glide.with(TasteThatApplication.getStaticContext())
                    .load(urls.get(i))
                    .centerCrop()
                    .into(images[i]);
        }
    }

    private void loadDescription() {
    }
}
