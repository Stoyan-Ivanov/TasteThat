package com.stoyanivanov.tastethat.view_utils;

import android.widget.Filter;
import android.widget.Filterable;

import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.CombinationsRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 02.12.17.
 */

public class CustomFilter implements Filterable {
    private ArrayList<Combination> data;
    private String searched;
    private CombinationsRecyclerViewAdapter adapter;

    public CustomFilter(CombinationsRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    public void filter(ArrayList<Combination> data, String searched) {
        this.data = data;
        this.searched = searched;
        getFilter().filter(searched);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                searched = charSequence.toString();
                ArrayList<Combination> filteredData = new ArrayList<>();

                if (searched.isEmpty()) {
                    filteredData = data;
                } else {
                    for (Combination combination : data) {

                        if (combination.toString().trim().contains(searched)) {
                            filteredData.add(combination);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredData;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                adapter.setNewData((ArrayList<Combination>) results.values);
            }
        };
    }
}
