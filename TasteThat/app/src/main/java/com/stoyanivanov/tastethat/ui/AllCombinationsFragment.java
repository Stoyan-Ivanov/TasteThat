package com.stoyanivanov.tastethat.ui;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.activities.MainActivity;
import com.stoyanivanov.tastethat.interfaces.OnClickItemLikeListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.view_utils.CustomFilter;
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
    private EditText searchBar;
    private ImageView cancelSearch;
    private ImageView searchIcon;
    private CustomTextView selectedSectionHeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view  = inflater.inflate(R.layout.fragment_base_recyclerview, container, false);

        allCombinations = new ArrayList<>();
        likeCounter = (CustomTextView) view.findViewById(R.id.vh_tv_like_counter);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        searchBar = (EditText) view.findViewById(R.id.et_search);
        cancelSearch = (ImageView) view.findViewById(R.id.iv_cancel_search);
        searchIcon = (ImageView) view.findViewById(R.id.iv_search_icon);
        selectedSectionHeader = (CustomTextView) view.findViewById(R.id.ctv_selected_section_header);

        selectedSectionHeader.setText("All Combinations");

        getAllCombinations();
        instantiateRV();
        configureSearchBar();

        RVScrollController scrollController = new RVScrollController();
        scrollController.addControlToBottomNavigation(recyclerView);

        return view;
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

    private void instantiateRV() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyRecyclerViewAdapter(Constants.RV_ALL_COMBINATIONS, allCombinations, new OnClickItemLikeListener() {
            @Override
            public void onItemClick(final Combination combination, final CustomTextView likeCounter, int position) {
                final String nameOfCombination = combination.getCombinationName();
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

    private void configureSearchBar() {
        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    showVirtualKeyboard();
                } else {
                    hideVirtualKeyboard(v);
                }
            }
        });

        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.d("SII", "onKey: entering");
                    startFilteringContent();
                    return true;
                }
                return false;
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchBar.getVisibility() == View.VISIBLE) {
                    startFilteringContent();
                    hideVirtualKeyboard(v);
                } else {
                    showAppBarSearch();
                }
            }
        });

        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyAdapterOnSearchCancel();
                searchBar.setText("");
                hideVirtualKeyboard(v);
                showAppBarHeader();
            }
        });
    }

    private void startFilteringContent() {
        adapter.setNewData(allCombinations);
        adapter.filterData(searchBar.getText().toString());
    }

    private void notifyAdapterOnSearchCancel() {
        adapter.setNewData(allCombinations);
    }

    private void hideVirtualKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if(imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showVirtualKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    private void showAppBarHeader() {
        searchBar.setVisibility(View.INVISIBLE);
        cancelSearch.setVisibility(View.INVISIBLE);
        selectedSectionHeader.setVisibility(View.VISIBLE);
    }

    private void showAppBarSearch() {
        selectedSectionHeader.setVisibility(View.INVISIBLE);
        searchBar.setVisibility(View.VISIBLE);
        cancelSearch.setVisibility(View.VISIBLE);
        searchBar.requestFocus();
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
