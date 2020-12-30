package com.evisitor.ui.main.commercial.visitor.staff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityHkBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.main.commercial.visitor.staff.expected.ExpectedOfficeStaffFragment;
import com.evisitor.ui.main.commercial.visitor.staff.registered.RegisteredOfficeStaffFragment;
import com.evisitor.util.ViewPagerAdapter;

public class OfficeStaffActivity extends BaseActivity<ActivityHkBinding, OfficeStaffViewModel> implements View.OnClickListener {

    private ExpectedOfficeStaffFragment expectedHKFragment;
    private RegisteredOfficeStaffFragment registeredHKFragment;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, OfficeStaffActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_hk;
    }

    @Override
    public OfficeStaffViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(OfficeStaffViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        initView();
        setUpSearch();

        setUpPagerAdapter();
    }

    private void initView() {
        getViewDataBinding().header.tvTitle.setText(R.string.title_staff);
        getViewDataBinding().header.imgBack.setVisibility(View.VISIBLE);
        getViewDataBinding().header.imgBack.setOnClickListener(v -> onBackPressed());

        getViewDataBinding().tvRegistered.setOnClickListener(this);
        getViewDataBinding().tvExpected.setOnClickListener(this);
    }

    private void setUpPagerAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        expectedHKFragment = ExpectedOfficeStaffFragment.newInstance();
        adapter.addFragment(expectedHKFragment);
        registeredHKFragment = RegisteredOfficeStaffFragment.newInstance();
        adapter.addFragment(registeredHKFragment);
        getViewDataBinding().viewPager.setOffscreenPageLimit(2);

        getViewDataBinding().viewPager.setAdapter(adapter);
        getViewDataBinding().viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getViewDataBinding().customSearchView.searchView.setQuery("", false);

                if (position == 0) {
                    getViewDataBinding().tvExpected.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    getViewDataBinding().tvRegistered.setTextColor(getResources().getColor(R.color.black));
                } else {
                    getViewDataBinding().tvExpected.setTextColor(getResources().getColor(R.color.black));
                    getViewDataBinding().tvRegistered.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUpSearch() {
        getViewDataBinding().header.imgSearch.setVisibility(View.VISIBLE);
        getViewDataBinding().header.imgSearch.setOnClickListener(this);

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
                    if (getViewDataBinding().viewPager.getCurrentItem() == 0)
                        expectedHKFragment.setSearch(txt);
                    else
                        registeredHKFragment.setSearch(txt);
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_expected:
                getViewDataBinding().viewPager.setCurrentItem(0, true);
                break;

            case R.id.tv_registered:
                getViewDataBinding().viewPager.setCurrentItem(1, true);
                break;

            case R.id.img_search:
                hideKeyboard();
                getViewDataBinding().customSearchView.llSearchBar.setVisibility(getViewDataBinding().customSearchView.llSearchBar.getVisibility() == View.GONE
                        ? View.VISIBLE : View.GONE);

                getViewDataBinding().customSearchView.searchView.setQuery("", false);
                break;
        }
    }
}
