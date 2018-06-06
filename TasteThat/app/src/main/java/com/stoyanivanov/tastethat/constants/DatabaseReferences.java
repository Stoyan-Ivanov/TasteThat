package com.stoyanivanov.tastethat.constants;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by stoyan-ivanov on 08.11.17.
 */

public class DatabaseReferences {
    public final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference nodeUsers = database.getReference().child(Constants.USER_NODE);
    public static DatabaseReference nodeCombinations = database.getReference().child(Constants.COMBINATIONS_NODE);
    public static DatabaseReference nodeCombinationRating = database.getReference().child(Constants.COMBINATION_RATINGS);
    public static DatabaseReference nodeReports = database.getReference().child(Constants.REPORTS_NODE);

}
