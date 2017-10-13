package com.stoyanivanov.tastethat.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.stoyanivanov.tastethat.MainActivity;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.MyRecyclerViewAdapter;

import java.util.ArrayList;


public class CombinationsFragment extends Fragment {

    CustomTextView likeCounter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_combinations, container, false);



        ArrayList<Combination> allCombinations = new ArrayList<>();
        allCombinations.add(new Combination("Caramel", "Salt"));
        allCombinations.add(new Combination("Tomato", "Salt"));
        allCombinations.add(new Combination("Tea", "Salt"));
        allCombinations.add(new Combination("Caramel", "Chocolate"));
        allCombinations.add(new Combination("Caramel", "Patlajanche"));
        allCombinations.add(new Combination("Rakija", "Salad"));
        allCombinations.add(new Combination("KISELO MLEKO", "Waffles"));


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MyRecyclerViewAdapter(allCombinations));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && MainActivity.bottomNavigationView.isShown()) {
                    MainActivity.bottomNavigationView.setVisibility(View.GONE);
                } else if (dy < 0 || dy == 0) {
                    MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }



}
