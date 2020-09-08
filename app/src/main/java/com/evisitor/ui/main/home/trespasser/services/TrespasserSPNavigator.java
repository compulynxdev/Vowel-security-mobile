package com.evisitor.ui.main.home.trespasser.services;

import com.evisitor.data.model.TrespassserResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

public interface TrespasserSPNavigator extends BaseNavigator {
    void onTrespasserSuccess(List<TrespassserResponse.ContentBean> beans);

    void hideSwipeToRefresh();

    void refreshList();
}
