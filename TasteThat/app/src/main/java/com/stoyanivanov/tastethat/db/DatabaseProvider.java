package com.stoyanivanov.tastethat.db;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.network.TasteThatApplication;
import com.stoyanivanov.tastethat.ui.fragments.AllCombinationsFragment;
import com.stoyanivanov.tastethat.ui.fragments.LikedCombinationsFragment;
import com.stoyanivanov.tastethat.ui.fragments.UploadedCombinationsFragment;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.NormalViewHolder;

import java.util.ArrayList;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.tableCombinations;
import static com.stoyanivanov.tastethat.constants.DatabaseReferences.tableLikes;
import static com.stoyanivanov.tastethat.constants.DatabaseReferences.tableUsers;

/**
 * Created by Stoyan on 2.2.2018 г..
 */

public class DatabaseProvider {
    private static DatabaseProvider instance;
    private final int TOTAL_COMBINATIONS_FOR_ONE_LOAD = 5;

    public static DatabaseProvider getInstance() {
        if(instance == null) {
            instance = new DatabaseProvider();
        }
        return instance;
    }

    private DatabaseProvider() {}

    public void getCombinations(final String nodeId, final ArrayList<Combination> combinations, final AllCombinationsFragment fragment) {
            Query query;

            if(nodeId == null) {
                query = tableCombinations
                        .limitToFirst(TOTAL_COMBINATIONS_FOR_ONE_LOAD)
                        .orderByChild("timestamp");
            } else {
                query = tableCombinations
                        .limitToFirst(TOTAL_COMBINATIONS_FOR_ONE_LOAD)
                        .orderByChild("timestamp")
                        .startAt((long)combinations.get(combinations.size() - 1).getTimestamp(),nodeId +1);
            }

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChildren()){
                        TasteThatApplication.showToast("No more combinations");
                    }
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Combination currCombination = snapshot.getValue(Combination.class);
                        combinations.add(currCombination);
                    }
                    fragment.onDataGathered(combinations);
                }

                @Override public void onCancelled(DatabaseError databaseError) {}});
    }

    public void getCombinationLikes(Combination combination, final NormalViewHolder viewHolder) {

        tableLikes.child(combination.getCombinationKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String numberOfLikes = String.valueOf(dataSnapshot.getChildrenCount());
                        viewHolder.setLikes(numberOfLikes);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    public void getLikedCombinations(final LikedCombinationsFragment fragment) {
        final ArrayList<Combination> likedCombinations = new ArrayList<>();

        tableUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Constants.USER_LIKED_COMBINATIONS)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            Combination currCombination = dataSnapshot.getValue(Combination.class);
                            likedCombinations.add(currCombination);
                        }

                        fragment.onDataGathered(likedCombinations);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("SII", "onCancelled: error");
                    }
                });
    }

    public void getUploadedCombinations(final UploadedCombinationsFragment fragment) {
        final ArrayList<Combination> uploadedCombinations = new ArrayList<>();

        tableUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        uploadedCombinations.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            Combination currCombination = dataSnapshot.getValue(Combination.class);
                            uploadedCombinations.add(currCombination);
                        }
                        fragment.onDataGathered(uploadedCombinations);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("SII", "onCancelled: error");
                    }
                });
    }
}
