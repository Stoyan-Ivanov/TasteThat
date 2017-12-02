package com.stoyanivanov.tastethat.view_utils;

import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;

import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.view_utils.rv_adapters.MyRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stoyan-ivanov on 02.12.17.
 */

public class CustomFilter implements Filterable {
    ArrayList<Combination> data;
    String searched;
    MyRecyclerViewAdapter adapter;

    public CustomFilter(ArrayList<Combination> mData, String searched, MyRecyclerViewAdapter adapter) {
        this.data = mData;
        this.searched = searched;
        this.adapter = adapter;

        getFilter().filter(searched);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searched = charSequence.toString();
                ArrayList<Combination> filteredData = new ArrayList<>();

                if (searched.isEmpty()) {
                    filteredData = data;
                } else {
                    for (Combination combination : data) {

                        if ((combination.getFirstComponent() + combination.getSecondComponent()).trim().contains(searched)) {
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
                Log.d("SII", "publishResults: " + (ArrayList<Combination>) results.values);
                adapter.setNewData((ArrayList<Combination>) results.values);
            }
        };
    }
}
