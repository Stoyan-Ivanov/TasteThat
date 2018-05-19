package com.stoyanivanov.tastethat.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanivanov.tastethat.ui.activities.MyProfileActivity;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.db.DatabaseProvider;
import com.stoyanivanov.tastethat.ui.activities.main_activity.MainActivity;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.OnClickViewHolder;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.CombinationsRecyclerViewAdapter;
import com.stoyanivanov.tastethat.view_utils.views_behaviour.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;

public class LikedCombinationsFragment extends BaseRecyclerViewFragment {

    private ArrayList<Combination> likedCombinations;
    private CombinationsRecyclerViewAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        instantiateRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_base_recyclerview, inflater, container);

        selectedSectionHeader.setText(R.string.liked_header);
        optionsMenu.setImageAlpha(0);

        startLoadingCombinations();
        return view;
    }

    @Override
    public void startLoadingCombinations() {
        if(likedCombinations == null) {
            likedCombinations = new ArrayList<>();
        } else {
            likedCombinations.clear();
        }
        loadCombinations(null);
    }

    private void loadCombinations(String nodeId) {
        DatabaseProvider.getInstance().getLikedCombinations(nodeId, likedCombinations,
                this, super.currORDER);
    }

    private void loadMoreCombinations(){
        String nodeId = likedCombinations.get(likedCombinations.size() - 1).getCombinationKey();
        loadCombinations(nodeId);
    }

    public void onDataGathered(ArrayList<Combination> combinations) {
        if(adapter == null) {
            likedCombinations = combinations;
            instantiateRecyclerView();
        } else {
            adapter.setNewData(combinations);
        }
    }

    @Override
    protected void instantiateRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CombinationsRecyclerViewAdapter(Constants.RV_LIKED_COMBINATIONS, likedCombinations, new OnClickViewHolder() {
            @Override
            public void onRateButtonClicked(Combination combination) {
                ((MainActivity) getActivity())
                        .replaceFragment(RateCombinationFragment.newInstance(combination));
            }

            @Override
            public void onItemClick(Combination combination) {
                ((MyProfileActivity) getActivity())
                        .replaceFragment(CombinationDetailsFragment.newInstance(MyProfileActivity.class.getSimpleName(), combination));
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                loadMoreCombinations();
            }
        });
    }

    @Override
    public void startFilteringContent() {
        adapter.setNewData(likedCombinations);
        adapter.filterData(searchBar.getText().toString());
    }

    @Override
    public void notifyAdapterOnSearchCancel() {
        adapter.setNewData(likedCombinations);
    }
}
