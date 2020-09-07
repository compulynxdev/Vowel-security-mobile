package com.evisitor.ui.main.home.guest.expected;

import com.evisitor.data.model.Guests;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

public interface ExpectedGuestNavigator extends BaseNavigator {
    void onExpectedGuestSuccess(List<Guests> guestsList);

    void hideSwipeToRefresh();

    void refreshList();
}
