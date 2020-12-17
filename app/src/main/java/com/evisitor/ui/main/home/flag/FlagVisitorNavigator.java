package com.evisitor.ui.main.home.flag;

import com.evisitor.data.model.FlaggedVisitorResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface FlagVisitorNavigator extends BaseNavigator {

    void hideSwipeToRefresh();

    void onSuccessFlagList(List<FlaggedVisitorResponse.ContentBean> beans);
}
