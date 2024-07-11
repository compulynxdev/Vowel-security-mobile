package com.evisitor.ui.main.home.trespasser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityTrespasserBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.main.home.trespasser.guests.TrespasserGuestFragment;
import com.evisitor.ui.main.home.trespasser.services.TrespasserSPFragment;
import com.evisitor.util.ViewPagerAdapter;

public class TrespasserActivity extends BaseActivity<ActivityTrespasserBinding, TrespasserViewModel> implements BaseNavigator, View.OnClickListener {

    private TrespasserSPFragment trespasserSPFragment;
    private TrespasserGuestFragment trespasserGuestFragment;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, TrespasserActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_trespasser;
    }

    @Override
    public TrespasserViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(TrespasserViewModel.class);
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
        getViewDataBinding().header.tvTitle.setText(R.string.title_trespasser_visitor);
        getViewDataBinding().header.imgBack.setVisibility(View.VISIBLE);
        getViewDataBinding().header.imgBack.setOnClickListener(v -> onBackPressed());

        getViewDataBinding().fabNotify.setOnClickListener(v -> getViewModel().doEnableDeviceNotification());
        getViewModel().getIsNotificationEnable().observe(this, aBoolean -> {
            if(aBoolean){
                getViewDataBinding().fabNotify.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_notification_on));
            }else{
                getViewDataBinding().fabNotify.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_disable_alarm));
            }
        });
        getViewModel().doGetDeviceNotificationStatus();

        getViewDataBinding().llTab.setVisibility(mViewModel.getDataManager().isCommercial() ? View.GONE : View.VISIBLE);
        getViewDataBinding().tvService.setOnClickListener(this);
        getViewDataBinding().tvGuest.setOnClickListener(this);
    }

    private void setUpPagerAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        trespasserGuestFragment = TrespasserGuestFragment.newInstance();
        adapter.addFragment(trespasserGuestFragment);

        if (!mViewModel.getDataManager().isCommercial()) {
            trespasserSPFragment = TrespasserSPFragment.newInstance();
            adapter.addFragment(trespasserSPFragment);
        }

        getViewDataBinding().viewPager.setOffscreenPageLimit(adapter.getCount());

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
                    getViewDataBinding().tvService.setTextColor(getResources().getColor(R.color.black));
                } else {
                    getViewDataBinding().tvGuest.setTextColor(getResources().getColor(R.color.black));
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
        if (getViewModel().getDataManager().isCommercial())
            getViewDataBinding().customSearchView.searchView.setQueryHint(getString(R.string.search_commercial_data_trespasser, ",".concat(getViewModel().getDataManager().getLevelName())));
        else
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
                        trespasserGuestFragment.setSearch(newText);
                    else
                        trespasserSPFragment.setSearch(newText);
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.tv_guest) {
            getViewDataBinding().viewPager.setCurrentItem(0, true);
        } else if (id == R.id.tv_service) {
            getViewDataBinding().viewPager.setCurrentItem(1, true);
        } else if (id == R.id.img_search) {
            hideKeyboard();
            if (getViewDataBinding().customSearchView.llSearchBar.getVisibility() == View.GONE) {
                getViewDataBinding().customSearchView.llSearchBar.setVisibility(View.VISIBLE);
            } else {
                getViewDataBinding().customSearchView.llSearchBar.setVisibility(View.GONE);
            }
            getViewDataBinding().customSearchView.searchView.setQuery("", false);
        }
    }
}
