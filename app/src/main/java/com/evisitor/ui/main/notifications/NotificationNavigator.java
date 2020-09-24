package com.evisitor.ui.main.notifications;


import com.evisitor.data.model.NotificationResponse;
import com.evisitor.ui.base.BaseNavigator;

import java.util.List;

interface NotificationNavigator extends BaseNavigator {

    void hideSwipeToRefresh();

    void onNotificationSuccess(List<NotificationResponse.ContentBean> content);

    void refreshList();

    void onReadAllNotification();
}
