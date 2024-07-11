package com.evisitor.ui.main.commercial.staff;

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
import com.evisitor.ui.main.commercial.staff.expected.ExpectedCommercialStaffFragment;
import com.evisitor.ui.main.commercial.staff.registered.RegisteredCommercialStaffFragment;
import com.evisitor.util.ViewPagerAdapter;

public class CommercialStaffActivity extends BaseActivity<ActivityHkBinding, CommercialStaffViewModel> implements View.OnClickListener {

    private ExpectedCommercialStaffFragment expectedHKFragment;
    private RegisteredCommercialStaffFragment registeredHKFragment;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, CommercialStaffActivity.class);
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
    public CommercialStaffViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(CommercialStaffViewModel.class);
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

        /*getViewDataBinding().header.img2.setVisibility(View.VISIBLE);
        getViewDataBinding().header.img2.setOnClickListener(v -> {
            if (PermissionUtils.checkCameraPermission(this)) {
                Intent i = BarcodeScanActivity.getStartIntent(this);
                startActivityForResult(i, SCAN_RESULT);
            }
        });*/


        getViewDataBinding().tvRegistered.setOnClickListener(this);
        getViewDataBinding().tvExpected.setOnClickListener(this);
    }

    private void setUpPagerAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        expectedHKFragment = ExpectedCommercialStaffFragment.newInstance();
        adapter.addFragment(expectedHKFragment);
        registeredHKFragment = RegisteredCommercialStaffFragment.newInstance();
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
        getViewDataBinding().customSearchView.searchView.setQueryHint(getString(R.string.search_commercial_staff_data, getViewModel().getDataManager().getLevelName()));
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
        int id = v.getId();

        if (id == R.id.tv_expected) {
            getViewDataBinding().viewPager.setCurrentItem(0, true);
        }else if (id == R.id.tv_registered) {
            getViewDataBinding().viewPager.setCurrentItem(1, true);
        }else if (id == R.id.img_search) {
            hideKeyboard();
            getViewDataBinding().customSearchView.llSearchBar.setVisibility(getViewDataBinding().customSearchView.llSearchBar.getVisibility() == View.GONE
                    ? View.VISIBLE : View.GONE);

            getViewDataBinding().customSearchView.searchView.setQuery("", false);
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SCAN_RESULT && data != null) {
                String barcodeData = data.getStringExtra("data");
                if (barcodeData != null && !barcodeData.isEmpty()) {
                    getViewDataBinding().viewPager.setCurrentItem(0, true);
                    expectedHKFragment.scanQRId(barcodeData.concat(".png"));
                } else {
                    showAlert(R.string.alert, R.string.blank);
                }
            }
        }
    }*/
}
