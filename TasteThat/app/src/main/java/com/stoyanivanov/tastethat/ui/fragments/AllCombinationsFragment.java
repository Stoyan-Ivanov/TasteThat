package com.stoyanivanov.tastethat.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.ui.activities.BaseBottomNavigationActivity;
import com.stoyanivanov.tastethat.ui.activities.MainActivity;
import com.stoyanivanov.tastethat.db.DatabaseProvider;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.OnClickViewHolder;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.view_utils.views_behaviour.EndlessRecyclerOnScrollListener;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.CombinationsRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.*;


public class AllCombinationsFragment extends BaseRecyclerViewFragment {

    @BindView(R.id.et_search) EditText searchBar;
    @BindView(R.id.iv_cancel_search) ImageView cancelSearch;
    @BindView(R.id.iv_search_icon) ImageView searchIcon;
    @BindView(R.id.ctv_selected_section_header) CustomTextView selectedSectionHeader;
    @BindView(R.id.rv) RecyclerView recyclerView;
    @BindView(R.id.fab_add_combination) FloatingActionButton fabAddCombination;

    private CombinationsRecyclerViewAdapter adapter;
    private ArrayList<Combination> allCombinations;
    private Combination currentCombination;
    private boolean processLike = false;
    private boolean isLiked;
    private long likes;

    @OnClick(R.id.fab_add_combination)
        void inflateNewAddCombinationFragment() {
        ((MainActivity) getActivity()).replaceFragment(AddCombinationFragment.newInstance());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_base_recyclerview, inflater, container);

        allCombinations = new ArrayList<>();

        fabAddCombination.setVisibility(View.VISIBLE);
        selectedSectionHeader.setText(R.string.all_combinations_header);
        setupOptionsMenu(view);
        configureSearchWidget(searchBar,searchIcon,cancelSearch,selectedSectionHeader);

        loadCombinations(null);


        return view;
    }

    private void loadCombinations(String nodeId) {
        DatabaseProvider.getInstance().getCombinations(nodeId, allCombinations,
                this, BaseRecyclerViewFragment.ORDER_TIMESTAMP);
    }

    private void loadMoreCombinations(){
        String nodeId = allCombinations.get(allCombinations.size() - 1).getCombinationKey();
        loadCombinations(nodeId);
    }

    public void onDataGathered(ArrayList<Combination> combinations) {
            if(adapter == null) {
                allCombinations = combinations;
                instantiateRV();
            } else {
                adapter.setNewData(combinations);
            }
    }

    @Override
    protected void instantiateRV() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new CombinationsRecyclerViewAdapter(Constants.RV_ALL_COMBINATIONS, allCombinations, new OnClickViewHolder() {
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

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager, fabAddCombination) {
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
