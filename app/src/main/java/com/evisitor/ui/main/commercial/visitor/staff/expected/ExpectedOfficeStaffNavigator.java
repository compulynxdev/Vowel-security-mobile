package com.evisitor.ui.main.commercial.visitor.staff.expected;

import com.evisitor.data.model.OfficeStaffResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface ExpectedOfficeStaffNavigator extends BaseNavigator {
    void onExpectedOFSuccess(List<OfficeStaffResponse.ContentBean> houseKeepingList);

    void hideSwipeToRefresh();

    void refreshList();
}
