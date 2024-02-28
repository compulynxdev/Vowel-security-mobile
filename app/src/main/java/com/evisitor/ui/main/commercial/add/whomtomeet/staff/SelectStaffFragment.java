package com.evisitor.ui.main.commercial.add.whomtomeet.staff;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.BR;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.SelectCommercialStaffResponse;
import com.evisitor.databinding.FragmentWhomToMeetBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.main.commercial.add.whomtomeet.WhomToMeetCallback;

import java.util.ArrayList;
import java.util.List;

public class SelectStaffFragment extends BaseFragment<FragmentWhomToMeetBinding, SelectStaffViewModel> implements SelectStaffNavigator {

    private SelectStaffAdapter adapter;
    private List<SelectCommercialStaffResponse> list;
    private WhomToMeetCallback callback;

    public static SelectStaffFragment newInstance(WhomToMeetCallback callback) {
        SelectStaffFragment fragment = new SelectStaffFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setCallback(callback);
        return fragment;
    }

    public void setCallback(WhomToMeetCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
    }

    public synchronized void setSearch(String search) {
        if (search.isEmpty())
            mViewModel.getStaffListData();
        else mViewModel.getSearchData(search);
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_whom_to_meet;
    }

    @Override
    public SelectStaffViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(SelectStaffViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().tvNoData.setText(R.string.no_staff);
        setUpAdapter();
        mViewModel.getStaffListData();
    }


    private void setUpAdapter() {
        list = new ArrayList<>();
        adapter = new SelectStaffAdapter(list, pos -> {
            if (callback != null) callback.onStaffClick(list.get(pos));
        });
        getViewDataBinding().recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStaffDataReceived(List<SelectCommercialStaffResponse> staffList) {
        this.list.clear();
        list.addAll(staffList);
        adapter.notifyDataSetChanged();
        if (this.list.size() == 0) {
            getViewDataBinding().recyclerView.setVisibility(View.GONE);
            getViewDataBinding().tvNoData.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().tvNoData.setVisibility(View.GONE);
            getViewDataBinding().recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
