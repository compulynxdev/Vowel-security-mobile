package com.evisitor.ui.main.activity;

import com.evisitor.data.model.CommercialGuestResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseKeeping;
import com.evisitor.data.model.OfficeStaffResponse;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

public interface ActivityNavigator extends BaseNavigator {
    void onExpectedCommercialGuestSuccess(List<CommercialGuestResponse.CommercialGuest> guestsList);

    void onExpectedGuestSuccess(List<Guests> guestsList);

    void onExpectedHKSuccess(List<HouseKeeping> houseKeepingList);

    void onExpectedOfficeSuccess(List<OfficeStaffResponse.ContentBean> officeStaffList);

    void onExpectedSPSuccess(List<ServiceProvider> spList);

    void hideSwipeToRefresh();

    void refreshList();
}
