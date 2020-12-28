package com.evisitor.ui.main.activity.checkin;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.CommercialGuestResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseKeeping;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.FragmentCheckInBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.activity.ActivityNavigator;
import com.evisitor.ui.main.activity.checkin.adapter.CommercialGuestCheckInAdapter;
import com.evisitor.ui.main.activity.checkin.adapter.GuestCheckInAdapter;
import com.evisitor.ui.main.activity.checkin.adapter.HouseKeepingCheckInAdapter;
import com.evisitor.ui.main.activity.checkin.adapter.ServiceProviderCheckInAdapter;
import com.evisitor.ui.main.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class CheckInFragment extends BaseFragment<FragmentCheckInBinding, CheckInViewModel> implements ActivityNavigator {

    private CheckInViewModel viewModel;
    private List<CommercialGuestResponse.CommercialGuest> commercialGuestList;
    private List<Guests> guestsList;
    private List<ServiceProvider> serviceProviderList;
    private List<HouseKeeping> houseKeepingList;

    private GuestCheckInAdapter guestAdapter;
    private CommercialGuestCheckInAdapter commercialGuestCheckInAdapter;
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
        getViewModel().setCheckInOutNavigator(this);
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
                if (getViewModel().getDataManager().isCommercial())
                    getViewDataBinding().recyclerView.setAdapter(commercialGuestCheckInAdapter);
                else
                    getViewDataBinding().recyclerView.setAdapter(guestAdapter);
                break;

            //house
            case 1:
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
        if (viewModel == null)
            viewModel = new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(CheckInViewModel.class);

        return viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        houseKeepingList = new ArrayList<>();
        serviceProviderList = new ArrayList<>();

        if (getViewModel().getDataManager().isCommercial()) {
            setUpCommercialGuestAdapter();
        } else {
            setUpGuestAdapter();
        }

        setUpServiceProviderAdapter();

        setUpHouseKeeperAdapter();
        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                switch (listOf) {
                    case 0:
                        setGuestAdapterLoading(true);
                        guestPage++;
                        getViewModel().getCheckInData(guestPage, search, listOf);
                        break;

                    case 1:
                        setHKAdapterLoading(true);
                        hkPage++;
                        getViewModel().getCheckInData(hkPage, search, listOf);
                        break;

                    case 2:
                        setSPAdapterLoading(true);
                        spPage++;
                        getViewModel().getCheckInData(spPage, search, listOf);
                        break;


                }
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);
        updateUI();
    }

    private void setUpCommercialGuestAdapter() {
        commercialGuestList = new ArrayList<>();
        commercialGuestCheckInAdapter = new CommercialGuestCheckInAdapter(commercialGuestList, getBaseActivity(), pos -> {
            CommercialGuestResponse.CommercialGuest guests = commercialGuestList.get(pos);
            List<VisitorProfileBean> beans = getViewModel().getCommercialGuestCheckInProfileBean(guests);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                getViewModel().checkOut(0);
            }).setIsCommercialGuest(true).setImage(guests.getImageUrl()).setBtnLabel(getString(R.string.check_out)).show(getFragmentManager());
        });

        getViewDataBinding().recyclerView.setAdapter(commercialGuestCheckInAdapter);
    }

    private void setUpHouseKeeperAdapter() {
        houseKeepingAdapter = new HouseKeepingCheckInAdapter(houseKeepingList, getBaseActivity(), houseKeeping -> {
            List<VisitorProfileBean> beans = getViewModel().getHouseKeepingCheckInProfileBean(houseKeeping);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                if (houseKeeping.isCheckOutFeature() && !houseKeeping.isHostCheckOut())
                    showCallDialog(1);
                else {
                    if (isNetworkConnected(true))
                        getViewModel().checkOut(1);
                }
            }).setImage(houseKeeping.getImageUrl()).setBtnLabel(getString(R.string.check_out)).show(getFragmentManager());
        });
    }

    private void setUpServiceProviderAdapter() {
        serviceProviderAdapter = new ServiceProviderCheckInAdapter(serviceProviderList, getBaseActivity(), serviceProvider -> {
            List<VisitorProfileBean> beans = getViewModel().getServiceProviderCheckInProfileBean(serviceProvider);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                if (serviceProvider.isCheckOutFeature() && !serviceProvider.isHostCheckOut())
                    showCallDialog(2);
                else getViewModel().checkOut(2);
            }).setImage(serviceProvider.getImageUrl()).setBtnLabel(getString(R.string.check_out)).show(getFragmentManager());
        });
    }

    private void setUpGuestAdapter() {
        guestsList = new ArrayList<>();
        guestAdapter = new GuestCheckInAdapter(guestsList, getBaseActivity(), pos -> {
            Guests guests = guestsList.get(pos);
            List<VisitorProfileBean> beans = getViewModel().getGuestCheckInProfileBean(guests);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                if (guests.isCheckOutFeature() && !guests.isHostCheckOut())
                    showCallDialog(0);
                else getViewModel().checkOut(0);
            }).setImage(guests.getImageUrl()).setBtnLabel(getString(R.string.check_out)).show(getFragmentManager());
        });

        getViewDataBinding().recyclerView.setAdapter(guestAdapter);
    }

    private void showCallDialog(int type) {
        AlertDialog.newInstance()
                .setNegativeBtnShow(false)
                .setCloseBtnShow(true)
                .setTitle(getString(R.string.check_out))
                .setMsg(getString(R.string.msg_check_out_call))
                .setPositiveBtnLabel(getString(R.string.approve))
                /*.setNegativeBtnLabel(getString(R.string.reject))
                .setOnNegativeClickListener(dialog1 -> {
                    dialog1.dismiss();
                    showAlert(R.string.alert, R.string.check_out_rejected);
                })*/
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    if (isNetworkConnected(true))
                        getViewModel().checkOut(type);
                }).show(getFragmentManager());
    }

    public void doSearch(String search) {
        this.search = search;
        scrollListener.onDataCleared();
        switch (listOf) {
            case 0:
                if (getViewModel().getDataManager().isCommercial())
                    commercialGuestList.clear();
                else
                    guestsList.clear();

                guestPage = 0;
                getViewModel().getCheckInData(guestPage, search, listOf);
                break;

            case 1:
                houseKeepingList.clear();
                hkPage = 0;
                getViewModel().getCheckInData(hkPage, search, listOf);
                break;

            case 2:
                serviceProviderList.clear();
                spPage = 0;
                getViewModel().getCheckInData(spPage, search, listOf);
                break;
        }
    }

    @Override
    public void onExpectedCommercialGuestSuccess(List<CommercialGuestResponse.CommercialGuest> tmpGuestsList) {
        if (guestPage == 0) commercialGuestList.clear();

        commercialGuestList.addAll(tmpGuestsList);
        commercialGuestCheckInAdapter.notifyDataSetChanged();

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
    public void onExpectedGuestSuccess(List<Guests> tmpGuestsList) {
        if (guestPage == 0) guestsList.clear();

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
    public void onExpectedHKSuccess(List<HouseKeeping> tmpHouseKeepingList) {
        if (hkPage == 0) houseKeepingList.clear();

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
    public void onExpectedSPSuccess(List<ServiceProvider> tmpSPList) {
        if (spPage == 0) serviceProviderList.clear();

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
        if (getViewModel().getDataManager().isCommercial()) {
            if (commercialGuestCheckInAdapter != null) {
                commercialGuestCheckInAdapter.showLoading(isShowLoader);
                commercialGuestCheckInAdapter.notifyDataSetChanged();
            }
        } else {
            if (guestAdapter != null) {
                guestAdapter.showLoading(isShowLoader);
                guestAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setHKAdapterLoading(boolean isShowLoader) {
        if (houseKeepingAdapter != null) {
            houseKeepingAdapter.showLoading(isShowLoader);
            houseKeepingAdapter.notifyDataSetChanged();
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
