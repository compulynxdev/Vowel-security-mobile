package com.evisitor.ui.main.commercial.staff.expected;

import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface ExpectedCommercialStaffNavigator extends BaseNavigator {
    void onExpectedOFSuccess(List<CommercialStaffResponse.ContentBean> houseKeepingList);

    void hideSwipeToRefresh();

    void refreshList();

    //void onScannedDataRetrieve(CommercialStaffResponse.ContentBean content);
}
