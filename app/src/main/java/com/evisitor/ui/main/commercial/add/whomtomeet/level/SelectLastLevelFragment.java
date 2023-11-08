package com.evisitor.ui.main.commercial.add.whomtomeet.level;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.BR;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.HouseDetailBean;
import com.evisitor.databinding.FragmentWhomToMeetBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.main.commercial.add.whomtomeet.WhomToMeetCallback;

import java.util.ArrayList;
import java.util.List;

public class SelectLastLevelFragment extends BaseFragment<FragmentWhomToMeetBinding, SelectLastLevelViewModel> implements SelectLastLevelNavigator {

    private SelectLastLevelAdapter adapter;
    private List<HouseDetailBean> list;
    private WhomToMeetCallback callback;

    public static SelectLastLevelFragment newInstance(WhomToMeetCallback callback) {
        SelectLastLevelFragment fragment = new SelectLastLevelFragment();
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
            mViewModel.doGetLastLevelData();
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
    public SelectLastLevelViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(SelectLastLevelViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().tvNoData.setText(getString(R.string.no_last_level, mViewModel.getDataManager().getLevelName()));
        setUpAdapter();
        mViewModel.doGetLastLevelData();
    }


    private void setUpAdapter() {
        list = new ArrayList<>();
        adapter = new SelectLastLevelAdapter(list, pos -> {
            if (callback != null) callback.onLastLevelClick(list.get(pos));
        });
        getViewDataBinding().recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLastLevelDataReceived(List<HouseDetailBean> lastLevelDataList) {
        this.list.clear();
        list.addAll(lastLevelDataList);
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
