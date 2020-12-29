package com.evisitor.ui.main.residential.staff.registered;

import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface RegisteredHKNavigator extends BaseNavigator {
    void onRegisteredHKSuccess(List<HouseKeepingResponse.ContentBean> houseKeepingList);

    void hideSwipeToRefresh();

    void refreshList();
}
