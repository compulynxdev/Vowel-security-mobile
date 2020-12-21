package com.evisitor.ui.main.home.total;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityTotalVisitorsBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.main.home.guest.expected.ExpectedGuestFragment;
import com.evisitor.ui.main.home.housekeeping.expected.ExpectedHKFragment;
import com.evisitor.ui.main.home.sp.ExpectedSPFragment;

import java.util.ArrayList;
import java.util.List;

public class TotalVisitorsActivity extends BaseActivity<ActivityTotalVisitorsBinding, TotalVisitorsViewModel> implements BaseNavigator, View.OnClickListener {

    private ExpectedGuestFragment expectedGuestFragment;
    private ExpectedHKFragment expectedHKFragment;
    private ExpectedSPFragment expectedSPFragment;

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
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(TotalVisitorsViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
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

        if (getViewModel().getDataManager().isCommercial())
            getViewDataBinding().titleHouse.setText(getString(R.string.title_office_staff));
    }

    private void setUpPagerAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        expectedGuestFragment = ExpectedGuestFragment.newInstance();
        adapter.addFragment(expectedGuestFragment);
        expectedHKFragment = ExpectedHKFragment.newInstance();
        adapter.addFragment(expectedHKFragment);
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
                } else if (position == 1) {
                    getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.black));
                    getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.black));
                } else {
                    getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.black));
                    getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.black));
                    getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.img_search:
                hideKeyboard();
                getViewDataBinding().customSearchView.llSearchBar.setVisibility(getViewDataBinding().customSearchView.llSearchBar.getVisibility() == View.GONE
                        ? View.VISIBLE : View.GONE);

                getViewDataBinding().customSearchView.searchView.setQuery("", false);
                break;

            case R.id.title_guest:
                getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.colorPrimary));
                getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.black));
                getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.black));
                getViewDataBinding().viewPager.setCurrentItem(0);
                break;

            case R.id.title_house:
                getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.black));
                getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.black));
                getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.colorPrimary));
                getViewDataBinding().viewPager.setCurrentItem(1);
                break;

            case R.id.title_service:
                getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.black));
                getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.colorPrimary));
                getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.black));
                getViewDataBinding().viewPager.setCurrentItem(2);
                break;
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
                            expectedGuestFragment.setSearch(txt);
                            break;

                        case 1:
                            expectedHKFragment.setSearch(txt);
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


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();

        ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }
}
