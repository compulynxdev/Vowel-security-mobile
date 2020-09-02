package com.evisitor.ui.main.home.housekeeping.registered;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.RegisteredHKResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.FragmentExpectedBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.main.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class RegisteredHKFragment extends BaseFragment<FragmentExpectedBinding, RegisteredHKViewModel> implements RegisteredHKNavigator {
    private final int SCAN_RESULT = 101;
    private RecyclerViewScrollListener scrollListener;
    private RegisteredHKAdapter adapter;
    private String search = "";
    private int page = 0;
    private List<RegisteredHKResponse.ContentBean> list;

    public static RegisteredHKFragment newInstance() {
        RegisteredHKFragment fragment = new RegisteredHKFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setSearch(String search) {
        this.search = search;
        doSearch(search);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_expected;
    }

    @Override
    public RegisteredHKViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(RegisteredHKViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().setNavigator(this);
        setUpAdapter();
    }

    private void setUpAdapter() {
        list = new ArrayList<>();
        adapter = new RegisteredHKAdapter(list, pos -> {
            List<VisitorProfileBean> visitorProfileBeanList = getViewModel().setClickVisitorDetail(list.get(pos));
            VisitorProfileDialog.newInstance(visitorProfileBeanList, null).setBtnVisible(false).show(getFragmentManager());
        });
        adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                adapter.showLoading(true);
                adapter.notifyDataSetChanged();
                page++;

                getViewModel().getExpectedHKListData(page, search);
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);

        getViewModel().getRegisteredHKListData().observe(this, list -> {
            adapter.showLoading(false);
            adapter.notifyDataSetChanged();

            if (page == 0) this.list.clear();

            this.list.addAll(list);
            adapter.notifyDataSetChanged();
        });

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);
        updateUI();
    }

    private void updateUI() {
        getViewDataBinding().swipeToRefresh.setRefreshing(true);
        doSearch(search);
    }

    private void doSearch(String search) {
        scrollListener.onDataCleared();
        list.clear();
        this.page = 0;
        getViewModel().getExpectedHKListData(page, search.trim());
    }

    @Override
    public void hideSwipeToRefresh() {
        getViewDataBinding().swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void refreshList() {
        doSearch(search);
    }
}
