package com.evisitor.ui.main.home.total;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityTotalVisitorsBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.main.commercial.staff.expected.ExpectedCommercialStaffFragment;
import com.evisitor.ui.main.commercial.visitor.expected.ExpectedCommercialGuestFragment;
import com.evisitor.ui.main.residential.guest.expected.ExpectedGuestFragment;
import com.evisitor.ui.main.residential.sp.ExpectedSPFragment;
import com.evisitor.ui.main.residential.staff.expected.ExpectedHKFragment;
import com.evisitor.util.ViewPagerAdapter;

public class TotalVisitorsActivity extends BaseActivity<ActivityTotalVisitorsBinding, TotalVisitorsViewModel> implements BaseNavigator, View.OnClickListener {

    private ExpectedGuestFragment expectedGuestFragment;
    private ExpectedCommercialGuestFragment commercialGuestFragment;
    private ExpectedHKFragment expectedHKFragment;
    private ExpectedSPFragment expectedSPFragment;
    private ExpectedCommercialStaffFragment officeStaffFragment;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, TotalVisitorsActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_total_visitors;
    }

    @Override
    public TotalVisitorsViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(TotalVisitorsViewModel.class);
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
        getViewDataBinding().header.tvTitle.setText(R.string.title_expected_visitor);
        getViewDataBinding().header.imgBack.setVisibility(View.VISIBLE);
        getViewDataBinding().header.imgBack.setOnClickListener(this);
        getViewDataBinding().titleGuest.setOnClickListener(this);
        getViewDataBinding().titleService.setOnClickListener(this);
        getViewDataBinding().titleHouse.setOnClickListener(this);

        getViewDataBinding().titleGuest.setText(mViewModel.getDataManager().isCommercial() ? R.string.title_visitor : R.string.title_guests);
        getViewDataBinding().titleHouse.setText(mViewModel.getDataManager().isCommercial() ? R.string.title_staff : R.string.title_domestic_staff);
    }

    private void setUpPagerAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (mViewModel.getDataManager().isCommercial()) {
            commercialGuestFragment = ExpectedCommercialGuestFragment.newInstance();
            adapter.addFragment(commercialGuestFragment);
            officeStaffFragment = ExpectedCommercialStaffFragment.newInstance();
            adapter.addFragment(officeStaffFragment);
        } else {
            expectedGuestFragment = ExpectedGuestFragment.newInstance();
            adapter.addFragment(expectedGuestFragment);
            expectedHKFragment = ExpectedHKFragment.newInstance();
            adapter.addFragment(expectedHKFragment);
        }
        expectedSPFragment = ExpectedSPFragment.newInstance();
        adapter.addFragment(expectedSPFragment);

        getViewDataBinding().viewPager.setOffscreenPageLimit(3);

        getViewDataBinding().viewPager.setAdapter(adapter);
        getViewDataBinding().viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getViewDataBinding().customSearchView.searchView.setQuery("", false);
                if (position == 0) {
                    getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.black));
                    getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.black));
                    if (getViewModel().getDataManager().isCommercial())
                        getViewDataBinding().customSearchView.searchView.setQueryHint(getString(R.string.search_commercial_visitor_data, getViewModel().getDataManager().getLevelName()));

                } else if (position == 1) {
                    getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.black));
                    getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.black));
                    if (getViewModel().getDataManager().isCommercial())
                        getViewDataBinding().customSearchView.searchView.setQueryHint(getString(R.string.search_commercial_staff_data, getViewModel().getDataManager().getLevelName()));

                } else {
                    getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.black));
                    getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.black));
                    getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    if (getViewModel().getDataManager().isCommercial())
                        getViewDataBinding().customSearchView.searchView.setQueryHint(getString(R.string.search_commercial_sp_data));


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.img_back) {
            onBackPressed();
        } else if (id == R.id.img_search) {
            hideKeyboard();
            if (getViewDataBinding().customSearchView.llSearchBar.getVisibility() == View.GONE) {
                getViewDataBinding().customSearchView.llSearchBar.setVisibility(View.VISIBLE);
            } else {
                getViewDataBinding().customSearchView.llSearchBar.setVisibility(View.GONE);
            }
            getViewDataBinding().customSearchView.searchView.setQuery("", false);
        } else if (id == R.id.title_guest) {
            getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.colorPrimary));
            getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.black));
            getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.black));
            if (getViewModel().getDataManager().isCommercial()) {
                getViewDataBinding().customSearchView.searchView.setQueryHint(getString(R.string.search_commercial_visitor_data, getViewModel().getDataManager().getLevelName()));
            }
            getViewDataBinding().viewPager.setCurrentItem(0);
        } else if (id == R.id.title_house) {
            getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.black));
            getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.black));
            getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.colorPrimary));
            if (getViewModel().getDataManager().isCommercial()) {
                getViewDataBinding().customSearchView.searchView.setQueryHint(getString(R.string.search_commercial_staff_data, getViewModel().getDataManager().getLevelName()));
            }
            getViewDataBinding().viewPager.setCurrentItem(1);
        } else if (id == R.id.title_service) {
            getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.black));
            getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.colorPrimary));
            getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.black));
            if (getViewModel().getDataManager().isCommercial()) {
                getViewDataBinding().customSearchView.searchView.setQueryHint(getString(R.string.search_commercial_sp_data));
            }
            getViewDataBinding().viewPager.setCurrentItem(2);
        }
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
                    switch (getViewDataBinding().viewPager.getCurrentItem()) {
                        case 0:
                            if (mViewModel.getDataManager().isCommercial()) {
                                commercialGuestFragment.setSearch(txt);
                            } else expectedGuestFragment.setSearch(txt);
                            break;

                        case 1:
                            if (mViewModel.getDataManager().isCommercial()) {
                                officeStaffFragment.setSearch(txt);
                            } else expectedHKFragment.setSearch(txt);

                            break;

                        case 2:
                            expectedSPFragment.setSearch(txt);
                            break;
                    }
                }
                return false;
            }
        });
    }
}
