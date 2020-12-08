package com.evisitor.ui.main.home.rejected.staff;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import android.view.View;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.databinding.FragmentRejectedStaffBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.main.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;
import java.util.ArrayList;
import java.util.List;

public class RejectedStaffFragment  extends BaseFragment<FragmentRejectedStaffBinding,RejectedStaffViewModel> implements RejectedStaffNavigator {
    private RecyclerViewScrollListener scrollListener;
    private RejectedStaffAdapter adapter;
    private String search = "";
    private int page = 0;

    private List<HouseKeepingResponse.ContentBean> list;

    public static RejectedStaffFragment newInstance() {
        RejectedStaffFragment fragment = new RejectedStaffFragment();
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
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_rejected_staff;
    }

    @Override
    public RejectedStaffViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(RejectedStaffViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().setNavigator(this);
        setUpAdapter();
    }


    private void setUpAdapter() {
        list = new ArrayList<>();
        adapter = new RejectedStaffAdapter(list, pos -> VisitorProfileDialog.newInstance(getViewModel().getVisitorDetail(list.get(pos)), null).setImage(list.get(pos).getImageUrl()).setBtnVisible(false).show(getChildFragmentManager()));
        adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                setAdapterLoading(true);
                page++;

                getViewModel().getDomesticStaff(page, search);
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
        getViewModel().getDomesticStaff(page, search.trim());
    }


    @Override
    public void onSuccess(List<HouseKeepingResponse.ContentBean> beans) {
        if (page == 0) this.list.clear();

        this.list.addAll(beans);
        adapter.notifyDataSetChanged();

        if(this.list.size()==0){
            getViewDataBinding().recyclerView.setVisibility(View.GONE);
            getViewDataBinding().tvNoData.setVisibility(View.VISIBLE);
        }else{
            getViewDataBinding().tvNoData.setVisibility(View.GONE);
            getViewDataBinding().recyclerView.setVisibility(View.VISIBLE);
        }
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

