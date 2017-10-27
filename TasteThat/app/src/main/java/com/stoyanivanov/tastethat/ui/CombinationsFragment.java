package com.stoyanivanov.tastethat.ui;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.MainActivity;
import com.stoyanivanov.tastethat.OnItemClickListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.MyRecyclerViewAdapter;

import java.util.ArrayList;


public class CombinationsFragment extends Fragment {

    CustomTextView likeCounter;
    ArrayList<Combination> allCombinations;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference dbRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_combinations, container, false);

        allCombinations = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        dbRef.child("combinations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Combination currCombination = dataSnapshot.getValue(Combination.class);
                    allCombinations.add(currCombination);
                    Log.d("SII", "combination" + currCombination);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("SII", "onCancelled: error");
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MyRecyclerViewAdapter(allCombinations, new OnItemClickListener() {
            @Override
            public void onItemClick(Combination combination, final CustomTextView likeCounter) {
                final String nameOfCombination = combination.getFirstComponent() + combination.getSecondComponent();

                dbRef.child("combinations").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        int likes = dataSnapshot.child(nameOfCombination).child("likes").getValue(Integer.class);

                        dbRef.child("combinations").child(nameOfCombination).child("likes").setValue(++likes);
                        likeCounter.setText(String.valueOf(likes));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FirebaseUser currUser = MainActivity.getCurrentGoogleUser();
                dbRef.child("users").child(currUser.getUid()).child("liked").push().setValue(combination);
            }
        }));

        addControlToBottomNavigation();

        return view;
    }

    private void addControlToBottomNavigation() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                BottomNavigationView bottomNavigationView = ((MainActivity) getActivity()).getBottomNavigation();

                if (dy > 0 && bottomNavigationView.isShown()) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else if (dy < 0 || dy == 0) {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
