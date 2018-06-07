package com.stoyanivanov.tastethat.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.ContentOrder;
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.ui.base_ui.BaseFragment;
import com.stoyanivanov.tastethat.view_utils.controllers.PopUpMenuController;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;

import butterknife.BindView;

public abstract class BaseRecyclerViewFragment extends BaseFragment {

    @BindView(R.id.et_search) EditText searchBar;
    @BindView(R.id.iv_cancel_search) ImageView cancelSearch;
    @BindView(R.id.iv_search_icon) ImageView searchIcon;
    @BindView(R.id.ctv_selected_section_header) CustomTextView selectedSectionHeader;
    @BindView(R.id.rv) RecyclerView recyclerView;
    @BindView(R.id.iv_options_menu) ImageView optionsMenu;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    protected boolean isLoading = false;
    public ContentOrder currORDER = ContentOrder.TIMESTAMP;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    private void configureSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if(!isLoading) {
                    startLoadingCombinations();
                }
            }, 500);
        });
    }

    @Override
    protected View inflateCurrentView(int layoutResID, LayoutInflater inflater, ViewGroup container) {
        View view = super.inflateCurrentView(layoutResID, inflater, container);

        configureSearchWidget();
        configureSwipeRefresh();
        return view;
    }

    protected void setupOptionsMenu(View view) {
        final ImageView optionsMenu = view.findViewById(R.id.iv_options_menu);
        optionsMenu.setOnClickListener(view1 -> {
            PopupMenu popupMenu = new PopupMenu(view1.getContext(), optionsMenu);
            PopUpMenuController popUpMenuController = new PopUpMenuController(popupMenu);
            popUpMenuController.inflatePopupMenu(BaseRecyclerViewFragment.this);
        });
    }

    private void configureSearchWidget() {

        searchBar.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                TasteThatApplication.showVirtualKeyboard();
            } else {
                TasteThatApplication.hideVirtualKeyboard(v);
            }
        });

        searchBar.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {

                startFilteringContent();
                return true;
            }
            return false;
        });

        searchIcon.setOnClickListener(v -> {
            if(searchBar.getVisibility() == View.VISIBLE) {
                startFilteringContent();
                TasteThatApplication.hideVirtualKeyboard(v);
            } else {
                showAppBarSearch(searchBar,cancelSearch,selectedSectionHeader, optionsMenu);
            }
        });

        cancelSearch.setOnClickListener(v -> {
            notifyAdapterOnSearchCancel();
            searchBar.setText("");
            TasteThatApplication.hideVirtualKeyboard(v);
            showAppBarHeader(searchBar,cancelSearch,selectedSectionHeader, optionsMenu);
        });
    }


    private void showAppBarHeader(EditText searchBar, ImageView cancelSearch,
                                  CustomTextView selectedSectionHeader, ImageView optionsMenu) {

        searchBar.setVisibility(View.INVISIBLE);
        cancelSearch.setVisibility(View.INVISIBLE);
        selectedSectionHeader.setVisibility(View.VISIBLE);
        optionsMenu.setVisibility(View.VISIBLE);
    }

    private void showAppBarSearch(EditText searchBar, ImageView cancelSearch,
                                  CustomTextView selectedSectionHeader, ImageView optionsMenu) {

        selectedSectionHeader.setVisibility(View.INVISIBLE);
        searchBar.setVisibility(View.VISIBLE);
        cancelSearch.setVisibility(View.VISIBLE);
        optionsMenu.setVisibility(View.INVISIBLE);
        searchBar.requestFocus();
    }

    protected abstract void startFilteringContent();

    protected abstract void notifyAdapterOnSearchCancel();

    protected abstract void instantiateRecyclerView();

    public abstract void startLoadingCombinations();
}
