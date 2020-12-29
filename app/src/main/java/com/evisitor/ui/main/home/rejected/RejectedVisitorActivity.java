package com.evisitor.ui.main.home.rejected;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityRejectedVisitorBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.main.home.rejected.guest.RejectedGuestFragment;
import com.evisitor.ui.main.home.rejected.sp.RejectedSPFragment;
import com.evisitor.ui.main.home.rejected.staff.RejectedStaffFragment;
import com.evisitor.util.ViewPagerAdapter;

public class RejectedVisitorActivity extends BaseActivity<ActivityRejectedVisitorBinding, RejectedVisitorViewModel> implements BaseNavigator, View.OnClickListener {

    private RejectedGuestFragment rejectedGuestFragment;
    private RejectedSPFragment rejectedSPFragment;
    private RejectedStaffFragment rejectedStaffFragment;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, RejectedVisitorActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_rejected_visitor;
    }

    @Override
    public RejectedVisitorViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(RejectedVisitorViewModel.class);
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
        getViewDataBinding().header.tvTitle.setText(R.string.title_rejected_visitor);
        getViewDataBinding().header.imgBack.setVisibility(View.VISIBLE);
        getViewDataBinding().header.imgBack.setOnClickListener(v -> onBackPressed());

        getViewDataBinding().tvService.setOnClickListener(this);
        getViewDataBinding().tvGuest.setOnClickListener(this);
        getViewDataBinding().tvHouse.setOnClickListener(this);

        getViewDataBinding().tvGuest.setText(mViewModel.getDataManager().isCommercial() ? R.string.title_visitor : R.string.title_guests);
        getViewDataBinding().tvHouse.setText(mViewModel.getDataManager().isCommercial() ? R.string.title_staff : R.string.title_domestic_staff);
    }

    private void setUpPagerAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        rejectedGuestFragment = RejectedGuestFragment.newInstance();
        adapter.addFragment(rejectedGuestFragment);
        rejectedStaffFragment = RejectedStaffFragment.newInstance();
        adapter.addFragment(rejectedStaffFragment);
        rejectedSPFragment = RejectedSPFragment.newInstance();
        adapter.addFragment(rejectedSPFragment);
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
                    getViewDataBinding().tvGuest.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    getViewDataBinding().tvHouse.setTextColor(getResources().getColor(R.color.black));
                    getViewDataBinding().tvService.setTextColor(getResources().getColor(R.color.black));
                } else if (position == 1) {
                    getViewDataBinding().tvGuest.setTextColor(getResources().getColor(R.color.black));
                    getViewDataBinding().tvHouse.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    getViewDataBinding().tvService.setTextColor(getResources().getColor(R.color.black));
                } else {
                    getViewDataBinding().tvGuest.setTextColor(getResources().getColor(R.color.black));
                    getViewDataBinding().tvHouse.setTextColor(getResources().getColor(R.color.black));
                    getViewDataBinding().tvService.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
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
        getViewDataBinding().customSearchView.searchView.setQueryHint(getString(R.string.search_data_trespasser));
        getViewDataBinding().customSearchView.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().isEmpty() || newText.trim().length() >= 3) {
                    if (getViewDataBinding().viewPager.getCurrentItem() == 0)
                        rejectedGuestFragment.setSearch(newText);
                    else if (getViewDataBinding().viewPager.getCurrentItem() == 1)
                        rejectedStaffFragment.setSearch(newText);
                    else rejectedSPFragment.setSearch(newText);
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_guest:
                getViewDataBinding().viewPager.setCurrentItem(0, true);
                break;

            case R.id.tv_house:
                getViewDataBinding().viewPager.setCurrentItem(1, true);
                break;

            case R.id.tv_service:
                getViewDataBinding().viewPager.setCurrentItem(2, true);
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
