package com.stoyanivanov.tastethat.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.stoyanivanov.tastethat.network.TasteThatApplication;
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

    public ContentOrder currORDER = ContentOrder.TIMESTAMP;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View inflateCurrentView(int layoutResID, LayoutInflater inflater, ViewGroup container) {
        View view = super.inflateCurrentView(layoutResID, inflater, container);

        configureSearchWidget();
        return view;
    }

    protected void setupOptionsMenu(View view) {
        final ImageView optionsMenu = view.findViewById(R.id.iv_options_menu);
        optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), optionsMenu);
                PopUpMenuController popUpMenuController = new PopUpMenuController(popupMenu);
                popUpMenuController.inflatePopupMenu(BaseRecyclerViewFragment.this);
            }
        });
    }

    private void configureSearchWidget() {

        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    TasteThatApplication.showVirtualKeyboard();
                } else {
                    TasteThatApplication.hideVirtualKeyboard(v);
                }
            }
        });

        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    startFilteringContent();
                    return true;
                }
                return false;
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchBar.getVisibility() == View.VISIBLE) {
                    startFilteringContent();
                    TasteThatApplication.hideVirtualKeyboard(v);
                } else {
                    showAppBarSearch(searchBar,cancelSearch,selectedSectionHeader, optionsMenu);
                }
            }
        });

        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyAdapterOnSearchCancel();
                searchBar.setText("");
                TasteThatApplication.hideVirtualKeyboard(v);
                showAppBarHeader(searchBar,cancelSearch,selectedSectionHeader, optionsMenu);
            }
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

    protected abstract void instantiateRV();

    public abstract void startLoadingCombinations();
}
