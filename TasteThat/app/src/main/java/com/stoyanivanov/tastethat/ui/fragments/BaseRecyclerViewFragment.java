package com.stoyanivanov.tastethat.ui.fragments;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.stoyanivanov.tastethat.network.TasteThatApplication;
import com.stoyanivanov.tastethat.view_utils.custom_views.CustomTextView;


public abstract class BaseRecyclerViewFragment extends BaseFragment {

    protected void configureSearchWidget(final EditText searchBar, final ImageView searchIcon,
                                    final ImageView cancelSearch, final CustomTextView selectedSectionHeader) {

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
                    showAppBarSearch(searchBar,cancelSearch,selectedSectionHeader);
                }
            }
        });

        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyAdapterOnSearchCancel();
                searchBar.setText("");
                TasteThatApplication.hideVirtualKeyboard(v);
                showAppBarHeader(searchBar,cancelSearch,selectedSectionHeader);
            }
        });
    }


    private void showAppBarHeader(EditText searchBar, ImageView cancelSearch,
                                  CustomTextView selectedSectionHeader) {

        searchBar.setVisibility(View.INVISIBLE);
        cancelSearch.setVisibility(View.INVISIBLE);
        selectedSectionHeader.setVisibility(View.VISIBLE);
    }

    private void showAppBarSearch(EditText searchBar, ImageView cancelSearch,
                                  CustomTextView selectedSectionHeader) {

        selectedSectionHeader.setVisibility(View.INVISIBLE);
        searchBar.setVisibility(View.VISIBLE);
        cancelSearch.setVisibility(View.VISIBLE);
        searchBar.requestFocus();
    }

    protected abstract void startFilteringContent();

    protected abstract void notifyAdapterOnSearchCancel();

    protected abstract void instantiateRV();
}
