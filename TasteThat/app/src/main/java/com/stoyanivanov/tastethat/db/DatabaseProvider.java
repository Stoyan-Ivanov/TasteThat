package com.stoyanivanov.tastethat.db;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.ContentOrder;
import com.stoyanivanov.tastethat.constants.DatabaseReferences;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.network.TasteThatApplication;
import com.stoyanivanov.tastethat.ui.fragments.AllCombinationsFragment;
import com.stoyanivanov.tastethat.ui.fragments.LikedCombinationsFragment;
import com.stoyanivanov.tastethat.ui.fragments.UploadedCombinationsFragment;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.CombinationsViewHolder;

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

    public void saveCombination(final Combination newCombination) {
        String combinationKey = newCombination.getCombinationKey();

        DatabaseReferences.tableCombinations.child(combinationKey)
                .setValue(newCombination);

        // create negative timestamp for sorting purpose
        final DatabaseReference timestampReference = DatabaseReferences.tableCombinations
                .child(combinationKey).child("timestamp");

        timestampReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    if (!(Long.parseLong(dataSnapshot.getValue().toString()) < 0)) {

                        long negativeTimestamp = 0 - Long.parseLong(dataSnapshot.getValue().toString());

                        newCombination.setTimestamp(negativeTimestamp);
                        timestampReference.setValue(negativeTimestamp);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("SII", databaseError.getMessage());
            }
        });

        DatabaseReferences.tableUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .child(combinationKey)
                .setValue(newCombination);

        TasteThatApplication.showToast(String.valueOf((R.string.toast_successfull_adding)));
    }

    public void getAllCombinations(final String nodeId, final ArrayList<Combination> combinations,
                                   final AllCombinationsFragment fragment, final ContentOrder orderCriteria) {

            Query query;
            switch(orderCriteria) {
                case MOST_LIKED:
                    query = getQueryOrderByLikes(tableCombinations, nodeId, combinations);
                    break;

                default: query = getQueryOrderByTimestamp(tableCombinations, nodeId, combinations);
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

    private Query getQueryOrderByTimestamp(DatabaseReference tableReference,
                                        String nodeId, final ArrayList<Combination> combinations) {
        Query query;

        if(nodeId == null) {
            query = tableReference
                    .limitToFirst(TOTAL_COMBINATIONS_FOR_ONE_LOAD)
                    .orderByChild("timestamp");
        } else {
            query = tableReference
                    .limitToFirst(TOTAL_COMBINATIONS_FOR_ONE_LOAD)
                    .orderByChild("timestamp")
                    .startAt((long)combinations.get(combinations.size() - 1).getTimestamp(),nodeId +1);
        }

        return query;
    }

    private Query getQueryOrderByLikes(DatabaseReference tableReference,
                                           String nodeId, final ArrayList<Combination> combinations) {
        Query query;

        if(nodeId == null) {
            query = tableReference
                    .limitToFirst(TOTAL_COMBINATIONS_FOR_ONE_LOAD)
                    .orderByChild("likes");
        } else {
            query = tableReference
                    .limitToFirst(TOTAL_COMBINATIONS_FOR_ONE_LOAD)
                    .orderByChild("likes")
                    .startAt((long)combinations.get(combinations.size() - 1).getLikes(),nodeId +1);
        }

        return query;
    }

    public void getCombinationLikes(Combination combination, final CombinationsViewHolder viewHolder) {

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

    public void getLikedCombinations(final String nodeId, final ArrayList<Combination> likedCombinations,
                                     final LikedCombinationsFragment fragment, final ContentOrder orderCriteria) {

        final DatabaseReference userLikedCombinations = tableUsers.child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).child(Constants.USER_LIKED_COMBINATIONS);

        Query query;
        switch(orderCriteria) {
            case MOST_LIKED:
                query = getQueryOrderByLikes(userLikedCombinations, nodeId, likedCombinations);
                break;

            default:
                query = getQueryOrderByTimestamp(userLikedCombinations, nodeId, likedCombinations);
        }

        query.addValueEventListener(new ValueEventListener() {

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

    public void getUploadedCombinations(final String nodeId, final ArrayList<Combination> uploadedCombinations,
                                        final UploadedCombinationsFragment fragment, final ContentOrder orderCriteria) {

        final DatabaseReference userUploadedCombinations = tableUsers.child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child(Constants.USER_UPLOADED_COMBINATIONS);

        Query query;
        switch(orderCriteria) {
            case MOST_LIKED:
                query = getQueryOrderByLikes(userUploadedCombinations, nodeId, uploadedCombinations);
                break;

            default:
                query = getQueryOrderByTimestamp(userUploadedCombinations, nodeId, uploadedCombinations);
        }

        query.addValueEventListener(new ValueEventListener() {
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
