package com.evisitor.ui.main.home;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.HomeBean;
import com.evisitor.data.model.ResidentProfile;
import com.evisitor.databinding.FragmentHomeBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.main.commercial.staff.CommercialStaffActivity;
import com.evisitor.ui.main.commercial.visitor.VisitorActivity;
import com.evisitor.ui.main.home.blacklist.BlackListVisitorActivity;
import com.evisitor.ui.main.home.flag.FlagVisitorActivity;
import com.evisitor.ui.main.home.recurrentvisitor.FilterRecurrentVisitorActivity;
import com.evisitor.ui.main.home.rejected.RejectedVisitorActivity;
import com.evisitor.ui.main.home.scan.BarcodeScanActivity;
import com.evisitor.ui.main.home.total.TotalVisitorsActivity;
import com.evisitor.ui.main.home.trespasser.TrespasserActivity;
import com.evisitor.ui.main.residential.guest.GuestActivity;
import com.evisitor.ui.main.residential.residentprofile.ResidentProfileActivity;
import com.evisitor.ui.main.residential.sp.SPActivity;
import com.evisitor.ui.main.residential.staff.HouseKeepingActivity;
import com.evisitor.util.AppConstants;
import com.evisitor.util.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.evisitor.util.AppConstants.SCAN_RESULT;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> implements HomeNavigator {

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

        if (!getViewModel().getDataManager().isCommercial()) {
            getViewDataBinding().toolbar.imgSearch.setVisibility(View.VISIBLE);
            getViewDataBinding().toolbar.imgSearch.setImageResource(R.drawable.ic_qr);
        }
        getViewDataBinding().toolbar.imgSearch.setOnClickListener(v -> {
            if (PermissionUtils.checkCameraPermission(getActivity())) {
                Intent i = BarcodeScanActivity.getStartIntent(getActivity());
                startActivityForResult(i, SCAN_RESULT);
            }
        });

        //update guest configuration in data manager
        mViewModel.doGetGuestConfiguration(null);
    }

    private void setupAdapter() {
        homeList = new ArrayList<>();
        HomeAdapter homeAdapter = new HomeAdapter(homeList, pos -> {
            switch (homeList.get(pos).getPos()) {
                case HomeViewModel.ADD_VISITOR_VIEW:
                    Intent i = FilterRecurrentVisitorActivity.getStartIntent(getContext());
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

    @Override
    public void onSuccessResidentData(ResidentProfile profile) {
        Intent i = ResidentProfileActivity.getStartIntent(getActivity());
        i.putExtra("profile", profile);
        startActivity(i);
    }

    public interface HomeFragmentInteraction {
        void onReceiveNotificationCount(int count);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SCAN_RESULT && data != null) {
                String barcodeData = data.getStringExtra("data");
                if (barcodeData != null && !barcodeData.isEmpty()) {
                    getViewModel().getResidentData(barcodeData.concat(".png"));
                } else {
                    showAlert(R.string.alert, R.string.blank);
                }
            }
        }

    }


}
