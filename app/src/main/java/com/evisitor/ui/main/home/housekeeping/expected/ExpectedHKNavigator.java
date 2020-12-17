package com.evisitor.ui.main.home.housekeeping.expected;

import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface ExpectedHKNavigator extends BaseNavigator {
    void onExpectedHKSuccess(List<HouseKeepingResponse.ContentBean> houseKeepingList);

    void hideSwipeToRefresh();

    void refreshList();
}
