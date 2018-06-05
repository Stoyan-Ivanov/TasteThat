package com.stoyanivanov.tastethat.view_utils;

import android.widget.Filter;
import android.widget.Filterable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.DatabaseReferences;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.CombinationsRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by stoyan-ivanov on 02.12.17.
 */

public class CustomFilter {
    private String mSearchedString;
    private CombinationsRecyclerViewAdapter mAdapter;
    private String mRvTag;

    public CustomFilter(CombinationsRecyclerViewAdapter adapter, String rvTag) {
        this.mAdapter = adapter;
        this.mRvTag = rvTag;
    }

    public void filter(String searched) {
        this.mSearchedString = searched;

        switch (mRvTag) {
            case Constants.RV_ALL_COMBINATIONS: filterAllCombinations(); break;
            case Constants.RV_UPLOADED_COMBINATIONS: filterUploadedCombinations(); break;
            case Constants.RV_RATED_COMBINATIONS: filterRatedCombinations(); break;
        }
    }

    private void filterAllCombinations() {
        DatabaseReferences.tableCombinations.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                filter(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void filterUploadedCombinations() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReferences.tableUsers
                .child(userId)
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        filter(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) { }
                });
    }

    private void filterRatedCombinations() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReferences.tableUsers
                .child(userId)
                .child(Constants.USER_RATED_COMBINATIONS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        filter(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) { }
                });
    }

    private void filter(DataSnapshot dataSnapshot) {
        ArrayList<Combination> filteredCombinations = new ArrayList<>();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Combination combination = snapshot.getValue(Combination.class);
            if (combination != null) {
                String combinationName = combination.getCombinationName();

                if (combinationName.contains(mSearchedString)) {
                    filteredCombinations.add(combination);
                }
            }
        }
        mAdapter.setNewData(filteredCombinations);
    }
}
