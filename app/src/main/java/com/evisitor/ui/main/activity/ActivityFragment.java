package com.evisitor.ui.main.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.FragmentActivityBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.main.activity.checkin.CheckInFragment;
import com.evisitor.ui.main.activity.checkout.CheckOutFragment;
import com.evisitor.util.ViewPagerAdapter;

public class ActivityFragment extends BaseFragment<FragmentActivityBinding, ActivityViewModel> implements BaseNavigator, View.OnClickListener {
    private int listOf;
    private CheckInFragment checkInFragment;
    private CheckOutFragment checkOutFragment;

    public static ActivityFragment newInstance() {
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_activity;
    }

    @Override
    public ActivityViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(ActivityViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        setupSearch();
        setUpPagerAdapter();
    }

    private void setupSearch() {
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
                        checkInFragment.doSearch(txt);
                    else checkOutFragment.doSearch(txt);
                }
                return false;
            }
        });
    }

    private void initView() {
        getViewDataBinding().header.tvTitle.setText(R.string.title_activity);
        getViewDataBinding().tvIn.setOnClickListener(this);
        getViewDataBinding().tvIn.setText(getString(R.string.check_in_with_count, "0"));
        getViewDataBinding().tvOut.setText(getString(R.string.check_out_with_count, "0"));
        getViewDataBinding().tvOut.setOnClickListener(this);
        //guest
        listOf = 0;
        getViewDataBinding().titleGuest.setOnClickListener(this);
        getViewDataBinding().titleHouse.setOnClickListener(this);
        getViewDataBinding().titleService.setOnClickListener(this);

        getViewDataBinding().titleGuest.setText(mViewModel.getDataManager().isCommercial() ? R.string.title_visitor : R.string.title_guests);
        getViewDataBinding().titleHouse.setText(mViewModel.getDataManager().isCommercial() ? R.string.title_staff : R.string.title_domestic_staff);
    }

    private void setUpPagerAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        checkInFragment = CheckInFragment.newInstance(listOf, size -> getViewDataBinding().tvIn.setText(getString(R.string.check_in_with_count, String.valueOf(size))));
        adapter.addFragment(checkInFragment);
        checkOutFragment = CheckOutFragment.newInstance(listOf, size -> getViewDataBinding().tvOut.setText(getString(R.string.check_out_with_count, String.valueOf(size))));
        adapter.addFragment(checkOutFragment);
        getViewDataBinding().viewPager.setOffscreenPageLimit(2);
        getViewDataBinding().viewPager.setAdapter(adapter);
        getViewDataBinding().viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getViewDataBinding().customSearchView.searchView.setQuery("", true);

                if (position == 0) {
                    checkInFragment.doSearch("");
                    getViewDataBinding().tvIn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    getViewDataBinding().tvOut.setTextColor(getResources().getColor(R.color.black));
                } else {
                    checkOutFragment.doSearch("");
                    getViewDataBinding().tvIn.setTextColor(getResources().getColor(R.color.black));
                    getViewDataBinding().tvOut.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
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

        if (id == R.id.tv_in) {
            getViewDataBinding().viewPager.setCurrentItem(0, true);
        } else if (id == R.id.tv_out) {
            getViewDataBinding().viewPager.setCurrentItem(1, true);
        } else if (id == R.id.img_search) {
            hideKeyboard();
            getViewDataBinding().customSearchView.llSearchBar.setVisibility(
                    getViewDataBinding().customSearchView.llSearchBar.getVisibility() == View.GONE
                            ? View.VISIBLE : View.GONE
            );
            getViewDataBinding().customSearchView.searchView.setQuery("", false);
        } else if (id == R.id.title_guest) {
            listOf = 0;
            checkInFragment.setCheckInList(listOf);
            checkOutFragment.setCheckOutList(listOf);
            getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.colorPrimary));
            getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.black));
            getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.black));
            if (getViewModel().getDataManager().isCommercial()) {
                getViewDataBinding().customSearchView.searchView.setQueryHint(
                        getString(R.string.search_commercial_visitor_data, getViewModel().getDataManager().getLevelName())
                );
            }
        } else if (id == R.id.title_house) {
            listOf = 1;
            checkInFragment.setCheckInList(listOf);
            checkOutFragment.setCheckOutList(listOf);
            getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.black));
            getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.black));
            getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.colorPrimary));
            if (getViewModel().getDataManager().isCommercial()) {
                getViewDataBinding().customSearchView.searchView.setQueryHint(
                        getString(R.string.search_commercial_staff_data, getViewModel().getDataManager().getLevelName())
                );
            }
        } else if (id == R.id.title_service) {
            listOf = 2;
            checkInFragment.setCheckInList(listOf);
            checkOutFragment.setCheckOutList(listOf);
            getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.black));
            getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.colorPrimary));
            getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.black));
            if (getViewModel().getDataManager().isCommercial()) {
                getViewDataBinding().customSearchView.searchView.setQueryHint(
                        getString(R.string.search_commercial_sp_data)
                );
            }
        }
    }
}
