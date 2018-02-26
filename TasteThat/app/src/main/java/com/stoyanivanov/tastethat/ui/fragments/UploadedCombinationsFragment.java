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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UploadedCombinationsFragment extends BaseRecyclerViewFragment {

    @BindView(R.id.rv) RecyclerView recyclerView;
    @BindView(R.id.et_search) EditText searchBar;
    @BindView(R.id.iv_cancel_search) ImageView cancelSearch;
    @BindView(R.id.iv_search_icon) ImageView searchIcon;
    @BindView(R.id.ctv_selected_section_header) CustomTextView selectedSectionHeader;

    private ArrayList<Combination> uploadedCombinations;
    private CombinationsRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateCurrentView(R.layout.fragment_base_recyclerview, inflater, container);

        uploadedCombinations = new ArrayList<>();

        DatabaseProvider.getInstance().getUploadedCombinations(this);

        selectedSectionHeader.setText(R.string.uploads_header);
        configureSearchWidget(searchBar,searchIcon,cancelSearch,selectedSectionHeader);

        return view;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
