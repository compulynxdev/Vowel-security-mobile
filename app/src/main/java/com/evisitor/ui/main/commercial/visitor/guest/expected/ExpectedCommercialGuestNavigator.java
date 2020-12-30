package com.evisitor.ui.main.commercial.visitor.guest.expected;

import com.evisitor.data.model.CommercialGuestResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface ExpectedCommercialGuestNavigator extends BaseNavigator {
    void onExpectedGuestSuccess(List<CommercialGuestResponse.CommercialGuest> guestsList);

    void hideSwipeToRefresh();

    void refreshList();
}
