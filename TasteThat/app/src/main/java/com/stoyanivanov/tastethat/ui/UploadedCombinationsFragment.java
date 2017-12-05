package com.stoyanivanov.tastethat.ui;


import android.content.Context;
import android.os.Bundle;
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
import android.widget.ImageView;

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
    private EditText searchBar;
    private ImageView cancelSearch;
    private ImageView searchIcon;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_base_recyclerview, container, false);

        searchBar = (EditText) view.findViewById(R.id.et_search);
        cancelSearch = (ImageView) view.findViewById(R.id.iv_cancel_search);
        searchIcon = (ImageView) view.findViewById(R.id.iv_search_icon);

        uploadedCombinations = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        RVScrollController scrollController = new RVScrollController();
        scrollController.addControlToBottomNavigation(recyclerView);

        getUploadedCombinations();
        configureSearchBar();

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

    private void configureSearchBar() {
        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
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
                startFilteringContent();
                hideVirtualKeyboard(v);
            }
        });

        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyAdapterOnSearchCancel();
                searchBar.setText("");
            }
        });
    }

    private void hideVirtualKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if(imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void startFilteringContent() {
        adapter.setNewData(uploadedCombinations);
        adapter.filterData(searchBar.getText().toString());
    }

    private void notifyAdapterOnSearchCancel() {
        adapter.setNewData(uploadedCombinations);
    }
}
