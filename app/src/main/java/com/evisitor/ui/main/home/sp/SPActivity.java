package com.evisitor.ui.main.home.sp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivitySpBinding;
import com.evisitor.ui.base.BaseActivity;

public class SPActivity extends BaseActivity<ActivitySpBinding, SPViewModel> {

    private ExpectedSPFragment fragment;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SPActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sp;
    }

    @Override
    public SPViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(SPViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        getViewDataBinding().header.tvTitle.setText(R.string.title_service_provider);
        getViewDataBinding().header.imgBack.setVisibility(View.VISIBLE);
        getViewDataBinding().header.imgBack.setOnClickListener(v -> onBackPressed());
        setUpSearch();

        fragment = ExpectedSPFragment.newInstance();
        replaceFragment(fragment,R.id.frame_layout);
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
                    fragment.setSearch(txt);
                }
                return false;
            }
        });
    }
}
