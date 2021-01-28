package com.evisitor.ui.main.activity;

import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.data.model.CommercialVisitorResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseKeeping;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

public interface ActivityNavigator extends BaseNavigator {
    void onExpectedCommercialGuestSuccess(int page, List<CommercialVisitorResponse.CommercialGuest> guestsList);

    void onExpectedGuestSuccess(int page, List<Guests> guestsList);

    void onExpectedHKSuccess(int page, List<HouseKeeping> houseKeepingList);

    void onExpectedOfficeSuccess(int page, List<CommercialStaffResponse.ContentBean> officeStaffList);

    void onExpectedSPSuccess(int page, List<ServiceProvider> spList);

    void hideSwipeToRefresh();

    void refreshList();
}
