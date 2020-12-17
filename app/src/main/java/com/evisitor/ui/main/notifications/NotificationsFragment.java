package com.evisitor.ui.main.notifications;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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
    private String search = "";
    private int page = 0;
    private NotificationFragmentInteraction interaction;

    public static NotificationsFragment newInstance(NotificationFragmentInteraction interaction) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setInteraction(interaction);
        return fragment;
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
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(NotificationsViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().setNavigator(this);
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

                getViewModel().getNotifications(page, search);
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);
        getViewModel().doReadAllNotification();
        updateUI();
    }


    private void setUpSearch() {
        getViewDataBinding().header.imgSearch.setVisibility(View.VISIBLE);
        getViewDataBinding().header.imgSearch.setOnClickListener(v -> {
            hideKeyboard();
            getViewDataBinding().customSearchView.llSearchBar.setVisibility(getViewDataBinding().customSearchView.llSearchBar.getVisibility() == View.GONE
                    ? View.VISIBLE : View.GONE);

            getViewDataBinding().customSearchView.searchView.setQuery("", false);
        });
        setupSearchSetting(getViewDataBinding().customSearchView.searchView);
        getViewDataBinding().customSearchView.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String txt = newText.trim();
                if (txt.isEmpty() || txt.length() >= 3) {
                    doSearch(txt);
                }
                return false;
            }
        });
    }

    private void updateUI() {
        getViewDataBinding().swipeToRefresh.setRefreshing(true);
        doSearch(search);
    }


    private void doSearch(String search) {
        scrollListener.onDataCleared();
        notificationsList.clear();
        this.page = 0;
        getViewModel().getNotifications(page, search);
    }

    @Override
    public void hideSwipeToRefresh() {
        setAdapterLoading(false);
        getViewDataBinding().swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void onNotificationSuccess(List<NotificationResponse.ContentBean> content) {
        if (page == 0) notificationsList.clear();

        notificationsList.addAll(content);
        adapter.notifyDataSetChanged();

        if (notificationsList.size() == 0) {
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
        doSearch(search);
    }

    @Override
    public void onReadAllNotification() {
        if (interaction != null) interaction.onReadAllNotification();
    }

    public interface NotificationFragmentInteraction {
        void onReadAllNotification();
    }
}
