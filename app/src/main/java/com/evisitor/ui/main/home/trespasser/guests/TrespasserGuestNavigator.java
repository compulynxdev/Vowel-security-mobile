package com.evisitor.ui.main.home.trespasser.guests;

import com.evisitor.data.model.TrespasserResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface TrespasserGuestNavigator extends BaseNavigator {

    void onTrespasserSuccess(List<TrespasserResponse.ContentBean> beans);

    void hideSwipeToRefresh();

    void refreshList();
}
