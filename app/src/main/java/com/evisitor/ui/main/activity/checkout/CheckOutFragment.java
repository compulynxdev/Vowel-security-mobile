package com.evisitor.ui.main.activity.checkout;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.CheckInOut;
import com.evisitor.databinding.FragmentCheckOutBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.util.CalenderUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class CheckOutFragment extends BaseFragment<FragmentCheckOutBinding,CheckOutViewModel> implements BaseNavigator {

    private List<CheckInOut> list;
    private CheckOutAdpter adapter;
    private static TabLayout tabLayout;
    private static EditText etSearch;
    private int listOf=0;

    public static CheckOutFragment newInstance(EditText et_Search, TabLayout tab_Layout) {
        CheckOutFragment fragment = new CheckOutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        etSearch = et_Search;
        tabLayout = tab_Layout;
        return fragment;
    }


    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_check_out;
    }

    @Override
    public CheckOutViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(CheckOutViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().setNavigator(this);

        list=new ArrayList<>();
        CheckInOut obj = new CheckInOut();
        obj.setName("Suresh");
        obj.setHouseNo("A 401");
        obj.setHost("Rahul");
        obj.setTime(CalenderUtils.getCurrentTime());
        obj.setVisitorType("Service Provider");
        list.add(obj);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    //guest
                    case 0:
                        listOf = 0;
                        break;
                    //house keeping
                    case 1:
                        listOf=1;
                        break;
                    //service provider
                    case 2 :
                        listOf = 2;
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        adapter = new CheckOutAdpter(list,getBaseActivity());
        getViewDataBinding().recyclerView.setAdapter(adapter);
        getViewDataBinding().swipeToRefresh.setOnRefreshListener(() -> {
            adapter.notifyDataSetChanged();
            getViewDataBinding().swipeToRefresh.setRefreshing(false);
        });
    }
}
