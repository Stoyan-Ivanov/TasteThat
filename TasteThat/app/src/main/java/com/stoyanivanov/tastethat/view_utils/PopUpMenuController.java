package com.stoyanivanov.tastethat.view_utils;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

import com.stoyanivanov.tastethat.R;

/**
 * Created by stoyan-ivanov on 06.11.17.
 */

public class PopUpMenuController {
    private PopupMenu popupMenu;
    private String rvTag;
    private MyRecyclerViewAdapter.ViewHolder viewHolder;

    public PopUpMenuController(PopupMenu popupMenu, String rvTag, MyRecyclerViewAdapter.ViewHolder viewHolder) {
        this.popupMenu = popupMenu;
        this.rvTag = rvTag;
        this.viewHolder = viewHolder;
    }

    public void inflatePopupMenu(final int position) {

        switch (rvTag) {
            case "rvAllCombinations":
                allCombinationsPopup(position);
                break;

            case "rvUploadedCombinations":
                uploadedCombinationsPopup(position);
                break;

            case "rvLikedCombinations":
                likedCombinationsPopup();
                break;
        }
    }

    private void allCombinationsPopup(final int position) {
        showPopup(R.menu.popup_menu_all_combinations);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.pm_rv_all_share:
                        break;

                    case R.id.pm_rv_all_report:
                        break;

                }
                return true;
            }
        });

    }

    private void uploadedCombinationsPopup(final int position) {
        showPopup(R.menu.popup_menu_uploaded_combinations);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.pm_rv_uploaded_share:
                        break;

                    case R.id.pm_rv_uploaded_delete:
                        viewHolder.deleteCombinationFromRV(position);
                        //viewHolder.deleteCombinationFromDB();
                        break;

                }
                return true;
            }
        });

    }

    private void likedCombinationsPopup() {
        showPopup(R.menu.popup_menu_liked_combinations);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.pm_rv_liked_share:
                        break;

                }
                return true;
            }
        });

    }

    private void showPopup(int menuId) {
        popupMenu.getMenuInflater().inflate(menuId, popupMenu.getMenu());
        popupMenu.show();
    }

}
