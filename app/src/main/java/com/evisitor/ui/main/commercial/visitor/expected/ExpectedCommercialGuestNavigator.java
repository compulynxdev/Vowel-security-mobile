package com.evisitor.ui.main.commercial.visitor.expected;

import com.evisitor.data.model.CommercialVisitorResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface ExpectedCommercialGuestNavigator extends BaseNavigator {
    void onExpectedGuestSuccess(List<CommercialVisitorResponse.CommercialGuest> guestsList);

    void hideSwipeToRefresh();

    void refreshList();
}
