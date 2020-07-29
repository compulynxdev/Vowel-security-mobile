package com.evisitor.ui.main.home.guest.expected;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.Guests;
import com.evisitor.databinding.ActivityExpectedGuestBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.main.home.guest.add.AddGuestActivity;
import com.evisitor.util.AppConstants;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class ExpectedGuestActivity extends BaseActivity<ActivityExpectedGuestBinding,GuestViewModel> implements GuestNavigator {

    private boolean viewSearch = false;
    private List<Guests> guestsList;
    private RecyclerViewScrollListener scrollListener;
    private GuestAdapter adapter;
    private int page = 0;

    public static Intent getStartIntent(Context context){
        return new Intent(context,ExpectedGuestActivity.class);
    }
    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_expected_guest;
    }

    @Override
    public GuestViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(GuestViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_expected_guests );
        setUpSearch();
        setUpAdapter();

        getViewDataBinding().fabAdd.setOnClickListener(v -> {
            Intent i = AddGuestActivity.getStartIntent(this);
            startActivity(i);
        });
    }

    private void setUpAdapter() {
        guestsList = new ArrayList<>();
        adapter = new GuestAdapter(guestsList,this);
        getViewDataBinding().recyclerView.setAdapter(adapter);

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(() -> {
            doSearch(getViewDataBinding().etSearch.getText().toString());
            getViewDataBinding().swipeToRefresh.setRefreshing(false);
        });
        getData(page,"");
        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                adapter.showLoading(true);
                adapter.notifyDataSetChanged();
                page += AppConstants.LIMIT;
                getData(page, getViewDataBinding().etSearch.getText().toString());
            }
        };
        getViewDataBinding().recyclerView.setOnScrollListener(scrollListener);
    }

    private void setUpSearch() {
        ImageView imgSearch = findViewById(R.id.img_search);
        imgSearch.setVisibility(View.VISIBLE);
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(v -> onBackPressed());
        imgSearch.setOnClickListener(v -> {
            if (!viewSearch) {
                getViewDataBinding().searchBar.setVisibility(View.VISIBLE);
                getViewDataBinding().etSearch.setText("");
                viewSearch = true;
            }
            else{
                getViewDataBinding().searchBar.setVisibility(View.GONE);
                getViewDataBinding().etSearch.setText("");
                viewSearch = false;
            }
        });

        getViewDataBinding().etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    doSearch("");
                else {
                    doSearch(s.toString());
                }
            }
        });
    }

    private void getData(int page, String search) {
        getViewModel().getGuestListData(page,search).observe(this, guests -> {
            adapter.showLoading(false);
            adapter.notifyDataSetChanged();

            guestsList.addAll(guests);
            adapter.notifyDataSetChanged();
        });

    }
    private void doSearch(String search) {
        scrollListener.onDataCleared();
        guestsList.clear();
        this.page = 0;
        getData(0,search);
    }
}
