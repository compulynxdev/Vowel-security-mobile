package com.evisitor.ui.main.home.blacklist;

import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

        ImageView imgSearch = findViewById(R.id.img_search);
        imgSearch.setVisibility(View.VISIBLE);
        imgSearch.setOnClickListener(v -> {
            hideKeyboard();
            getViewDataBinding().searchBar.setVisibility(getViewDataBinding().searchBar.getVisibility() == View.GONE
                    ? View.VISIBLE : View.GONE);

            if (!getViewDataBinding().etSearch.getText().toString().trim().isEmpty()) {
                getViewDataBinding().etSearch.setText("");
            }
        });

        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(v -> onBackPressed());

        setUpSearch();
        setUpAdapter();

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                adapter.showLoading(true);
                adapter.notifyDataSetChanged();
                page++;
                doSearch(getViewDataBinding().etSearch.getText().toString().trim());
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);
        updateUI();

        getViewModel().getBlackListData().observe(this, contentBeans -> {
            adapter.showLoading(false);
            adapter.notifyDataSetChanged();

            if (page==0) this.list.clear();

            this.list.addAll(contentBeans);
            adapter.notifyDataSetChanged();
        });
    }

    private void setUpAdapter() {
        list = new ArrayList<>();
        adapter = new BlackListAdapter(list);
        getViewDataBinding().recyclerView.setAdapter(adapter);
    }

    private void setUpSearch() {
        getViewDataBinding().etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty() || s.toString().length() >= 2) {
                    doSearch(s.toString());
                }
            }
        });

        getViewDataBinding().etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                return true;
            }
            return false;
        });
    }

    private void updateUI() {
        getViewDataBinding().swipeToRefresh.setRefreshing(true);
        doSearch(getViewDataBinding().etSearch.getText().toString());
    }

    @Override
    public void hideSwipeToRefresh() {
        getViewDataBinding().swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void refreshList() {
        doSearch(getViewDataBinding().etSearch.getText().toString());
    }

    private void doSearch(String search) {
        scrollListener.onDataCleared();
        this.list.clear();
        this.page = 0;
        getViewModel().getData(page,search);
    }
}
