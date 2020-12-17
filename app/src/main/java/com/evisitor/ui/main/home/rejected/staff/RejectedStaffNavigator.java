package com.evisitor.ui.main.home.rejected.staff;

import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface RejectedStaffNavigator extends BaseNavigator {

    void onSuccess(List<HouseKeepingResponse.ContentBean> beans);

    void hideSwipeToRefresh();

    void refreshList();
}
