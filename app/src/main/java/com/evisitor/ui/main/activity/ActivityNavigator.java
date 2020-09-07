package com.evisitor.ui.main.activity;

import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseKeeping;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

public interface ActivityNavigator extends BaseNavigator {
    void onExpectedGuestSuccess(List<Guests> guestsList);

    void onExpectedHKSuccess(List<HouseKeeping> houseKeepingList);

    void onExpectedSPSuccess(List<ServiceProvider> spList);

    void hideSwipeToRefresh();

    void refreshList();
}
