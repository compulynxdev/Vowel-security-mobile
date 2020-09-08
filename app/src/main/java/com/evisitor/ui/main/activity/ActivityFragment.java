package com.evisitor.ui.main.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
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

public class ActivityFragment extends BaseFragment<FragmentActivityBinding, ActivityViewModel> implements BaseNavigator,View.OnClickListener {
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
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_activity;
    }

    @Override
    public ActivityViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(ActivityViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().setNavigator(this);
        initView();
        setupSearch();
        setUpPagerAdapter();
    }

    private void setupSearch() {
        setupSearchSetting(getViewDataBinding().customSearchView.searchView);
        getViewDataBinding().header.imgSearch.setVisibility(View.VISIBLE);
        getViewDataBinding().header.imgSearch.setOnClickListener(this);

        getViewDataBinding().customSearchView.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().isEmpty() || newText.trim().length() >= 3) {
                    if (getViewDataBinding().viewPager.getCurrentItem() == 0)
                        checkInFragment.doSearch(newText);
                    else checkOutFragment.doSearch(newText);
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
    }

    private void setUpPagerAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        checkInFragment = CheckInFragment.newInstance(getViewDataBinding().customSearchView.searchView, listOf, size -> getViewDataBinding().tvIn.setText(getString(R.string.check_in_with_count, String.valueOf(size))));
        adapter.addFragment(checkInFragment);
        checkOutFragment = CheckOutFragment.newInstance(getViewDataBinding().customSearchView.searchView, listOf, size -> getViewDataBinding().tvOut.setText(getString(R.string.check_out_with_count, String.valueOf(size))));
        adapter.addFragment(checkOutFragment);
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
                    getViewDataBinding().tvIn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    getViewDataBinding().tvOut.setTextColor(getResources().getColor(R.color.black));
                } else {
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
        switch (v.getId()){
            case R.id.tv_in :
                getViewDataBinding().viewPager.setCurrentItem(0,true);
                break;

            case R.id.tv_out :
                getViewDataBinding().viewPager.setCurrentItem(1,true);
                break;

            case R.id.img_search :
                hideKeyboard();
                getViewDataBinding().customSearchView.llSearchBar.setVisibility(getViewDataBinding().customSearchView.llSearchBar.getVisibility() == View.GONE
                        ? View.VISIBLE : View.GONE);

                getViewDataBinding().customSearchView.searchView.setQuery("", false);
                break;

            case R.id.title_guest:
                listOf = 0;
                checkInFragment.setCheckInList(listOf);
                checkOutFragment.setCheckOutList(listOf);
                getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.colorPrimary));
                getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.black));
                getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.black));
                break;

            case R.id.title_house:
                listOf = 1;
                checkInFragment.setCheckInList(listOf);
                checkOutFragment.setCheckOutList(listOf);
                getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.black));
                getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.black));
                getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;

            case R.id.title_service:
                listOf = 2;
                checkInFragment.setCheckInList(listOf);
                checkOutFragment.setCheckOutList(listOf);
                getViewDataBinding(). titleGuest.setTextColor(getResources().getColor(R.color.black));
                getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.colorPrimary));
                getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.black));
                break;
        }
    }
}
