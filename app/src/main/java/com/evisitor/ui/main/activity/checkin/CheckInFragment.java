package com.evisitor.ui.main.activity.checkin;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.CheckInOut;
import com.evisitor.databinding.FragmentCheckInBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.base.BaseNavigator;
import com.evisitor.util.CalenderUtils;

import java.util.ArrayList;
import java.util.List;


public class CheckInFragment extends BaseFragment<FragmentCheckInBinding,CheckInViewModel> implements BaseNavigator {
    private List<CheckInOut> list;
    private CheckInAdpter adapter;
     public static CheckInFragment newInstance() {
        CheckInFragment fragment = new CheckInFragment();
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
        return R.layout.fragment_check_in;
    }

    @Override
    public CheckInViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(CheckInViewModel.class);
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
        adapter = new CheckInAdpter(list,getBaseActivity());
        getViewDataBinding().recyclerView.setAdapter(adapter);
        getViewDataBinding().swipeToRefresh.setOnRefreshListener(() -> {

            adapter.notifyDataSetChanged();
            getViewDataBinding().swipeToRefresh.setRefreshing(false);
        });
    }
}
