package com.evisitor.ui.main.home.rejected.sp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import android.view.View;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.databinding.FragmentRejectedSBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.main.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

        public class RejectedSPFragment extends BaseFragment<FragmentRejectedSBinding,RejectedSPViewModel> implements RejectedSPNavigator {
        private RecyclerViewScrollListener scrollListener;
        private RejectedSPAdapter adapter;
        private String search = "";
        private int page = 0;

        private List<ServiceProvider> list;

        public static RejectedSPFragment newInstance() {
            RejectedSPFragment fragment = new RejectedSPFragment();
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
                return R.layout.fragment_rejected_s;
                }

        @Override
        public RejectedSPViewModel getViewModel() {
                return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(RejectedSPViewModel.class);
                }

        @Override
        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);
                getViewModel().setNavigator(this);
                setUpAdapter();
                }


        private void setUpAdapter() {
                list = new ArrayList<>();
                adapter = new RejectedSPAdapter(list, pos -> VisitorProfileDialog.newInstance(getViewModel().getVisitorDetail(list.get(pos)), null).setImage(list.get(pos).getImageUrl()).setBtnVisible(false).show(getChildFragmentManager()));
                adapter.setHasStableIds(true);
                getViewDataBinding().recyclerView.setAdapter(adapter);

                scrollListener = new RecyclerViewScrollListener() {
        @Override
        public void onLoadMore() {
                setAdapterLoading(true);
                page++;

                getViewModel().getSP(page, search);
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
                getViewModel().getSP(page, search.trim());
                }


        @Override
        public void onSuccess(List<ServiceProvider> beans) {
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

