package com.evisitor.ui.main.residential.guest.expected;

import com.evisitor.data.model.Guests;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface ExpectedGuestNavigator extends BaseNavigator {
    void onExpectedGuestSuccess(List<Guests> guestsList);

    void hideSwipeToRefresh();

    void refreshList();
}
