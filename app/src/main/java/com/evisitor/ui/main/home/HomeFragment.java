package com.evisitor.ui.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.HomeBean;
import com.evisitor.databinding.FragmentHomeBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.main.commercial.add.CommercialAddVisitorActivity;
import com.evisitor.ui.main.commercial.staff.CommercialStaffActivity;
import com.evisitor.ui.main.commercial.visitor.VisitorActivity;
import com.evisitor.ui.main.home.blacklist.BlackListVisitorActivity;
import com.evisitor.ui.main.home.flag.FlagVisitorActivity;
import com.evisitor.ui.main.home.rejected.RejectedVisitorActivity;
import com.evisitor.ui.main.home.total.TotalVisitorsActivity;
import com.evisitor.ui.main.home.trespasser.TrespasserActivity;
import com.evisitor.ui.main.residential.add.AddVisitorActivity;
import com.evisitor.ui.main.residential.guest.GuestActivity;
import com.evisitor.ui.main.residential.sp.SPActivity;
import com.evisitor.ui.main.residential.staff.HouseKeepingActivity;
import com.evisitor.util.AppConstants;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
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
        getViewDataBinding().toolbar.tvTitle.setText(R.string.title_home);

        setupAdapter();
        mViewModel.getNotificationCountData().observe(this, count -> {
            if (interaction != null)
                interaction.onReceiveNotificationCount(count);
        });

        //update guest configuration in data manager
        mViewModel.doGetGuestConfiguration(null);
    }

    private void setupAdapter() {
        homeList = new ArrayList<>();
        HomeAdapter homeAdapter = new HomeAdapter(homeList, pos -> {
            switch (homeList.get(pos).getPos()) {
                case HomeViewModel.ADD_VISITOR_VIEW:
                    Intent i = mViewModel.getDataManager().isCommercial() ? CommercialAddVisitorActivity.getStartIntent(getContext()) : AddVisitorActivity.getStartIntent(getContext());
                    i.putExtra(AppConstants.FROM, AppConstants.CONTROLLER_HOME);
                    startActivity(i);
                    break;

                case HomeViewModel.GUEST_VIEW:
                    startActivity(mViewModel.getDataManager().isCommercial() ? VisitorActivity.getStartIntent(getContext()) : GuestActivity.getStartIntent(getContext()));
                    break;

                case HomeViewModel.STAFF_VIEW:
                    startActivity(mViewModel.getDataManager().isCommercial() ? CommercialStaffActivity.getStartIntent(getContext()) : HouseKeepingActivity.getStartIntent(getContext()));
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
                    startActivity(RejectedVisitorActivity.getStartIntent(getContext()));
                    break;
            }
        });
        getViewDataBinding().recyclerView.setAdapter(homeAdapter);

        mViewModel.getHomeListData().observe(this, homeBeansList -> {
            homeList.clear();
            homeList.addAll(homeBeansList);
            homeAdapter.notifyDataSetChanged();
        });
        mViewModel.setupHomeList();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getVisitorCount();
    }

    public interface HomeFragmentInteraction {
        void onReceiveNotificationCount(int count);
    }
}
