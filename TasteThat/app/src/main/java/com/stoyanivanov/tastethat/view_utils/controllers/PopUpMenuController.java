package com.stoyanivanov.tastethat.view_utils.controllers;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.ContentOrder;
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.ui.fragments.BaseRecyclerViewFragment;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.CombinationsViewHolder;

import static com.stoyanivanov.tastethat.constants.DatabaseReferences.*;


/**
 * Created by stoyan-ivanov on 06.11.17.
 */

public class PopUpMenuController {
    private PopupMenu popupMenu;
    private String rvTag;
    private CombinationsViewHolder viewHolder;
    private String combinationKey;

    public PopUpMenuController(PopupMenu popupMenu, String rvTag, CombinationsViewHolder viewHolder) {
        this.popupMenu = popupMenu;
        this.rvTag = rvTag;
        this.viewHolder = viewHolder;
    }

    public PopUpMenuController(PopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    public void inflatePopupMenu(final int position, final String combinationKey) {
        this.combinationKey = combinationKey;

        switch (rvTag) {
            case Constants.RV_ALL_COMBINATIONS:
                allCombinationsVHPopup(position);
                break;

            case Constants.RV_UPLOADED_COMBINATIONS:
                uploadedCombinationsVHPopup(position);
                break;

            case Constants.RV_LIKED_COMBINATIONS:
                likedCombinationsVHPopup();
                break;
        }
    }

    public void inflatePopupMenu(BaseRecyclerViewFragment fragment) {
        orderContentByPopup(fragment);
    }

    private void orderContentByPopup(final BaseRecyclerViewFragment fragment) {

        showPopup(R.menu.actionbar_menu_order);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.order_by_likes:
                        fragment.currORDER = ContentOrder.HIGHEST_RATING;
                        fragment.startLoadingCombinations();
                        TasteThatApplication.showToast("Ordering by likes...");
                        break;

                    case R.id.order_by_timestamp:
                        fragment.currORDER = ContentOrder.TIMESTAMP;
                        fragment.startLoadingCombinations();
                        TasteThatApplication.showToast("Ordering by timestamp...");
                        break;
                }
                return true;
            }
        });
    }

    private void allCombinationsVHPopup(final int position) {
        showPopup(R.menu.popup_menu_all_combinations);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.pm_rv_all_share:
                        showNotAvailableToast();
                        break;

                    case R.id.pm_rv_all_report:
                        showNotAvailableToast();
                        break;

                }
                return true;
            }
        });

    }

    private void uploadedCombinationsVHPopup(final int position) {
        showPopup(R.menu.popup_menu_uploaded_combinations);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.pm_rv_uploaded_share:
                       showNotAvailableToast();
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

    private void likedCombinationsVHPopup() {
        showPopup(R.menu.popup_menu_liked_combinations);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.pm_rv_liked_share:
                        showNotAvailableToast();
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

    private void deleteCombinationFromDB() {
        tableCombinations.child(combinationKey).removeValue();

        tableUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Constants.USER_UPLOADED_COMBINATIONS)
                .child(combinationKey).removeValue();
    }

    private void showNotAvailableToast() {
        TasteThatApplication.showToast(TasteThatApplication
                .getStaticContext()
                .getResources()
                .getString(R.string.toast_not_available));
    }
}
