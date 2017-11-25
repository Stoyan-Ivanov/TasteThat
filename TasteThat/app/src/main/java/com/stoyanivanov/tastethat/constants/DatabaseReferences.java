package com.stoyanivanov.tastethat.constants;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by stoyan-ivanov on 08.11.17.
 */

public class DatabaseReferences {
    public final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static  DatabaseReference tableLikes = database.getReference().child(Constants.LIKES_TABLE);
    public static DatabaseReference tableUsers = database.getReference().child(Constants.USER_TABLE);
    public static DatabaseReference tableCombinations = database.getReference().child(Constants.COMBINATIONS_TABLE);
}
