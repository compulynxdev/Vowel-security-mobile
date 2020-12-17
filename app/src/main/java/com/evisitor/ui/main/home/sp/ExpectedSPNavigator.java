package com.evisitor.ui.main.home.sp;

import com.evisitor.data.model.ServiceProvider;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface ExpectedSPNavigator extends BaseNavigator {
    void onExpectedSPSuccess(List<ServiceProvider> spList);

    void hideSwipeToRefresh();

    void refreshList();
}
