package com.evisitor.ui.main.home.trespasser.guests;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.BR;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.TrespasserResponse;
import com.evisitor.databinding.FragmentTrespasserGuestBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.main.home.trespasser.TrespasserAdapter;
import com.evisitor.ui.main.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class TrespasserGuestFragment extends BaseFragment<FragmentTrespasserGuestBinding,TrespasserGuestViewModel> implements TrespasserGuestNavigator {
    private RecyclerViewScrollListener scrollListener;
    private TrespasserAdapter adapter;
    private String search = "";
    private int page = 0;

    private List<TrespasserResponse.ContentBean> list;

    public static TrespasserGuestFragment newInstance() {
        TrespasserGuestFragment fragment = new TrespasserGuestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public synchronized void setSearch(String search) {
        this.search = search;
        doSearch(search);
    }
    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_trespasser_guest;
    }

    @Override
    public TrespasserGuestViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(TrespasserGuestViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().setNavigator(this);
        setUpAdapter();
    }


    private void setUpAdapter() {
        list = new ArrayList<>();
        adapter = new TrespasserAdapter(list, pos -> VisitorProfileDialog.newInstance(getViewModel().getVisitorDetail(list.get(pos)), null).setBtnVisible(false).show(getChildFragmentManager()));
        adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                setAdapterLoading(true);
                page++;

                getViewModel().getTrespasserGuest(page, search);
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);

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
        getViewModel().getTrespasserGuest(page, search.trim());
    }

    @Override
    public void onTrespasserSuccess(List<TrespasserResponse.ContentBean> beans) {
        if (page == 0) this.list.clear();

        this.list.addAll(beans);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void hideSwipeToRefresh() {
        setAdapterLoading(false);
        getViewDataBinding().swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void refreshList() {
        doSearch(search);
    }


    private void setAdapterLoading(boolean isShowLoader) {
        if (adapter != null) {
            adapter.showLoading(isShowLoader);
            adapter.notifyDataSetChanged();
        }
    }
}
