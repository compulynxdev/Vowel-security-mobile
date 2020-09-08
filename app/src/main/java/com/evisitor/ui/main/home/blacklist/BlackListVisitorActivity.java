package com.evisitor.ui.main.home.blacklist;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.BlackListVisitorResponse;
import com.evisitor.databinding.ActivityBlackListVisitorBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.util.pagination.RecyclerViewScrollListener;
import java.util.ArrayList;
import java.util.List;

public class BlackListVisitorActivity extends BaseActivity<ActivityBlackListVisitorBinding,BlackListViewModel> implements BlackListNavigtor {

    private List<BlackListVisitorResponse.ContentBean> list;
    private RecyclerViewScrollListener scrollListener;
    private int page;
    private String search = "";
    private BlackListAdapter adapter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context,BlackListVisitorActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_black_list_visitor;
    }

    @Override
    public BlackListViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(BlackListViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_blacklisted_visitor);

        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(v -> onBackPressed());

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                setAdapterLoading(true);
                adapter.notifyDataSetChanged();
                page++;
                doSearch(search);
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);
        setUpSearch();
        setUpAdapter();

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);

        updateUI();


    }

    private void setUpAdapter() {
        list = new ArrayList<>();
        adapter = new BlackListAdapter(list);
        adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);
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
                if (newText.trim().isEmpty() || newText.trim().length() >= 3) {
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
        getViewDataBinding().swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void refreshList() {
        doSearch(search);
    }

    @Override
    public void OnSuccessBlackList(List<BlackListVisitorResponse.ContentBean> beans) {
            if (page==0) this.list.clear();

            this.list.addAll(beans);
            adapter.notifyDataSetChanged();
    }

    private void doSearch(String search) {
        scrollListener.onDataCleared();
        this.list.clear();
        this.page = 0;
        getViewModel().getData(page,search);
    }

    private void setAdapterLoading(boolean isShowLoader) {
        if (adapter != null) {
            adapter.showLoading(isShowLoader);
            adapter.notifyDataSetChanged();
        }
    }
}
