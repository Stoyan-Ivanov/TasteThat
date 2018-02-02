package com.stoyanivanov.tastethat.view_utils.controllers;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.CombinationsViewHolder;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.*;


/**
 * Created by stoyan-ivanov on 06.11.17.
 */

public class PopUpMenuController {
    private PopupMenu popupMenu;
    private String rvTag;
    private CombinationsViewHolder viewHolder;
    private String combinationNameKey;

    public PopUpMenuController(PopupMenu popupMenu, String rvTag, CombinationsViewHolder viewHolder) {
        this.popupMenu = popupMenu;
        this.rvTag = rvTag;
        this.viewHolder = viewHolder;
    }

    public void inflatePopupMenu(final int position, final String combinationNameKey) {
        this.combinationNameKey = combinationNameKey;

        switch (rvTag) {
            case Constants.RV_ALL_COMBINATIONS:
                allCombinationsPopup(position);
                break;

            case Constants.RV_UPLOADED_COMBINATIONS:
                uploadedCombinationsPopup(position);
                break;

            case Constants.RV_LIKED_COMBINATIONS:
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
                        viewHolder.deleteViewHolderFromRV(position);
                        deleteCombinationFromDB();
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

    public void deleteCombinationFromDB() {
        tableCombinations.child(combinationNameKey).removeValue();

        tableUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .child(combinationNameKey).removeValue();
    }

}
