package com.stoyanivanov.tastethat.view_utils.controllers;

import android.support.v7.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;
import com.stoyanivanov.tastethat.constants.Constants;
import com.stoyanivanov.tastethat.R;
import com.stoyanivanov.tastethat.constants.ContentOrder;
import com.stoyanivanov.tastethat.TasteThatApplication;
import com.stoyanivanov.tastethat.db.DatabaseProvider;
import com.stoyanivanov.tastethat.db.models.Combination;
import com.stoyanivanov.tastethat.ui.fragments.BaseRecyclerViewFragment;
import com.stoyanivanov.tastethat.view_utils.recyclerview_utils.combinations_recyclerview.CombinationsViewHolder;


/**
 * Created by stoyan-ivanov on 06.11.17.
 */

public class PopUpMenuController {
    private PopupMenu mPopupMenu;
    private String mRvTag;
    private CombinationsViewHolder mViewHolder;
    private Combination mCombination;

    public PopUpMenuController(PopupMenu popupMenu, String rvTag, CombinationsViewHolder viewHolder) {
        this.mPopupMenu = popupMenu;
        this.mRvTag = rvTag;
        this.mViewHolder = viewHolder;
    }

    public PopUpMenuController(PopupMenu popupMenu) {
        this.mPopupMenu = popupMenu;
    }

    public void inflatePopupMenu(final int position, final Combination combination) {
        this.mCombination = combination;

        switch (mRvTag) {
            case Constants.RV_ALL_COMBINATIONS:
                allCombinationsVHPopup(position);
                break;

            case Constants.RV_UPLOADED_COMBINATIONS:
                uploadedCombinationsVHPopup(position);
                break;

            case Constants.RV_RATED_COMBINATIONS:
                ratedCombinationsVHPopup();
                break;
        }
    }

    public void inflatePopupMenu(BaseRecyclerViewFragment fragment) {
        orderContentByPopup(fragment);
    }

    private void orderContentByPopup(final BaseRecyclerViewFragment fragment) {

        showPopup(R.menu.actionbar_menu_order);

        mPopupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.order_by_likes:
                    fragment.currORDER = ContentOrder.HIGHEST_RATING;
                    fragment.startLoadingCombinations();
                    TasteThatApplication.showToast("Ordering by rating...");
                    break;

                case R.id.order_by_timestamp:
                    fragment.currORDER = ContentOrder.TIMESTAMP;
                    fragment.startLoadingCombinations();
                    TasteThatApplication.showToast("Ordering by timestamp...");
                    break;
            }
            return true;
        });
    }

    private void allCombinationsVHPopup(final int position) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(!userId.equals(mCombination.getUserId())) {

            showPopup(R.menu.popup_menu_all_combinations);

            mPopupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.pm_rv_all_share:
                        showNotAvailableToast();
                        break;

                    case R.id.pm_rv_all_report:
                        reportCombination();
                        break;
                }
                return true;
            });
        } else {
            showPopup(R.menu.popup_menu_all_combinations_current_user);

            mPopupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.pm_rv_all_share:
                        showNotAvailableToast();
                        break;

                    case R.id.pm_rv_all_delete:
                        mViewHolder.deleteViewHolderFromRV(position);
                        deleteCombinationFromDB();
                }
                return true;
            });
        }

    }

    private void uploadedCombinationsVHPopup(final int position) {
        showPopup(R.menu.popup_menu_uploaded_combinations);

        mPopupMenu.setOnMenuItemClickListener(item -> {
            switch(item.getItemId()) {
                case R.id.pm_rv_uploaded_share:
                   showNotAvailableToast();
                    break;

                case R.id.pm_rv_uploaded_delete:
                    mViewHolder.deleteViewHolderFromRV(position);
                    deleteCombinationFromDB();
                    break;

            }
            return true;
        });

    }

    private void ratedCombinationsVHPopup() {
        showPopup(R.menu.popup_menu_rated_combinations);

        mPopupMenu.setOnMenuItemClickListener(item -> {
            switch(item.getItemId()) {
                case R.id.pm_rv_liked_share:
                    showNotAvailableToast();
                    break;
            }
            return true;
        });
    }

    private void showPopup(int menuId) {
        mPopupMenu.getMenuInflater().inflate(menuId, mPopupMenu.getMenu());
        mPopupMenu.show();
    }

    private void deleteCombinationFromDB() {
        DatabaseProvider.getInstance().deleteCombination(mCombination.getCombinationKey());
    }

    private void reportCombination() {
        DatabaseProvider.getInstance().submitReport(mCombination.getCombinationKey());
    }

    private void showNotAvailableToast() {
        TasteThatApplication.showToast(TasteThatApplication
                .getStaticContext()
                .getResources()
                .getString(R.string.toast_not_available));
    }
}
