package com.evisitor.ui.main.home.total;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class TotalVisitorsActivity extends BaseActivity<ActivityTotalVisitorsBinding,TotalVisitorsViewModel> implements BaseNavigator ,View.OnClickListener{

    private ExpectedSPFragment expectedSPFragment;
    private ExpectedHKFragment expectedHKFragment;

    public static Intent getStartIntent(Context context) {
        return new Intent(context,TotalVisitorsActivity.class);
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
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_expected_visitor);

        ImageView imgSearch = findViewById(R.id.img_search);
        imgSearch.setVisibility(View.VISIBLE);
        imgSearch.setOnClickListener(this);
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
        getViewDataBinding().titleGuest.setOnClickListener(this);
        getViewDataBinding().titleService.setOnClickListener(this);
        getViewDataBinding().titleHouse.setOnClickListener(this);
        setUpSearch();
        setUpPagerAdapter();
    }
    private void setUpPagerAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ExpectedGuestFragment.newInstance(getViewDataBinding().etSearch));
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
                if (position == 0) {
                    getViewDataBinding().etSearch.setText("");
                    getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.black));
                    getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.black));
                } else if (position==1){
                    getViewDataBinding().etSearch.setText("");
                    getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.black));
                    getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.black));
                }else {
                    getViewDataBinding().etSearch.setText("");
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
        switch (v.getId()){

            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.img_search :
                hideKeyboard();
                getViewDataBinding().searchBar.setVisibility(getViewDataBinding().searchBar.getVisibility() == View.GONE
                        ? View.VISIBLE : View.GONE);

                if (!getViewDataBinding().etSearch.getText().toString().trim().isEmpty()) {
                    getViewDataBinding().etSearch.setText("");
                }
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
                expectedSPFragment.setSearch(getViewDataBinding().etSearch.getText().toString());
                getViewDataBinding(). titleGuest.setTextColor(getResources().getColor(R.color.black));
                getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.colorPrimary));
                getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.black));
                getViewDataBinding().viewPager.setCurrentItem(2);
                break;
        }
    }

    private void setUpSearch() {
        getViewDataBinding().etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty() || s.toString().length() >= 2) {
                    expectedSPFragment.setSearch(s.toString());
                    expectedHKFragment.setSearch(s.toString());
                }
            }
        });

        getViewDataBinding().etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                return true;
            }
            return false;
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

        void addFragment(Fragment fragment){
            fragmentList.add(fragment);
        }
    }
}
