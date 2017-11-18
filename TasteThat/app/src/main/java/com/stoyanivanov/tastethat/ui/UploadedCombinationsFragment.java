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
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.activities.UserProfileActivity;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.interfaces.OnClickItemLikeListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.view_utils.controllers.RVScrollController;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.rv_adapters.MyRecyclerViewAdapter;

import java.util.ArrayList;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.tableUsers;

public class UploadedCombinationsFragment extends Fragment {

    ArrayList<Combination> uploadedCombinations;
    FirebaseUser currUser = UserProfileActivity.getCurrentGoogleUser();
    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_uploaded_combinations, container, false);

        uploadedCombinations = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_uploaded);
        RVScrollController scrollController = new RVScrollController();
        scrollController.addControlToBottomNavigation(recyclerView);

        getUploadedCombinations();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyRecyclerViewAdapter(Constants.RV_UPLOADED_COMBINATIONS, uploadedCombinations, new OnClickItemLikeListener() {
            @Override
            public void onItemClick(Combination combination, CustomTextView likeCounter, int position) {
            }
        });

        recyclerView.setAdapter(adapter);
        return view;
    }

    private void getUploadedCombinations() {
        tableUsers.child(currUser.getUid())
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                uploadedCombinations.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Combination currCombination = dataSnapshot.getValue(Combination.class);
                    uploadedCombinations.add(currCombination);
                }
                adapter.setNewData(uploadedCombinations);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("SII", "onCancelled: error");
            }
        });
    }


}
