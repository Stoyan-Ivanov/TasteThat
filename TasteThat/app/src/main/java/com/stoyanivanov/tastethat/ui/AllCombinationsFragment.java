package com.stoyanivanov.tastethat.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.activities.MainActivity;
import com.stoyanivanov.tastethat.interfaces.OnClickItemLikeListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.view_utils.controllers.RVScrollController;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.rv_adapters.MyRecyclerViewAdapter;

import java.util.ArrayList;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.*;


public class AllCombinationsFragment extends Fragment {

    private CustomTextView likeCounter;
    private FirebaseUser currUser = MainActivity.getCurrentGoogleUser();
    private MyRecyclerViewAdapter adapter;
    private ArrayList<Combination> allCombinations;
    private Combination currentCombination;
    private RecyclerView recyclerView;
    private boolean processLike = false;
    private boolean isLiked;
    private long likes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_all_combinations, container, false);

        allCombinations = new ArrayList<>();
        likeCounter = (CustomTextView) view.findViewById(R.id.vh_tv_like_counter);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        getAllCombinations();
        setHasOptionsMenu(true);
        instantiateRV();

        RVScrollController scrollController = new RVScrollController();
        scrollController.addControlToBottomNavigation(recyclerView);

        return view;
    }

    private void instantiateRV() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyRecyclerViewAdapter(Constants.RV_ALL_COMBINATIONS, allCombinations, new OnClickItemLikeListener() {
            @Override
            public void onItemClick(final Combination combination, final CustomTextView likeCounter, int position) {
                final String nameOfCombination = combination.getFirstComponent() + combination.getSecondComponent();
                currentCombination = combination;

                tableLikes.addListenerForSingleValueEvent(new ValueEventListener() {
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
        });
        recyclerView.setAdapter(adapter);
    }

    private void getAllCombinations() {
        tableCombinations.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allCombinations.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Combination currCombination = snapshot.getValue(Combination.class);
                    allCombinations.add(currCombination);
                }
                adapter.setNewData(allCombinations);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("SII", "onCancelled: error getAllCombinations");
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setQueryHint("What goes with...");
        //searchView.setIconifiedByDefault(false);
    }

    public long controlLikesInDB(long likes, String nameOfCombination) {
        if(combinationIsLiked(nameOfCombination)) {
            return --likes;
        }

        return ++likes;
    }

    public boolean combinationIsLiked(final String nameOfCombination) {

        processLike = true;
        isLiked = false;

        tableLikes.child(nameOfCombination).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (processLike) {
                        if (dataSnapshot.hasChild(currUser.getUid())) {
                            tableLikes.child(nameOfCombination)
                                    .child(currUser.getUid()).removeValue();
                            tableUsers.child(currUser.getUid()).
                                    child(Constants.USER_LIKED_COMBINATIONS).child(nameOfCombination).removeValue();
                            isLiked = true;
                            processLike = false;
                        } else {
                            tableLikes.child(nameOfCombination)
                                    .child(currUser.getUid()).setValue(currUser.getEmail());
                            tableUsers.child(currUser.getUid()).
                                    child(Constants.USER_LIKED_COMBINATIONS).child(nameOfCombination).setValue(currentCombination);
                            isLiked = false;
                            processLike = false;
                        }
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("SII", "OnCancel: combinationIsLiked");
            }
        });

        return isLiked;
    }
}
