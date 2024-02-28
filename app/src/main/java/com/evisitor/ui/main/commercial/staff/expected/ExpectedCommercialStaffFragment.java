package com.evisitor.ui.main.commercial.staff.expected;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.FragmentExpectedBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.main.home.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class ExpectedCommercialStaffFragment extends BaseFragment<FragmentExpectedBinding, ExpectedCommercialStaffViewModel> implements ExpectedCommercialStaffNavigator {
    private RecyclerViewScrollListener scrollListener;
    private ExpectedCommercialStaffAdapter adapter;
    private String search = "";
    private int page = 0;
    private List<CommercialStaffResponse.ContentBean> list;

    public static ExpectedCommercialStaffFragment newInstance() {
        ExpectedCommercialStaffFragment fragment = new ExpectedCommercialStaffFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setCheckInOutNavigator(this);
    }

    public synchronized void setSearch(String search) {
        this.search = search;
        doSearch(search);
    }

    /*public void scanQRId(String code) {
        mViewModel.getScannedData(code);
    }*/

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_expected;
    }

    @Override
    public ExpectedCommercialStaffViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(ExpectedCommercialStaffViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().tvNoData.setText(getString(R.string.no_expected_ofc_staff));
        setUpAdapter();
    }

    private void setUpAdapter() {
        list = new ArrayList<>();
        adapter = new ExpectedCommercialStaffAdapter(list, pos -> {
            List<VisitorProfileBean> visitorProfileBeanList = mViewModel.setClickVisitorDetail(list.get(pos));
            VisitorProfileDialog.newInstance(visitorProfileBeanList, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                mViewModel.doCheckIn();
            }).setImage(list.get(pos).getImageUrl()).setDocumentImage(list.get(pos).getDocumentImage()).setBtnLabel(getString(R.string.check_in))
        .show(getFragmentManager());
        });
        adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                setAdapterLoading(true);
                page++;

                mViewModel.getExpectedOfficeListData(page, search);
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
        if (scrollListener != null) {
            scrollListener.onDataCleared();
        }
        list.clear();
        this.page = 0;
        mViewModel.getExpectedOfficeListData(page, search.trim());
    }

    @Override
    public void onExpectedOFSuccess(List<CommercialStaffResponse.ContentBean> houseKeepingList) {
        if (this.page == 0) this.list.clear();

        this.list.addAll(houseKeepingList);
        adapter.notifyDataSetChanged();

        if (this.list.size() == 0) {
            getViewDataBinding().recyclerView.setVisibility(View.GONE);
            getViewDataBinding().tvNoData.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().tvNoData.setVisibility(View.GONE);
            getViewDataBinding().recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideSwipeToRefresh() {
        setAdapterLoading(false);
        getViewDataBinding().swipeToRefresh.setRefreshing(false);
    }

    private void setAdapterLoading(boolean isShowLoader) {
        if (adapter != null) {
            adapter.showLoading(isShowLoader);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshList() {
        if (!list.isEmpty() && getViewModel().getDataManager().getCommercialStaff() != null) {
            list.remove(getViewModel().getDataManager().getCommercialStaff());
            adapter.notifyDataSetChanged();
        } else doSearch(search);
    }

    /*@Override
    public void onScannedDataRetrieve(CommercialStaffResponse.ContentBean content) {
        if (content != null) {
            List<VisitorProfileBean> visitorProfileBeanList = mViewModel.setClickVisitorDetail(content);
            VisitorProfileDialog.newInstance(visitorProfileBeanList, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                mViewModel.doCheckIn();
            }).setImage(content.getImageUrl()).setBtnLabel(getString(R.string.check_in)).show(getFragmentManager());
        }
    }*/
}
