package com.evisitor.ui.main.home.trespasser.services;

import com.evisitor.data.model.TrespasserResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

public interface TrespasserSPNavigator extends BaseNavigator {
    void onTrespasserSuccess(List<TrespasserResponse.ContentBean> beans);

    void hideSwipeToRefresh();

    void refreshList();
}
