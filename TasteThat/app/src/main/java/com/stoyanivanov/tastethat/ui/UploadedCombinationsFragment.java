package com.stoyanivanov.tastethat.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.MainActivity;
import com.stoyanivanov.tastethat.OnItemClickListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.view_utils.RVScrollController;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.MyRecyclerViewAdapter;

import java.util.ArrayList;

public class UploadedCombinationsFragment extends Fragment {

    ArrayList<Combination> uploadedCombinations;
    FirebaseDatabase database;
    DatabaseReference mDatabaseUsers ;
    FirebaseUser currUser = MainActivity.getCurrentGoogleUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_uploaded_combinations, container, false);

        database = FirebaseDatabase.getInstance();
        mDatabaseUsers = database.getReference().child("users").child(currUser.getUid());
        uploadedCombinations = new ArrayList<>();

        getUploadedCombinations();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_uploaded);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MyRecyclerViewAdapter(uploadedCombinations, new OnItemClickListener() {
            @Override
            public void onItemClick(Combination combination, CustomTextView likeCounter, int position) {
            }
        }));

        RVScrollController scrollController = new RVScrollController();
        scrollController.addControlToBottomNavigation(recyclerView);

        return view;
    }

    private void getUploadedCombinations() {
        mDatabaseUsers.child("uploadedCombinations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Combination currCombination = dataSnapshot.getValue(Combination.class);
                    uploadedCombinations.add(currCombination);
                }
                Log.d("SII", uploadedCombinations.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("SII", "onCancelled: error");
            }
        });
    }


}
