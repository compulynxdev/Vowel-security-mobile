package com.evisitor.ui.main.commercial.staff.registered;

import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface RegisteredCommercialStaffNavigator extends BaseNavigator {
    void onRegisteredOfficeStaffSuccess(List<CommercialStaffResponse.ContentBean> houseKeepingList);

    void hideSwipeToRefresh();

    void refreshList();
}
