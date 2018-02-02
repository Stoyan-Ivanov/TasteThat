package com.stoyanivanov.tastethat.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.stoyanivanov.tastethat.ui.activities.MyProfileActivity;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.db.DatabaseProvider;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.OnClickViewHolder;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.view_utils.controllers.RVScrollController;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.CombinationsRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LikedCombinationsFragment extends BaseRecyclerViewFragment {

    @BindView(R.id.rv) RecyclerView recyclerView;
    @BindView(R.id.et_search) EditText searchBar;
    @BindView(R.id.iv_cancel_search) ImageView cancelSearch;
    @BindView(R.id.iv_search_icon) ImageView searchIcon;
    @BindView(R.id.ctv_selected_section_header) CustomTextView selectedSectionHeader;

    private ArrayList<Combination> likedCombinations;
    private CombinationsRecyclerViewAdapter adapter;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_base_recyclerview, container, false);

        unbinder = ButterKnife.bind(this, view);

        selectedSectionHeader.setText(R.string.liked_header);
        configureSearchWidget(searchBar,searchIcon,cancelSearch,selectedSectionHeader);

        DatabaseProvider.getInstance().getLikedCombinations(this);

        return view;
    }

    public void onDataGathered(ArrayList<Combination> combinations) {
        if(adapter == null) {
            likedCombinations = combinations;
            instantiateRV();
        } else {
            adapter.setNewData(combinations);
        }
    }

    @Override
    protected void instantiateRV() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CombinationsRecyclerViewAdapter(Constants.RV_LIKED_COMBINATIONS, likedCombinations, new OnClickViewHolder() {
            @Override
            public void onItemClick(Combination combination, CustomTextView likeCounter, int position) {

            }

            @Override
            public void onItemLongClick(Combination combination) {
                ((MyProfileActivity) getActivity())
                        .replaceFragment(CombinationDetailsFragment.newInstance(MyProfileActivity.class.getSimpleName(), combination));
            }
        });
        recyclerView.setAdapter(adapter);

        RVScrollController scrollController = new RVScrollController();
        scrollController.addControlToBottomNavigation(recyclerView);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
