package com.evisitor.ui.main.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.FragmentHomeBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.base.BaseNavigator;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> implements BaseNavigator {

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        return R.layout.fragment_home;
    }

    @Override
    public HomeViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(HomeViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().setNavigator(this);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_home);
    }
}
