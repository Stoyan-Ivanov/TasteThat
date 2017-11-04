package com.stoyanivanov.tastethat.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.Constants;
import com.stoyanivanov.tastethat.MainActivity;
import com.stoyanivanov.tastethat.interfaces.OnItemClickListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.view_utils.RVScrollController;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.MyRecyclerViewAdapter;

import java.util.ArrayList;

public class UploadedCombinationsFragment extends Fragment {

    ArrayList<Combination> uploadedCombinations;
    FirebaseDatabase database;
    DatabaseReference mDatabaseUsers;
    FirebaseUser currUser = MainActivity.getCurrentGoogleUser();
    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_uploaded_combinations, container, false);

        database = FirebaseDatabase.getInstance();
        mDatabaseUsers = database.getReference().child(Constants.USER_DATABASE).child(currUser.getUid());
        uploadedCombinations = new ArrayList<>();

        getUploadedCombinations();

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_uploaded);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyRecyclerViewAdapter(uploadedCombinations, new OnItemClickListener() {
            @Override
            public void onItemClick(Combination combination, CustomTextView likeCounter, int position) {
            }
        });

        RVScrollController scrollController = new RVScrollController();
        scrollController.addControlToBottomNavigation(recyclerView);

        recyclerView.setAdapter(adapter);
        return view;
    }

    private void inflatePopUpMenu(final int position) {

    }

    private void removeCombination(int position) {

    }

    private void getUploadedCombinations() {
        mDatabaseUsers.child(Constants.USER_UPLOADED_COMBINATIONS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Combination currCombination = dataSnapshot.getValue(Combination.class);
                    uploadedCombinations.add(currCombination);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("SII", "onCancelled: error");
            }
        });
    }


}
