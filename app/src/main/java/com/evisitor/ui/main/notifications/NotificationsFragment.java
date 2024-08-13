package com.evisitor.ui.main.notifications;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.NotificationResponse;
import com.evisitor.databinding.FragmentNotificationsBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends BaseFragment<FragmentNotificationsBinding, NotificationsViewModel> implements NotificationNavigator {
    private List<NotificationResponse.ContentBean> notificationsList;
    private RecyclerViewScrollListener scrollListener;
    private NotificationAdapter adapter;
    private int page = 0;
    private NotificationFragmentInteraction interaction;

    public static NotificationsFragment newInstance(NotificationFragmentInteraction interaction) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setInteraction(interaction);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
    }

    private void setInteraction(NotificationFragmentInteraction interaction) {
        this.interaction = interaction;
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
    public NotificationsViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(NotificationsViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().header.tvTitle.setText(R.string.title_notification);

        notificationsList = new ArrayList<>();
        adapter = new NotificationAdapter(notificationsList);
        //adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                setAdapterLoading(true);
                page++;

                mViewModel.getNotifications(page);
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);
        mViewModel.doReadAllNotification();
        updateUI();
    }

    private void updateUI() {
        getViewDataBinding().swipeToRefresh.setRefreshing(true);
        doRefreshApiCall();
    }


    private void doRefreshApiCall() {
        if (scrollListener != null) {
            scrollListener.onDataCleared();
        }
        notificationsList.clear();
        this.page = 0;
        mViewModel.getNotifications(page);
    }

    @Override
    public void hideSwipeToRefresh() {
        setAdapterLoading(false);
        getViewDataBinding().swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void onNotificationSuccess(List<NotificationResponse.ContentBean> content) {
        if (this.page == 0) notificationsList.clear();

        notificationsList.addAll(content);
        adapter.notifyDataSetChanged();

        if (notificationsList.isEmpty()) {
            getViewDataBinding().recyclerView.setVisibility(View.GONE);
            getViewDataBinding().tvNoData.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().tvNoData.setVisibility(View.GONE);
            getViewDataBinding().recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapterLoading(boolean isShowLoader) {
        if (adapter != null) {
            adapter.showLoading(isShowLoader);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshList() {
        doRefreshApiCall();
    }

    @Override
    public void onReadAllNotification() {
        if (interaction != null) interaction.onReadAllNotification();
    }

    public interface NotificationFragmentInteraction {
        void onReadAllNotification();
    }
}
