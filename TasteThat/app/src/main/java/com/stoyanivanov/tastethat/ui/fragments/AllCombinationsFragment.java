package com.stoyanivanov.tastethat.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.ui.activities.main_activity.MainActivity;
import com.stoyanivanov.tastethat.db.DatabaseProvider;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.OnClickViewHolder;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.decoration.SpacesItemDecoration;
import com.stoyanivanov.tastethat.view_utils.views_behaviour.EndlessRecyclerOnScrollListener;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.CombinationsRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


public class AllCombinationsFragment extends BaseRecyclerViewFragment {
    @BindView(R.id.fab_add_combination) FloatingActionButton fabAddCombination;

    private ArrayList<Combination> mAllCombinations;
    private boolean mAddDecoration = true;


    @OnClick(R.id.fab_add_combination)
        void inflateNewAddCombinationFragment() {
        ((MainActivity) getActivity()).replaceFragment(AddCombinationFragment.newInstance());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_base_recyclerview, inflater, container);

        fabAddCombination.setVisibility(View.VISIBLE);
        selectedSectionHeader.setText(R.string.all_combinations_header);
        setupOptionsMenu(view);

        startLoadingCombinations();

        return view;
    }

    @Override
    public void startLoadingCombinations() {
        if(mAllCombinations == null) {
            mAllCombinations = new ArrayList<>();
        } else {
            mAllCombinations.clear();
        }
        isLoading = true;
        loadCombinations(null);
    }

    public void loadCombinations(String nodeId) {
        DatabaseProvider.getInstance().getAllCombinations(nodeId, mAllCombinations,
                                                this, super.currORDER);
    }

    private void loadMoreCombinations(){
        if((mAllCombinations.size() - 1) > 0) {
            String nodeId = mAllCombinations.get(mAllCombinations.size() - 1).getCombinationKey();
            loadCombinations(nodeId);
        }
    }

    public void onDataGathered(ArrayList<Combination> combinations) {
//        if(mAdapter == null) {
            mAllCombinations = combinations;
            instantiateRecyclerView();
//        } else {
//            mAdapter.setNewData(combinations);
//        }
        isLoading = false;
    }

    @Override
    protected void instantiateRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new CombinationsRecyclerViewAdapter(Constants.RV_ALL_COMBINATIONS, mAllCombinations, new OnClickViewHolder() {
            @Override
            public void onRateButtonClicked(Combination combination) {
                ((MainActivity) getActivity())
                        .replaceFragment(RateCombinationFragment.newInstance(combination));
            }

            @Override
            public void onItemClick(Combination combination) {
                ((MainActivity) getActivity())
                        .replaceFragment(CombinationDetailsFragment.newInstance(MainActivity.class.getSimpleName(), combination));
            }
        });
        recyclerView.setAdapter(mAdapter);

        if(mAddDecoration) {
            recyclerView.addItemDecoration(new SpacesItemDecoration(16, SpacesItemDecoration.VERTICAL));
            mAddDecoration = false;
        }

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager, fabAddCombination) {
            @Override
            public void onLoadMore() {
                loadMoreCombinations();
            }
        });
    }

    @Override
    public void startFilteringContent() {
        mAdapter.setNewData(mAllCombinations);
        mAdapter.filterData(searchBar.getText().toString());
    }

    @Override
    public void notifyAdapterOnSearchCancel() {
        mAdapter.setNewData(mAllCombinations);
    }
}
