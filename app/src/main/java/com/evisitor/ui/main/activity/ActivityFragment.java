package com.evisitor.ui.main.activity;

import android.os.Bundle;
import android.view.View;
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
import com.evisitor.databinding.FragmentActivityBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.ui.main.activity.checkin.CheckInFragment;
import com.evisitor.ui.main.activity.checkout.CheckOutFragment;
import java.util.ArrayList;
import java.util.List;

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
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_activity);

        ImageView imgSearch = view.findViewById(R.id.img_search);
        imgSearch.setVisibility(View.VISIBLE);
        imgSearch.setOnClickListener(this);
        getViewDataBinding().tvIn.setOnClickListener(this);
        getViewDataBinding().tvIn.setText(getString(R.string.check_in_with_count, "0"));
        getViewDataBinding().tvOut.setText(getString(R.string.check_out_with_count, "0"));
        getViewDataBinding().tvOut.setOnClickListener(this);
        //guest
        listOf = 0;
        getViewDataBinding().titleGuest.setOnClickListener(this);
        getViewDataBinding().titleHouse.setOnClickListener(this);
        getViewDataBinding().titleService.setOnClickListener(this);

        setUpPagerAdapter();
    }

    private void setUpPagerAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        checkInFragment = CheckInFragment.newInstance(getViewDataBinding().etSearch, listOf, size -> getViewDataBinding().tvIn.setText(getString(R.string.check_in_with_count,String.valueOf(size))));
        adapter.addFragment(checkInFragment);
        checkOutFragment = CheckOutFragment.newInstance(getViewDataBinding().etSearch,listOf, size -> getViewDataBinding().tvOut.setText(getString(R.string.check_out_with_count,String.valueOf(size))));
        adapter.addFragment(checkOutFragment);
        getViewDataBinding().viewPager.setOffscreenPageLimit(2);

        getViewDataBinding().viewPager.setAdapter(adapter);
        getViewDataBinding().viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    getViewDataBinding().etSearch.setText("");
                    getViewDataBinding().tvIn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    getViewDataBinding().tvOut.setTextColor(getResources().getColor(R.color.black));
                } else {
                    getViewDataBinding().etSearch.setText("");
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
                getViewDataBinding().searchBar.setVisibility(getViewDataBinding().searchBar.getVisibility() == View.GONE
                        ? View.VISIBLE : View.GONE);

                if (!getViewDataBinding().etSearch.getText().toString().trim().isEmpty()) {
                    getViewDataBinding().etSearch.setText("");
                }
                break;

            case R.id.title_guest:
                listOf = 0;
                checkInFragment.setList(listOf);
                checkOutFragment.setList(listOf);
                getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.colorPrimary));
                getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.black));
                getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.black));
                break;

            case R.id.title_house:
                listOf = 1;
                checkInFragment.setList(listOf);
                checkOutFragment.setList(listOf);
                getViewDataBinding().titleGuest.setTextColor(getResources().getColor(R.color.black));
                getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.black));
                getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;

            case R.id.title_service:
                listOf = 2;
                checkInFragment.setList(listOf);
                checkOutFragment.setList(listOf);
                getViewDataBinding(). titleGuest.setTextColor(getResources().getColor(R.color.black));
                getViewDataBinding().titleService.setTextColor(getResources().getColor(R.color.colorPrimary));
                getViewDataBinding().titleHouse.setTextColor(getResources().getColor(R.color.black));
               break;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

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
