package com.evisitor.ui.main.home.rejected.guest;

import com.evisitor.data.model.Guests;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface RejectedGuestNavigator extends BaseNavigator {

    void onSuccess(List<Guests> beans);

    void hideSwipeToRefresh();

    void refreshList();
}
