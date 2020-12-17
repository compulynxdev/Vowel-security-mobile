package com.evisitor.ui.main.home.housekeeping.registered;

import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface RegisteredHKNavigator extends BaseNavigator {
    void onRegisteredHKSuccess(List<HouseKeepingResponse.ContentBean> houseKeepingList);

    void hideSwipeToRefresh();

    void refreshList();
}
