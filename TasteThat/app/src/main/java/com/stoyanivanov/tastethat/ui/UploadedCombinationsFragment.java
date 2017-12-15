package com.stoyanivanov.tastethat.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.activities.UserProfileActivity;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.interfaces.OnClickItemLikeListener;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.controllers.RVScrollController;
import com.stoyanivanov.tastethat.view_utils.rv_adapters.MyRecyclerViewAdapter;

import java.util.ArrayList;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.tableUsers;

public class UploadedCombinationsFragment extends BaseRecyclerViewFragment {

    private ArrayList<Combination> uploadedCombinations;
    private FirebaseUser currUser = UserProfileActivity.getCurrentFirebaseUser();
    private MyRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private EditText searchBar;
    private ImageView cancelSearch;
    private ImageView searchIcon;
    private CustomTextView selectedSectionHeader;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_base_recyclerview, container, false);

        uploadedCombinations = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        searchBar = (EditText) view.findViewById(R.id.et_search);
        cancelSearch = (ImageView) view.findViewById(R.id.iv_cancel_search);
        searchIcon = (ImageView) view.findViewById(R.id.iv_search_icon);
        selectedSectionHeader = (CustomTextView) view.findViewById(R.id.ctv_selected_section_header);

        selectedSectionHeader.setText("Uploaded Combinations");
        configureSearchWidget(searchBar,searchIcon,cancelSearch,selectedSectionHeader);

        getUploadedCombinations();
        instantiateRV();

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

    @Override
    protected void instantiateRV() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyRecyclerViewAdapter(Constants.RV_UPLOADED_COMBINATIONS, uploadedCombinations, new OnClickItemLikeListener() {
            @Override
            public void onItemClick(Combination combination, CustomTextView likeCounter, int position) {
            }
        });

        recyclerView.setAdapter(adapter);

        RVScrollController scrollController = new RVScrollController();
        scrollController.addControlToBottomNavigation(recyclerView);
    }

    @Override
    protected void startFilteringContent() {
        adapter.setNewData(uploadedCombinations);
        adapter.filterData(searchBar.getText().toString());
    }

    @Override
    protected void notifyAdapterOnSearchCancel() {
        adapter.setNewData(uploadedCombinations);
    }
}
