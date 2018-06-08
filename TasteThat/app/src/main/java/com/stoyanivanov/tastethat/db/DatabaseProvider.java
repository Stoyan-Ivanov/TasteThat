package com.stoyanivanov.tastethat.db;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.constants.ContentOrder;
import com.stoyanivanov.tastethat.constants.DatabaseReferences;
import com.stoyanivanov.tastethat.db.models.Achievement;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.ui.fragments.AllCombinationsFragment;
import com.stoyanivanov.tastethat.ui.fragments.MyProfileFragment;
import com.stoyanivanov.tastethat.ui.fragments.RatedCombinationsFragment;
import com.stoyanivanov.tastethat.ui.fragments.UploadedCombinationsFragment;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.CombinationsViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.nodeCombinationRating;
import static com.stoyanivanov.tastethat.constants.DatabaseReferences.nodeCombinations;
import static com.stoyanivanov.tastethat.constants.DatabaseReferences.nodeReports;
import static com.stoyanivanov.tastethat.constants.DatabaseReferences.nodeUsers;

/**
 * Created by Stoyan on 2.2.2018 г..
 */

public class DatabaseProvider {
    private static DatabaseProvider sInstance;
    private final int TOTAL_COMBINATIONS_FOR_ONE_LOAD = 5;
    private String mUserId;

    public static DatabaseProvider getInstance() {
        if(sInstance == null) {
            sInstance = new DatabaseProvider();
        }
        return sInstance;
    }

    private DatabaseProvider() {
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void saveCombination(final Combination newCombination) {
        String combinationKey = newCombination.getCombinationKey();

        DatabaseReferences.nodeCombinations.child(combinationKey)
                .setValue(newCombination);

        // create negative timestamp for sorting purpose
        final DatabaseReference timestampReference = DatabaseReferences.nodeCombinations
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
        DatabaseReferences.nodeUsers
                .child(mUserId)
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .child(newCombination.getCombinationKey())
                .setValue(newCombination);
    }

    public void saveRatingForCombination(Combination combination, float rating) {
        if(combination != null) {
            DatabaseReferences.nodeCombinationRating
                    .child(combination.getCombinationKey())
                    .child("users")
                    .child(mUserId)
                    .setValue(rating);
        }
    }

    public void getAllCombinations(final String nodeId, final ArrayList<Combination> combinations,
                                   final AllCombinationsFragment fragment, final ContentOrder orderCriteria) {
        Log.d("SII", "getAllCombinations: " + orderCriteria.toString());
            Query query;
            switch(orderCriteria) {
                case HIGHEST_RATING:
                    query = getQueryOrderByRating(nodeCombinations, nodeId, combinations);
                    break;

                default: query = getQueryOrderByTimestamp(nodeCombinations, nodeId, combinations);
            }

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChildren()){
                        TasteThatApplication.showToast("No more combinations");
                    } else {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Combination currCombination = snapshot.getValue(Combination.class);
                            combinations.add(currCombination);
                        }
                        fragment.onDataGathered(combinations);
                    }
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

        nodeCombinationRating.child(combination.getCombinationKey())
                .child("rating")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() != null) {
                            double rating = dataSnapshot.getValue(Double.class);
                            viewHolder.setRating(rating);
                        } else {
                            viewHolder.setRating(0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
    }

    public void getAchievements(MyProfileFragment fragment) {
        ArrayList<Achievement> mAchievements = new ArrayList<>();
        nodeUsers.child(mUserId)
                .child(Constants.USER_ACHIEVEMENTS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Achievement currAchievement = snapshot.getValue(Achievement.class);
                            mAchievements.add(currAchievement);
                        }
                       fragment.onAchievementsGathered(mAchievements);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
    }

    public void getRatedCombinations(final String nodeId, final ArrayList<Combination> ratedCombinations,
                                     final RatedCombinationsFragment fragment, final ContentOrder orderCriteria) {

        final DatabaseReference userLikedCombinations = nodeUsers.child(mUserId).child(Constants.USER_RATED_COMBINATIONS);

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

        final DatabaseReference userUploadedCombinations = nodeUsers.child(mUserId).child(Constants.USER_UPLOADED_COMBINATIONS);

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
        nodeUsers.child(mUserId)
                .child(Constants.USER_RATED_COMBINATIONS)
                .child(combination.getCombinationKey())
                .setValue(combination);
    }

    public void deleteCombination(String combinationKey) {
        nodeCombinations.child(combinationKey).removeValue();

        nodeUsers.child(mUserId)
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .child(combinationKey).removeValue();

        nodeCombinationRating
                .child(combinationKey)
                .child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                           String userId = postSnapshot.getKey();
                           Log.d("SII", "onDataChange: " + userId);
                           nodeUsers.child(userId).child(Constants.USER_RATED_COMBINATIONS).child(combinationKey).removeValue();
                       }
                        nodeCombinationRating.child(combinationKey).removeValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("SII", "onCancelled: " + databaseError.getMessage());
                    }
                });
    }

    public void submitReport(String combinationKey) {
       nodeReports.child(combinationKey).child(mUserId).setValue(ServerValue.TIMESTAMP);
    }

    public void updateCombinationDescription(Combination currCombination) {

        nodeUsers.child(mUserId)
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .child(currCombination.getCombinationKey())
                .child("description")
                .setValue(currCombination.getDescription());
    }
}
