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
import com.stoyanivanov.tastethat.RVScrollController;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.MyRecyclerViewAdapter;

import java.util.ArrayList;

import static com.stoyanivanov.tastethat.MainActivity.currUser;


public class CombinationsFragment extends Fragment {

    CustomTextView likeCounter;
    ArrayList<Combination> allCombinations;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference mDatabaseUsers;
    DatabaseReference mDatabaseCombinations;
    DatabaseReference mDatabaseLikes;

    boolean processLike = false;
    boolean isLiked = false;
    FirebaseUser currUser = MainActivity.getCurrentGoogleUser();
    long likes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_combinations, container, false);

        allCombinations = new ArrayList<>();
        likeCounter = (CustomTextView) view.findViewById(R.id.tv_like_counter);

        database = FirebaseDatabase.getInstance();
        mDatabaseCombinations = database.getReference().child("combinations");
        mDatabaseUsers = database.getReference().child("users");
        mDatabaseLikes = database.getReference().child("likes");

        getAllCombinations();

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MyRecyclerViewAdapter(allCombinations, new OnItemClickListener() {
            @Override
            public void onItemClick(final Combination combination, final CustomTextView likeCounter, int position) {
                final String nameOfCombination = combination.getFirstComponent() + combination.getSecondComponent();

                mDatabaseLikes.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        likes = dataSnapshot.child(nameOfCombination).getChildrenCount();

                        long manipulatedLikes = (controlLikesInDB(likes, nameOfCombination));
                        likeCounter.setText(String.valueOf(manipulatedLikes));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        }));

        RVScrollController scrollController = new RVScrollController();
        scrollController.addControlToBottomNavigation(recyclerView);


        return view;
    }

    private void getAllCombinations() {
        mDatabaseCombinations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Combination currCombination = dataSnapshot.getValue(Combination.class);
                    allCombinations.add(currCombination);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("SII", "onCancelled: error");
            }
        });
    }

    public long controlLikesInDB(long likes, String nameOfCombination) {
        if(combinationIsLiked(nameOfCombination)) {
            likes--;
        } else {
            likes++;
        }

        return likes;
    }



    public boolean combinationIsLiked(final String nameOfCombination) {

        processLike = true;

        mDatabaseLikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(processLike) {
                    if (dataSnapshot.child(nameOfCombination).hasChild(currUser.getUid())) {
                        mDatabaseLikes.child(nameOfCombination).child(currUser.getUid()).removeValue();
                        isLiked = true;
                        processLike = false;
                    } else {
                        mDatabaseLikes.child(nameOfCombination).child(currUser.getUid()).setValue(currUser.getEmail());
                        isLiked = false;
                        processLike = false;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return isLiked;
    }
}
