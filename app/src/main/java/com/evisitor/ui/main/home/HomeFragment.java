package com.evisitor.ui.main.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.HomeBean;
import com.evisitor.databinding.FragmentHomeBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.main.home.blacklist.BlackListVisitorActivity;
import com.evisitor.ui.main.home.flag.FlagVisitorActivity;
import com.evisitor.ui.main.home.guest.GuestActivity;
import com.evisitor.ui.main.home.housekeeping.HouseKeepingActivity;
import com.evisitor.ui.main.home.sp.SPActivity;
import com.evisitor.ui.main.home.total.TotalVisitorsActivity;
import com.evisitor.ui.main.home.trespasser.TrespasserActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {

    private List<HomeBean> homeList;
    private HomeFragmentInteraction interaction;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setInteraction(HomeFragmentInteraction interaction) {
        this.interaction = interaction;
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
        getViewDataBinding().toolbar.tvTitle.setText(R.string.title_home);

        setupAdapter();
        getViewModel().getNotificationCountData().observe(this, count -> {
            if (interaction != null)
                interaction.onReceiveNotificationCount(count);
        });
    }

    private void setupAdapter() {
        homeList = new ArrayList<>();
        HomeAdapter homeAdapter = new HomeAdapter(homeList, pos -> {
            switch (homeList.get(pos).getPos()) {
                case HomeViewModel.GUEST_VIEW:
                    startActivity(GuestActivity.getStartIntent(getContext()));
                    break;

                case HomeViewModel.HOUSE_KEEPING_VIEW:
                    startActivity(HouseKeepingActivity.getStartIntent(getContext()));
                    break;

                case HomeViewModel.SERVICE_PROVIDER_VIEW:
                    startActivity(SPActivity.getStartIntent(getContext()));
                    break;

                case HomeViewModel.TOTAL_VISITOR_VIEW:
                    startActivity(TotalVisitorsActivity.getStartIntent(getContext()));
                    break;

                case HomeViewModel.BLACKLISTED_VISITOR_VIEW:
                    startActivity(BlackListVisitorActivity.getStartIntent(getContext()));
                    break;

                case HomeViewModel.TRESPASSER_VIEW:
                    startActivity(TrespasserActivity.getStartIntent(getContext()));
                    break;

                case HomeViewModel.FLAGGED_VIEW:
                    startActivity(FlagVisitorActivity.getStartIntent(getContext()));
                    break;

                case HomeViewModel.REJECTED_VIEW:
                    showToast(R.string.under_development);
                    break;
            }
        });
        getViewDataBinding().recyclerView.setAdapter(homeAdapter);

        getViewModel().getHomeListData().observe(this, homeBeansList -> {
            homeList.clear();
            homeList.addAll(homeBeansList);
            homeAdapter.notifyDataSetChanged();
        });
        getViewModel().setupHomeList();
    }

    @Override
    public void onStart() {
        super.onStart();
        getViewModel().getVisitorCount();
    }

    public interface HomeFragmentInteraction {
        void onReceiveNotificationCount(int count);
    }
}
