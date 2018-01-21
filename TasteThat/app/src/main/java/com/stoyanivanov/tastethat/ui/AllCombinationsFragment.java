package com.stoyanivanov.tastethat.ui;

import android.graphics.Path;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.activities.MainActivity;
import com.stoyanivanov.tastethat.constants.PageHeaders;
import com.stoyanivanov.tastethat.interfaces.OnClickViewHolder;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.view_utils.EndlessRecyclerOnScrollListener;
import com.stoyanivanov.tastethat.models.Combination;
import com.stoyanivanov.tastethat.view_utils.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.rv_adapters.MyRecyclerViewAdapter;

import java.util.ArrayList;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.*;


public class AllCombinationsFragment extends BaseRecyclerViewFragment {

    private CustomTextView likeCounter;
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

    private static final int TOTAL_COMBINATIONS_FOR_ONE_LOAD = 3;

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

        selectedSectionHeader.setText(PageHeaders.allCombinationsFragment);
        configureSearchWidget(searchBar,searchIcon,cancelSearch,selectedSectionHeader);

        loadCombinations(null);
        instantiateRV();

        return view;
    }

    private void loadCombinations(String nodeId) {
        Query query;

        if(nodeId == null) {
            query = tableCombinations
                    .limitToFirst(TOTAL_COMBINATIONS_FOR_ONE_LOAD)
                    .orderByChild("timestamp");
        } else {
            query = tableCombinations
                    .limitToFirst(TOTAL_COMBINATIONS_FOR_ONE_LOAD)
                    .orderByChild("timestamp")
                    .startAt((long)allCombinations.get(allCombinations.size() - 1).getTimestamp(),nodeId +1);
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChildren()){
                    Toast.makeText(getActivity(), "No more combinations", Toast.LENGTH_SHORT).show();
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Combination currCombination = snapshot.getValue(Combination.class);
                    allCombinations.add(currCombination);
                }
                adapter.setNewData(allCombinations);
            }

            @Override public void onCancelled(DatabaseError databaseError) {}});
    }

    private void loadMoreCombinations(){
        loadCombinations(allCombinations.get(allCombinations.size() - 1).getCombinationKey());
    }

    @Override
    protected void instantiateRV() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MyRecyclerViewAdapter(Constants.RV_ALL_COMBINATIONS, allCombinations, new OnClickViewHolder() {
            @Override
            public void onItemClick(Combination combination, final CustomTextView likeCounter, int position) {
                final String combinationKey = combination.getCombinationKey();
                currentCombination = combination;

                tableLikes.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        likes = dataSnapshot.child(combinationKey).getChildrenCount();

                        long manipulatedLikes = (controlLikesInDB(likes, combinationKey));
                        likeCounter.setText(String.valueOf(manipulatedLikes));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }

            @Override
            public void onItemLongClick(Combination combination) {
                ((MainActivity) getActivity())
                        .replaceFragment(CombinationDetailsFragment.newInstance(MainActivity.class.getSimpleName(), combination));
            }
        });
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                loadMoreCombinations();
            }
        });
    }

    @Override
    public void startFilteringContent() {
        adapter.setNewData(allCombinations);
        adapter.filterData(searchBar.getText().toString());
    }

    @Override
    public void notifyAdapterOnSearchCancel() {
        adapter.setNewData(allCombinations);
    }

    public long controlLikesInDB(long likes, String nameOfCombination) {
        if(combinationIsLiked(nameOfCombination)) {
            return --likes;
        }

        return ++likes;
    }

    public boolean combinationIsLiked(final String combinationKey) {

        processLike = true;
        isLiked = false;

        tableLikes.child(combinationKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (processLike) {
                        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

                        if (dataSnapshot.hasChild(currUser.getUid())) {
                            tableLikes.child(combinationKey)
                                    .child(currUser.getUid()).removeValue();

                            tableUsers.child(currUser.getUid())
                                    .child(Constants.USER_LIKED_COMBINATIONS)
                                    .child(combinationKey)
                                    .removeValue();

                            isLiked = true;
                            processLike = false;

                        } else {
                            tableLikes.child(combinationKey)
                                    .child(currUser.getUid()).setValue(currUser.getEmail());

                            tableUsers.child(currUser.getUid())
                                    .child(Constants.USER_LIKED_COMBINATIONS)
                                    .child(combinationKey)
                                    .setValue(currentCombination);

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
