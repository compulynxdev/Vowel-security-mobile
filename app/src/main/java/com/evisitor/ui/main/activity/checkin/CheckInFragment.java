package com.evisitor.ui.main.activity.checkin;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.data.model.CommercialVisitorResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseKeeping;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.FragmentCheckInBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.activity.ActivityNavigator;
import com.evisitor.ui.main.activity.checkin.adapter.CommercialStaffCheckInAdapter;
import com.evisitor.ui.main.activity.checkin.adapter.CommercialVisitorCheckInAdapter;
import com.evisitor.ui.main.activity.checkin.adapter.GuestCheckInAdapter;
import com.evisitor.ui.main.activity.checkin.adapter.HouseKeepingCheckInAdapter;
import com.evisitor.ui.main.activity.checkin.adapter.ServiceProviderCheckInAdapter;
import com.evisitor.ui.main.home.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class CheckInFragment extends BaseFragment<FragmentCheckInBinding, CheckInViewModel> implements ActivityNavigator {

    private List<CommercialVisitorResponse.CommercialGuest> commercialGuestList;
    private List<Guests> guestsList;
    private List<ServiceProvider> serviceProviderList;
    private List<HouseKeeping> houseKeepingList;
    private List<CommercialStaffResponse.ContentBean> commercialStaffList;
    private CommercialStaffCheckInAdapter commercialStaffCheckInAdapter;
    private GuestCheckInAdapter guestAdapter;
    private CommercialVisitorCheckInAdapter commercialVisitorAdapter;
    private ServiceProviderCheckInAdapter serviceProviderAdapter;
    private HouseKeepingCheckInAdapter houseKeepingAdapter;
    private OnFragmentInteraction listener;
    private RecyclerViewScrollListener scrollListener;
    private int guestPage, hkPage, spPage;
    private int listOf = 0;
    private String search = "";

    public static CheckInFragment newInstance(int listOf, OnFragmentInteraction listener) {
        CheckInFragment fragment = new CheckInFragment();
        Bundle args = new Bundle();
        fragment.setData(listOf, listener);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setCheckInOutNavigator(this);
    }

    private void setData(int listOf, OnFragmentInteraction interaction) {
        this.listOf = listOf;
        listener = interaction;
    }

    public void setCheckInList(int listOf) {
        this.listOf = listOf;
        switch (listOf) {
            //guest
            case 0:
                if (mViewModel.getDataManager().isCommercial())
                    getViewDataBinding().recyclerView.setAdapter(commercialVisitorAdapter);
                else
                    getViewDataBinding().recyclerView.setAdapter(guestAdapter);
                break;

            //house
            case 1:
                if (mViewModel.getDataManager().isCommercial())
                    getViewDataBinding().recyclerView.setAdapter(commercialStaffCheckInAdapter);
                else
                    getViewDataBinding().recyclerView.setAdapter(houseKeepingAdapter);
                break;

            //service
            case 2:
                getViewDataBinding().recyclerView.setAdapter(serviceProviderAdapter);
                break;
        }
        doSearch(search);
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
        houseKeepingList = new ArrayList<>();
        serviceProviderList = new ArrayList<>();

        if (mViewModel.getDataManager().isCommercial()) {
            setUpCommercialGuestAdapter();
            setUpCommercialStaffAdapter();
        } else {
            setUpGuestAdapter();
            setUpHouseKeeperAdapter();
        }
        setUpServiceProviderAdapter();
        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                switch (listOf) {
                    case 0:
                        setGuestAdapterLoading(true);
                        guestPage++;
                        mViewModel.getCheckInData(guestPage, search, listOf);
                        break;

                    case 1:
                        setHKAdapterLoading(true);
                        hkPage++;
                        mViewModel.getCheckInData(hkPage, search, listOf);
                        break;

                    case 2:
                        setSPAdapterLoading(true);
                        spPage++;
                        mViewModel.getCheckInData(spPage, search, listOf);
                        break;
                }
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);
        updateUI();
    }

    private void setUpCommercialStaffAdapter() {
        commercialStaffList = new ArrayList<>();
        commercialStaffCheckInAdapter = new CommercialStaffCheckInAdapter(commercialStaffList, getBaseActivity(), bean -> {
            List<VisitorProfileBean> beans = mViewModel.getCommercialStaffBean(bean);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                mViewModel.staffCheckOut();
            }).setImage(bean.getImageUrl()).setBtnLabel(getString(R.string.check_out))
                    .setVehicalNoPlateImg(bean.getVehicleImage()).show(getFragmentManager());
        });
    }

    private void setUpCommercialGuestAdapter() {
        commercialGuestList = new ArrayList<>();
        commercialVisitorAdapter = new CommercialVisitorCheckInAdapter(commercialGuestList, getBaseActivity(), pos -> {
            CommercialVisitorResponse.CommercialGuest guests = commercialGuestList.get(pos);
            List<VisitorProfileBean> beans = mViewModel.getCommercialGuestProfileBean(guests);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                if (!guests.getHost().isEmpty() && guests.getHostCheckOutTime().isEmpty())
                    showCallDialog(0);
                else mViewModel.checkOut(0);
            }).setIsCommercialGuest(true).setImage(guests.getImageUrl()).setBtnLabel(getString(R.string.check_out)).show(getFragmentManager());
        });

        getViewDataBinding().recyclerView.setAdapter(commercialVisitorAdapter);
    }

    private void setUpHouseKeeperAdapter() {
        houseKeepingAdapter = new HouseKeepingCheckInAdapter(houseKeepingList, getBaseActivity(), houseKeeping -> {
            List<VisitorProfileBean> beans = mViewModel.getHouseKeepingProfileBean(houseKeeping);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                if (houseKeeping.isCheckOutFeature() && !houseKeeping.isHostCheckOut())
                    showCallDialog(1);
                else {
                    if (isNetworkConnected(true))
                        mViewModel.checkOut(1);
                }
            }).setImage(houseKeeping.getImageUrl()).setBtnLabel(getString(R.string.check_out)).show(getFragmentManager());
        });
    }

    private void setUpServiceProviderAdapter() {
        serviceProviderAdapter = new ServiceProviderCheckInAdapter(serviceProviderList, getBaseActivity(), serviceProvider -> {
            List<VisitorProfileBean> beans = mViewModel.getServiceProviderProfileBean(serviceProvider);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                if (serviceProvider.isCheckOutFeature() && !serviceProvider.isHostCheckOut())
                    showCallDialog(2);
                else mViewModel.checkOut(2);
            }).setImage(serviceProvider.getImageUrl()).setVehicalNoPlateImg(serviceProvider.getVehicleImage()).setBtnLabel(getString(R.string.check_out)).show(getFragmentManager());
        });
    }

    private void setUpGuestAdapter() {
        guestsList = new ArrayList<>();
        guestAdapter = new GuestCheckInAdapter(guestsList, getBaseActivity(), pos -> {
            Guests guests = guestsList.get(pos);
            List<VisitorProfileBean> beans = mViewModel.getGuestProfileBean(guests);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                if (guests.isCheckOutFeature() && !guests.isHostCheckOut())
                    showCallDialog(0);
                else mViewModel.checkOut(0);
            }).setImage(guests.getImageUrl()).setBtnLabel(getString(R.string.check_out)).
                    setVehicalNoPlateImg(guests.getVehicleImage()).show(getFragmentManager());
        });

        getViewDataBinding().recyclerView.setAdapter(guestAdapter);
    }

    private void showCallDialog(int type) {
        AlertDialog.newInstance()
                .setNegativeBtnShow(false)
                .setCloseBtnShow(true)
                .setTitle(getString(R.string.check_out))
                .setMsg(mViewModel.getDataManager().isCommercial() ? getString(R.string.commercial_msg_check_out_call) : getString(R.string.msg_check_out_call))
                .setPositiveBtnLabel(getString(R.string.approve))
                /*.setNegativeBtnLabel(getString(R.string.reject))
                .setOnNegativeClickListener(dialog1 -> {
                    dialog1.dismiss();
                    showAlert(R.string.alert, R.string.check_out_rejected);
                })*/
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    if (isNetworkConnected(true))
                        mViewModel.checkOut(type);
                }).show(getFragmentManager());
    }

    public void doSearch(String search) {
        this.search = search;
        if (scrollListener != null) {
            scrollListener.onDataCleared();
        }
        switch (listOf) {
            case 0:
                if (mViewModel.getDataManager().isCommercial()) {
                    commercialGuestList.clear();
                    commercialVisitorAdapter.notifyDataSetChanged();
                } else {
                    guestsList.clear();
                    guestAdapter.notifyDataSetChanged();
                }

                guestPage = 0;
                mViewModel.getCheckInData(guestPage, search, listOf);
                break;

            case 1:
                if (mViewModel.getDataManager().isCommercial()) {
                    commercialStaffList.clear();
                    commercialStaffCheckInAdapter.notifyDataSetChanged();
                } else {
                    houseKeepingList.clear();
                    houseKeepingAdapter.notifyDataSetChanged();
                }
                hkPage = 0;
                mViewModel.getCheckInData(hkPage, search, listOf);
                break;

            case 2:
                serviceProviderList.clear();
                serviceProviderAdapter.notifyDataSetChanged();
                spPage = 0;
                mViewModel.getCheckInData(spPage, search, listOf);
                break;
        }
    }

    @Override
    public void onExpectedCommercialGuestSuccess(int page, List<CommercialVisitorResponse.CommercialGuest> tmpGuestsList) {
        if (page == 0) commercialGuestList.clear();

        commercialGuestList.addAll(tmpGuestsList);
        commercialVisitorAdapter.notifyDataSetChanged();

        if (commercialGuestList.size() == 0) {
            getViewDataBinding().recyclerView.setVisibility(View.GONE);
            getViewDataBinding().tvNoData.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().tvNoData.setVisibility(View.GONE);
            getViewDataBinding().recyclerView.setVisibility(View.VISIBLE);
        }

        if (listOf == 0) listener.totalCount(commercialGuestList.size());
    }

    @Override
    public void onExpectedGuestSuccess(int page, List<Guests> tmpGuestsList) {
        if (page == 0) guestsList.clear();

        guestsList.addAll(tmpGuestsList);
        guestAdapter.notifyDataSetChanged();

        if (guestsList.size() == 0) {
            getViewDataBinding().recyclerView.setVisibility(View.GONE);
            getViewDataBinding().tvNoData.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().tvNoData.setVisibility(View.GONE);
            getViewDataBinding().recyclerView.setVisibility(View.VISIBLE);
        }

        if (listOf == 0) listener.totalCount(guestsList.size());
    }

    @Override
    public void onExpectedHKSuccess(int page, List<HouseKeeping> tmpHouseKeepingList) {
        if (page == 0) houseKeepingList.clear();

        houseKeepingList.addAll(tmpHouseKeepingList);
        houseKeepingAdapter.notifyDataSetChanged();

        if (houseKeepingList.size() == 0) {
            getViewDataBinding().recyclerView.setVisibility(View.GONE);
            getViewDataBinding().tvNoData.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().tvNoData.setVisibility(View.GONE);
            getViewDataBinding().recyclerView.setVisibility(View.VISIBLE);
        }

        if (listOf == 1) listener.totalCount(houseKeepingList.size());
    }

    @Override
    public void onExpectedOfficeSuccess(int page, List<CommercialStaffResponse.ContentBean> staffList) {
        if (page == 0) commercialStaffList.clear();

        commercialStaffList.addAll(staffList);
        commercialStaffCheckInAdapter.notifyDataSetChanged();

        if (commercialStaffList.size() == 0) {
            getViewDataBinding().recyclerView.setVisibility(View.GONE);
            getViewDataBinding().tvNoData.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().tvNoData.setVisibility(View.GONE);
            getViewDataBinding().recyclerView.setVisibility(View.VISIBLE);
        }

        if (listOf == 1) listener.totalCount(commercialStaffList.size());
    }

    @Override
    public void onExpectedSPSuccess(int page, List<ServiceProvider> tmpSPList) {
        if (page == 0) serviceProviderList.clear();

        serviceProviderList.addAll(tmpSPList);
        serviceProviderAdapter.notifyDataSetChanged();

        if (serviceProviderList.size() == 0) {
            getViewDataBinding().recyclerView.setVisibility(View.GONE);
            getViewDataBinding().tvNoData.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().tvNoData.setVisibility(View.GONE);
            getViewDataBinding().recyclerView.setVisibility(View.VISIBLE);
        }

        if (listOf == 2) listener.totalCount(serviceProviderList.size());
    }

    @Override
    public void hideSwipeToRefresh() {
        getViewDataBinding().swipeToRefresh.setRefreshing(false);
        switch (listOf) {
            case 0:
                setGuestAdapterLoading(false);
                break;

            case 1:
                setHKAdapterLoading(false);
                break;

            case 2:
                setSPAdapterLoading(false);
                break;
        }
    }

    private void setGuestAdapterLoading(boolean isShowLoader) {
        if (mViewModel.getDataManager().isCommercial()) {
            if (commercialVisitorAdapter != null) {
                commercialVisitorAdapter.showLoading(isShowLoader);
                commercialVisitorAdapter.notifyDataSetChanged();
            }
        } else {
            if (guestAdapter != null) {
                guestAdapter.showLoading(isShowLoader);
                guestAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setHKAdapterLoading(boolean isShowLoader) {
        if (mViewModel.getDataManager().isCommercial()) {
            if (commercialStaffCheckInAdapter != null) {
                commercialStaffCheckInAdapter.showLoading(isShowLoader);
                commercialStaffCheckInAdapter.notifyDataSetChanged();
            }
        } else {
            if (houseKeepingAdapter != null) {
                houseKeepingAdapter.showLoading(isShowLoader);
                houseKeepingAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setSPAdapterLoading(boolean isShowLoader) {
        if (serviceProviderAdapter != null) {
            serviceProviderAdapter.showLoading(isShowLoader);
            serviceProviderAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshList() {
        doSearch(search);
    }

    private void updateUI() {
        getViewDataBinding().swipeToRefresh.setRefreshing(true);
        doSearch(search);
    }

    public interface OnFragmentInteraction {
        void totalCount(int size);
    }
}
