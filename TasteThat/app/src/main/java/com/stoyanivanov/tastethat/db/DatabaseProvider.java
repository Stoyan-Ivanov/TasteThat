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
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.ui.fragments.AllCombinationsFragment;
import com.stoyanivanov.tastethat.ui.fragments.RatedCombinationsFragment;
import com.stoyanivanov.tastethat.ui.fragments.UploadedCombinationsFragment;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.CombinationsViewHolder;

import java.util.ArrayList;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.tableCombinationRating;
import static com.stoyanivanov.tastethat.constants.DatabaseReferences.tableCombinations;
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
                        saveCombinationToMyUploads(newCombination);
                        timestampReference.setValue(negativeTimestamp);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("SII", databaseError.getMessage());
            }
        });

        TasteThatApplication.showToast(TasteThatApplication.getStringFromId((R.string.toast_successfull_adding)));
    }



    private void saveCombinationToMyUploads(Combination newCombination) {
        DatabaseReferences.tableUsers
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .child(newCombination.getCombinationKey())
                .setValue(newCombination);
    }

    public void saveRatingForCombination(Combination combination, float rating) {
        if(combination != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReferences.tableCombinationRating
                    .child(combination.getCombinationKey())
                    .child("users")
                    .child(userId)
                    .setValue(rating);
        }
    }

    public void getAllCombinations(final String nodeId, final ArrayList<Combination> combinations,
                                   final AllCombinationsFragment fragment, final ContentOrder orderCriteria) {

            Query query;
            switch(orderCriteria) {
                case HIGHEST_RATING:
                    query = getQueryOrderByRating(tableCombinations, nodeId, combinations);
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

    private Query getQueryOrderByRating(DatabaseReference tableReference,
                                        String nodeId, final ArrayList<Combination> combinations) {
        Query query;

        if(nodeId == null) {
            query = tableReference
                    .limitToFirst(TOTAL_COMBINATIONS_FOR_ONE_LOAD)
                    .orderByChild("negativeRating");
        } else {
            query = tableReference
                    .limitToFirst(TOTAL_COMBINATIONS_FOR_ONE_LOAD)
                    .orderByChild("negativeRating")
                    .startAt((long)combinations.get(combinations.size() - 1).getRating(),nodeId +1);
        }

        return query;
    }

    public void getCombinationRating(Combination combination, final CombinationsViewHolder viewHolder) {

        tableCombinationRating.child(combination.getCombinationKey())
                .child("rating")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long rating = (Long)dataSnapshot.getValue();
                        if (rating != null) {
                            viewHolder.setRating(rating.intValue());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    public void getRatedCombinations(final String nodeId, final ArrayList<Combination> ratedCombinations,
                                     final RatedCombinationsFragment fragment, final ContentOrder orderCriteria) {

        final DatabaseReference userLikedCombinations = tableUsers.child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).child(Constants.USER_RATED_COMBINATIONS);

        Query query;
        switch(orderCriteria) {
            case HIGHEST_RATING:
                query = getQueryOrderByRating(userLikedCombinations, nodeId, ratedCombinations);
                break;

            default:
                query = getQueryOrderByTimestamp(userLikedCombinations, nodeId, ratedCombinations);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Combination currCombination = dataSnapshot.getValue(Combination.class);
                    ratedCombinations.add(currCombination);
                }

                fragment.onDataGathered(ratedCombinations);
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
            case HIGHEST_RATING:
                query = getQueryOrderByRating(userUploadedCombinations, nodeId, uploadedCombinations);
                break;

            default:
                query = getQueryOrderByTimestamp(userUploadedCombinations, nodeId, uploadedCombinations);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
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

    public void saveCombinationToUserRated(Combination combination) {
        tableUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Constants.USER_RATED_COMBINATIONS)
                .child(combination.getCombinationKey())
                .setValue(combination);
    }

    public void deleteCombination(String combinationKey) {
        tableCombinations.child(combinationKey).removeValue();

        tableUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .child(combinationKey).removeValue();

        tableCombinationRating
                .child(combinationKey)
                .child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                           String userId = postSnapshot.getKey();
                           Log.d("SII", "onDataChange: " + userId);
                           tableUsers.child(userId).child(Constants.USER_RATED_COMBINATIONS).child(combinationKey).removeValue();
                       }
                        tableCombinationRating.child(combinationKey).removeValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("SII", "onCancelled: " + databaseError.getMessage());
                    }
                });
    }
}
