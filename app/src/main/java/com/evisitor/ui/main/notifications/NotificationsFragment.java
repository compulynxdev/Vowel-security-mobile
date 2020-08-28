package com.evisitor.ui.main.notifications;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.Notifications;
import com.evisitor.databinding.FragmentNotificationsBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.base.BaseNavigator;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends BaseFragment<FragmentNotificationsBinding, NotificationsFragmentViewModel> implements BaseNavigator {

    private List<Notifications> notificationsList = new ArrayList<>();
    private NotificationAdapter adapter;

    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_notifications;
    }

    @Override
    public NotificationsFragmentViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(NotificationsFragmentViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().setNavigator(this);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_notification);

        Notifications notifications = new Notifications();
      /*  notifications.setTitle("Suresh");
        notifications.setTime(CalenderUtils.getCurrentTime());
        notifications.setMsg("Check in approved");
        notificationsList.add(notifications);

        notifications = new Notifications();
        notifications.setTitle("Suresh");
        notifications.setTime(CalenderUtils.getCurrentTime());
        notifications.setMsg("Check in rejected");
        notificationsList.add(notifications);*/

        adapter = new NotificationAdapter(notificationsList);
        getViewDataBinding().recyclerView.setAdapter(adapter);

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(() -> getViewDataBinding().swipeToRefresh.setRefreshing(false));
    }
}
