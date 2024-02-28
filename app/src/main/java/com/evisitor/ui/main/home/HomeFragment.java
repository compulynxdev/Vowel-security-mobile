package com.evisitor.ui.main.home;

import static android.app.Activity.RESULT_OK;
import static com.evisitor.util.AppConstants.ADD_FAMILY_MEMBER;
import static com.evisitor.util.AppConstants.SCAN_RESULT;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.CheckInTemperature;
import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HomeBean;
import com.evisitor.data.model.RecurrentVisitor;
import com.evisitor.data.model.ResidentProfile;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.FragmentHomeBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.main.commercial.add.CommercialAddVisitorActivity;
import com.evisitor.ui.main.commercial.secondryguest.SecondaryGuestInputActivity;
import com.evisitor.ui.main.commercial.staff.CommercialStaffActivity;
import com.evisitor.ui.main.commercial.visitor.VisitorActivity;
import com.evisitor.ui.main.home.blacklist.BlackListVisitorActivity;
import com.evisitor.ui.main.home.flag.FlagVisitorActivity;
import com.evisitor.ui.main.home.recurrentvisitor.FilterRecurrentVisitorActivity;
import com.evisitor.ui.main.home.rejected.RejectedVisitorActivity;
import com.evisitor.ui.main.home.rejectreason.InputDialog;
import com.evisitor.ui.main.home.scan.BarcodeScanActivity;
import com.evisitor.ui.main.home.total.TotalVisitorsActivity;
import com.evisitor.ui.main.home.trespasser.TrespasserActivity;
import com.evisitor.ui.main.home.visitorprofile.VisitorProfileDialog;
import com.evisitor.ui.main.residential.add.AddVisitorActivity;
import com.evisitor.ui.main.residential.guest.GuestActivity;
import com.evisitor.ui.main.residential.residentprofile.ResidentProfileActivity;
import com.evisitor.ui.main.residential.sp.SPActivity;
import com.evisitor.ui.main.residential.staff.HouseKeepingActivity;
import com.evisitor.util.AppConstants;
import com.evisitor.util.PermissionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> implements HomeNavigator {

    private final List<CheckInTemperature> guestIds = new ArrayList<>();
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
        mViewModel.setCheckInOutNavigator(this);
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
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(HomeViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().toolbar.tvTitle.setText(R.string.title_home);
        setupAdapter();
        mViewModel.getNotificationCountData().observe(getViewLifecycleOwner(), count -> {
            if (interaction != null)
                interaction.onReceiveNotificationCount(count);
        });
        getViewDataBinding().toolbar.imgSearch.setVisibility(View.VISIBLE);
        getViewDataBinding().toolbar.imgSearch.setImageResource(R.drawable.ic_qr);

        getViewDataBinding().toolbar.imgSearch.setOnClickListener(v -> {
            if (PermissionUtils.checkCameraPermission(getActivity())) {
                Intent i = BarcodeScanActivity.getStartIntent(getActivity());
                startActivityForResult(i, SCAN_RESULT);
            }
        });


        getViewDataBinding().toolbar.imgBack.setVisibility(View.VISIBLE);
        getViewDataBinding().toolbar.imgBack.setImageResource(R.drawable.ic_notification_on);

        getViewDataBinding().toolbar.imgBack.setOnClickListener(v -> {
            InputDialog.newInstance().setTitle(getString(R.string.panic)).setHint(getString(R.string.enter_description)).setOnPositiveClickListener((dialog, input) -> {
                dialog.dismiss();
                if (input.isEmpty()) {
                    showAlert(R.string.alert, R.string.description_not_empty);
                } else {
                    getViewModel().sendPanicAlert(input);
                }
            }).show(getFragmentManager());
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

        mViewModel.getHomeListData().observe(getViewLifecycleOwner(), homeBeansList -> {
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

    @Override
    public void onSuccessServiceProviderData(ServiceProvider serviceProvider) {
        List<VisitorProfileBean> bean = mViewModel.getServiceProviderProfileBean(serviceProvider);
        VisitorProfileDialog.newInstance(bean, dialog -> {
                    mViewModel.checkinOutSp(serviceProvider);
                }).setBtnLabel(serviceProvider.getCheckOutTime().isEmpty() && !serviceProvider.getCheckInTime().isEmpty() ? getString(R.string.check_out) : getString(R.string.check_in))
                .setBtnVisible(serviceProvider.getCheckOutTime().isEmpty())
                .setImage(serviceProvider.getImageUrl())
                .setVehicalNoPlateImg(serviceProvider.getVehicleImage()).show(getChildFragmentManager());
    }

    @Override
    public void onSuccessGuestData(Guests guests) {
        List<VisitorProfileBean> bean = mViewModel.setClickVisitorDetail(guests);
        VisitorProfileDialog.newInstance(bean, dialog -> {
                    if (guests.getGuestList().isEmpty()) {
                        mViewModel.approveByCall(guestIds);

            } else showSecondaryGuestListForCheckIn();
        }).setBtnLabel(getString(R.string.check_in))
                .setBtnVisible(guests.getStatus().equalsIgnoreCase("PENDING"))
                .setImage(guests.getImageUrl())
                .setVehicalNoPlateImg(guests.getVehicleImage()).show(getChildFragmentManager());
    }

    @Override
    public void onResponseRecurrentData(RecurrentVisitor recurrentVisitor) {
        Intent i = mViewModel.getDataManager().isCommercial() ? CommercialAddVisitorActivity.getStartIntent(getContext()) : AddVisitorActivity.getStartIntent(getContext());
        i.putExtra("RecurrentData", recurrentVisitor);
        startActivity(i);
    }

    @Override
    public void onScannedDataRetrieve(CommercialStaffResponse.ContentBean content) {
        if (content != null) {
            List<VisitorProfileBean> visitorProfileBeanList = mViewModel.setClickVisitorDetail(content);
            VisitorProfileDialog.newInstance(visitorProfileBeanList, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                mViewModel.doCheckIn();
            }).setImage(content.getImageUrl()).setBtnLabel(getString(R.string.check_in)).show(getChildFragmentManager());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SCAN_RESULT && data != null) {
                String barcodeData = data.getStringExtra("data");
                String[] split = barcodeData.split(",");
                if (!barcodeData.isEmpty()) {
                    getViewModel().getQRCodeDataByType(split[0].concat(".png"), split[1]);
                } else {
                    showAlert(R.string.alert, R.string.blank);
                }
            } else if (requestCode == ADD_FAMILY_MEMBER) {
                Guests tmpBean = mViewModel.getDataManager().getGuestDetail();
                Type listType = new TypeToken<List<CheckInTemperature>>() {
                }.getType();
                guestIds.clear();
                guestIds.addAll(Objects.requireNonNull(mViewModel.getDataManager().getGson().fromJson(data.getStringExtra("data"), listType)));
                mViewModel.approveByCall(guestIds);

            }
        }
    }

    private void showSecondaryGuestListForCheckIn() {
        Guests tmpBean = mViewModel.getDataManager().getGuestDetail();
        Intent i = SecondaryGuestInputActivity.getStartIntent(getBaseActivity());
        if (!tmpBean.getGuestList().isEmpty()) {
            i.putExtra("list", new Gson().toJson(tmpBean.guestList));
        }
        i.putExtra("checkIn", true);
        startActivityForResult(i, ADD_FAMILY_MEMBER);
    }

    public interface HomeFragmentInteraction {
        void onReceiveNotificationCount(int count);
    }

}
