package com.evisitor.ui.main.home.rejected.sp;

import com.evisitor.data.model.ServiceProvider;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface RejectedSPNavigator extends BaseNavigator {

    void onSuccess(List<ServiceProvider> beans);

    void hideSwipeToRefresh();

    void refreshList();
}
