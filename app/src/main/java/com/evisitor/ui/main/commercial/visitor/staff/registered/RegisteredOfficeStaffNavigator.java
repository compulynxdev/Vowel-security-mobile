package com.evisitor.ui.main.commercial.visitor.staff.registered;

import com.evisitor.data.model.OfficeStaffResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface RegisteredOfficeStaffNavigator extends BaseNavigator {
    void onRegisteredOfficeStaffSuccess(List<OfficeStaffResponse.ContentBean> houseKeepingList);

    void hideSwipeToRefresh();

    void refreshList();
}
