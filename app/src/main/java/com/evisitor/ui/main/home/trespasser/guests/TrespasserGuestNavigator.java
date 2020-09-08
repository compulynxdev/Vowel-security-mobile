package com.evisitor.ui.main.home.trespasser.guests;

import com.evisitor.data.model.TrespassserResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

public interface TrespasserGuestNavigator extends BaseNavigator {

    void onTrespasserSuccess(List<TrespassserResponse.ContentBean> beans);

    void hideSwipeToRefresh();

    void refreshList();
}
