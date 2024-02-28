package com.evisitor.ui.main.home.flag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.FlaggedVisitorResponse;
import com.evisitor.databinding.ActivityFlagVisitorBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.main.home.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class FlagVisitorActivity extends BaseActivity<ActivityFlagVisitorBinding, FlagVisitorViewModel> implements FlagVisitorNavigator {

    private List<FlaggedVisitorResponse.ContentBean> list;
    private RecyclerViewScrollListener scrollListener;
    private int page;
    private String search = "";
    private FlagVisitorAdapter adapter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, FlagVisitorActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_flag_visitor;
    }

    @Override
    public FlagVisitorViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(FlagVisitorViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        getViewDataBinding().header.tvTitle.setText(R.string.title_flagged_visitor);
        getViewDataBinding().header.imgBack.setVisibility(View.VISIBLE);
        getViewDataBinding().header.imgBack.setOnClickListener(v -> onBackPressed());
        setUpSearch();
        setUpAdapter();

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);
    }

    private void setUpAdapter() {
        list = new ArrayList<>();
        adapter = new FlagVisitorAdapter(list, pos -> VisitorProfileDialog.newInstance(mViewModel.getVisitorDetail(list.get(pos)), null).setImage(list.get(pos).getImageUrl()).setBtnVisible(false).show(getSupportFragmentManager()));
        adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);
        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                setAdapterLoading(true);
                page++;
                mViewModel.getData(page, search);
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);

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
        getViewDataBinding().customSearchView.searchView.setQueryHint(getString(R.string.search_data_flag));
        getViewDataBinding().customSearchView.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().isEmpty() || newText.trim().length() >= 3) {
                    search = newText;
                    doSearch(newText);
                }
                return false;
            }
        });
    }

    private void updateUI() {
        getViewDataBinding().swipeToRefresh.setRefreshing(true);
        doSearch(search);
    }

    @Override
    public void hideSwipeToRefresh() {
        setAdapterLoading(false);
        getViewDataBinding().recyclerView.getRecycledViewPool().clear();
        getViewDataBinding().swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void onSuccessFlagList(List<FlaggedVisitorResponse.ContentBean> beans) {
        if (this.page == 0) this.list.clear();

        this.list.addAll(beans);
        adapter.notifyDataSetChanged();

        if (this.list.size() == 0) {
            getViewDataBinding().recyclerView.setVisibility(View.GONE);
            getViewDataBinding().tvNoData.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().tvNoData.setVisibility(View.GONE);
            getViewDataBinding().recyclerView.setVisibility(View.VISIBLE);
        }

    }

    private void doSearch(String search) {
        if (scrollListener != null) {
            scrollListener.onDataCleared();
        }
        this.list.clear();
        this.page = 0;
        if (isNetworkConnected(true))
            mViewModel.getData(page, search);
        else getViewDataBinding().swipeToRefresh.setRefreshing(false);
    }

    private void setAdapterLoading(boolean isShowLoader) {
        if (adapter != null) {
            adapter.showLoading(isShowLoader);
            adapter.notifyDataSetChanged();
        }
    }
}
