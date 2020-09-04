package com.evisitor.ui.main.home.housekeeping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityHkBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.main.home.housekeeping.expected.ExpectedHKFragment;
import com.evisitor.ui.main.home.housekeeping.registered.RegisteredHKFragment;
import com.evisitor.util.ViewPagerAdapter;

public class HouseKeepingActivity extends BaseActivity<ActivityHkBinding, HKViewModel> implements View.OnClickListener {

    private ExpectedHKFragment expectedHKFragment;
    private RegisteredHKFragment registeredHKFragment;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, HouseKeepingActivity.class);
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
    public HKViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(HKViewModel.class);
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
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_house_keeping);
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(v -> onBackPressed());

        getViewDataBinding().tvRegistered.setOnClickListener(this);
        getViewDataBinding().tvExpected.setOnClickListener(this);
    }

    private void setUpPagerAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        expectedHKFragment = ExpectedHKFragment.newInstance();
        adapter.addFragment(expectedHKFragment);
        registeredHKFragment = RegisteredHKFragment.newInstance();
        adapter.addFragment(registeredHKFragment);
        getViewDataBinding().viewPager.setOffscreenPageLimit(2);

        getViewDataBinding().viewPager.setAdapter(adapter);
        getViewDataBinding().viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getViewDataBinding().etSearch.setQuery("", false);

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
        ImageView imgSearch = findViewById(R.id.img_search);
        imgSearch.setVisibility(View.VISIBLE);
        imgSearch.setOnClickListener(this);

        getViewDataBinding().etSearch.setActivated(true);
        getViewDataBinding().etSearch.setQueryHint(getString(R.string.search_data));
        getViewDataBinding().etSearch.onActionViewExpanded();
        getViewDataBinding().etSearch.clearFocus();
        TextView searchText = getViewDataBinding().etSearch.findViewById(R.id.search_src_text);
        searchText.setTextSize(16);
        searchText.setTypeface(ResourcesCompat.getFont(this, R.font.futura_round_medium));

        getViewDataBinding().etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().isEmpty() || newText.trim().length() >= 3) {
                    if (getViewDataBinding().viewPager.getCurrentItem() == 0)
                        expectedHKFragment.setSearch(newText);
                    else
                        registeredHKFragment.setSearch(newText);
                }
                return false;
            }
        });

        getViewDataBinding().etSearch.setOnSearchClickListener(v -> hideKeyboard());

       /* getViewDataBinding().etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getViewDataBinding().etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                return true;
            }
            return false;
        });*/
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
                getViewDataBinding().searchBar.setVisibility(getViewDataBinding().searchBar.getVisibility() == View.GONE
                        ? View.VISIBLE : View.GONE);

                getViewDataBinding().etSearch.setQuery("", false);
                break;
        }
    }
}
