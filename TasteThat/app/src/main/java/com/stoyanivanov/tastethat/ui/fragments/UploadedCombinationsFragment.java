package com.stoyanivanov.tastethat.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.ui.activities.MyProfileActivity;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.db.DatabaseProvider;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.OnClickViewHolder;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.CombinationsRecyclerViewAdapter;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.decoration.SpacesItemDecoration;
import com.stoyanivanov.tastethat.view_utils.views_behaviour.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;

public class UploadedCombinationsFragment extends BaseRecyclerViewFragment {

    private ArrayList<Combination> mUploadedCombinations;

    @Override
    public void onResume() {
        super.onResume();
        instantiateRecyclerView();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_base_recyclerview, inflater, container);

        selectedSectionHeader.setText(R.string.uploads_header);
        optionsMenu.setImageAlpha(0);

        startLoadingCombinations();
        return view;
    }

    @Override
    public void startLoadingCombinations() {
        if(mUploadedCombinations == null) {
            mUploadedCombinations = new ArrayList<>();
        } else {
            mUploadedCombinations.clear();
        }
        loadCombinations(null);
    }

    private void loadCombinations(String nodeId) {
        DatabaseProvider.getInstance().getUploadedCombinations(nodeId, mUploadedCombinations,
                this, super.currORDER);
    }

    private void loadMoreCombinations(){
        String nodeId = mUploadedCombinations.get(mUploadedCombinations.size() - 1).getCombinationKey();
        loadCombinations(nodeId);
    }

    public void onDataGathered(ArrayList<Combination> combinations) {
        if(mAdapter == null) {
            mUploadedCombinations = combinations;
            instantiateRecyclerView();
        } else {
            mAdapter.setNewData(combinations);
        }
    }

    @Override
    protected void instantiateRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new CombinationsRecyclerViewAdapter(Constants.RV_UPLOADED_COMBINATIONS, mUploadedCombinations, new OnClickViewHolder() {
            @Override
            public void onRateButtonClicked(Combination combination) {
                ((MyProfileActivity) getActivity())
                        .replaceFragment(RateCombinationFragment.newInstance(combination));
            }

            @Override
            public void onItemClick(Combination combination) {
                ((MyProfileActivity) getActivity())
                        .replaceFragment(CombinationDetailsFragment
                            .newInstance(MyProfileActivity.class.getSimpleName(), combination));
            }
        });

        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(16, SpacesItemDecoration.VERTICAL));

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                loadMoreCombinations();
            }
        });
    }

    @Override
    protected void startFilteringContent() {
        mAdapter.setNewData(mUploadedCombinations);
        mAdapter.filterData(searchBar.getText().toString());
    }

    @Override
    protected void notifyAdapterOnSearchCancel() {
        mAdapter.setNewData(mUploadedCombinations);
    }
}
