package com.stoyanivanov.tastethat.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.ui.activities.MyProfileActivity;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.db.DatabaseProvider;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.OnClickViewHolder;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.controllers.RVScrollController;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.CombinationsRecyclerViewAdapter;
import com.stoyanivanov.tastethat.view_utils.views_behaviour.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UploadedCombinationsFragment extends BaseRecyclerViewFragment {

    private ArrayList<Combination> uploadedCombinations;
    private CombinationsRecyclerViewAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        instantiateRV();
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
        if(uploadedCombinations == null) {
            uploadedCombinations = new ArrayList<>();
        } else {
            uploadedCombinations.clear();
        }
        loadCombinations(null);
    }

    private void loadCombinations(String nodeId) {
        DatabaseProvider.getInstance().getUploadedCombinations(nodeId, uploadedCombinations,
                this, super.currORDER);
    }

    private void loadMoreCombinations(){
        String nodeId = uploadedCombinations.get(uploadedCombinations.size() - 1).getCombinationKey();
        loadCombinations(nodeId);
    }

    public void onDataGathered(ArrayList<Combination> combinations) {
        if(adapter == null) {
            uploadedCombinations = combinations;
            instantiateRV();
        } else {
            adapter.setNewData(combinations);
        }
    }

    @Override
    protected void instantiateRV() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CombinationsRecyclerViewAdapter(Constants.RV_UPLOADED_COMBINATIONS, uploadedCombinations, new OnClickViewHolder() {
            @Override
            public void onItemClick(Combination combination, CustomTextView likeCounter, int position) {
            }

            @Override
            public void onItemLongClick(Combination combination) {
                ((MyProfileActivity) getActivity())
                        .replaceFragment(CombinationDetailsFragment
                            .newInstance(MyProfileActivity.class.getSimpleName(), combination));
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
    protected void startFilteringContent() {
        adapter.setNewData(uploadedCombinations);
        adapter.filterData(searchBar.getText().toString());
    }

    @Override
    protected void notifyAdapterOnSearchCancel() {
        adapter.setNewData(uploadedCombinations);
    }
}
