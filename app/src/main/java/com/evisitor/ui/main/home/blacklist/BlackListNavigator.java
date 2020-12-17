package com.evisitor.ui.main.home.blacklist;

import com.evisitor.data.model.BlackListVisitorResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface BlackListNavigator extends BaseNavigator {

    void hideSwipeToRefresh();

    void OnSuccessBlackList(List<BlackListVisitorResponse.ContentBean> beans);
}
