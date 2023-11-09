package com.evisitor.ui.main.activity.checkout;

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
import com.evisitor.databinding.FragmentCheckOutBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.main.activity.ActivityNavigator;
import com.evisitor.ui.main.activity.checkout.adapter.CommercialStaffCheckOutAdapter;
import com.evisitor.ui.main.activity.checkout.adapter.CommercialVisitorCheckOutAdapter;
import com.evisitor.ui.main.activity.checkout.adapter.GuestCheckOutAdapter;
import com.evisitor.ui.main.activity.checkout.adapter.HouseKeepingCheckOutAdapter;
import com.evisitor.ui.main.activity.checkout.adapter.ServiceProviderCheckOutAdapter;
import com.evisitor.ui.main.home.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class CheckOutFragment extends BaseFragment<FragmentCheckOutBinding, CheckOutViewModel> implements ActivityNavigator {

    private List<CommercialVisitorResponse.CommercialGuest> commercialGuestList;
    private List<Guests> guestsList;
    private List<ServiceProvider> serviceProviderList;
    private List<HouseKeeping> houseKeepingList;
    private List<CommercialStaffResponse.ContentBean> commercialStaffList;

    private CommercialVisitorCheckOutAdapter commercialVisitorAdapter;
    private GuestCheckOutAdapter guestAdapter;
    private ServiceProviderCheckOutAdapter serviceProviderAdapter;
    private CommercialStaffCheckOutAdapter commercialStaffAdapter;
    private HouseKeepingCheckOutAdapter houseKeepingAdapter;
    private int listOf = 0;
    private OnFragmentInteraction listener;
    private RecyclerViewScrollListener scrollListener;
    private int guestPage, hkPage, spPage;
    private String search = "";

    public static CheckOutFragment newInstance(int listOf, OnFragmentInteraction interaction) {
        CheckOutFragment fragment = new CheckOutFragment();
        Bundle args = new Bundle();
        fragment.setData(listOf, interaction);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
    }

    private void setData(int listOf, OnFragmentInteraction interaction) {
        this.listOf = listOf;
        listener = interaction;
    }


    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_check_out;
    }

    @Override
    public CheckOutViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(CheckOutViewModel.class);
    }

    public void setCheckOutList(int listOf) {
        this.listOf = listOf;
        switch (listOf) {
            //guest
            case 0:
                if (mViewModel.getDataManager().isCommercial()) {
                    getViewDataBinding().recyclerView.setAdapter(commercialVisitorAdapter);
                } else getViewDataBinding().recyclerView.setAdapter(guestAdapter);
                break;

            //domestic staff or commercial staff
            case 1:
                if (mViewModel.getDataManager().isCommercial()) {
                    getViewDataBinding().recyclerView.setAdapter(commercialStaffAdapter);
                } else getViewDataBinding().recyclerView.setAdapter(houseKeepingAdapter);
                break;

            //service
            case 2:
                getViewDataBinding().recyclerView.setAdapter(serviceProviderAdapter);
                break;
        }
        doSearch(search);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        serviceProviderList = new ArrayList<>();

        if (mViewModel.getDataManager().isCommercial()) {
            setUpCommercialVisitorAdapter();
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
                        mViewModel.getCheckOutData(guestPage, search, listOf);
                        break;

                    case 1:
                        setHKAdapterLoading(true);
                        hkPage++;
                        mViewModel.getCheckOutData(hkPage, search, listOf);
                        break;

                    case 2:
                        setSPAdapterLoading(true);
                        spPage++;
                        mViewModel.getCheckOutData(spPage, search, listOf);
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
        commercialStaffAdapter = new CommercialStaffCheckOutAdapter(commercialStaffList, pos -> VisitorProfileDialog.newInstance(mViewModel.getCommercialStaffProfileBean(commercialStaffList.get(pos)), null).setImage(commercialStaffList.get(pos).getImageUrl()).
                setVehicalNoPlateImg(commercialStaffList.get(pos).getVehicleImage())
                .setBtnVisible(false).show(getChildFragmentManager()));
        commercialStaffAdapter.setHasStableIds(true);
    }

    private void setUpHouseKeeperAdapter() {
        houseKeepingList = new ArrayList<>();
        houseKeepingAdapter = new HouseKeepingCheckOutAdapter(houseKeepingList, pos -> VisitorProfileDialog.newInstance(mViewModel.getHouseKeepingProfileBean(houseKeepingList.get(pos)), null).checkInIsApproved(true).setImage(houseKeepingList.get(pos).getImageUrl())
                . setVehicalNoPlateImg(houseKeepingList.get(pos).getVehicleImage())
                .setBtnVisible(false).show(getChildFragmentManager()));
        houseKeepingAdapter.setHasStableIds(true);
    }

    private void setUpServiceProviderAdapter() {
        serviceProviderAdapter = new ServiceProviderCheckOutAdapter(serviceProviderList, pos -> VisitorProfileDialog.newInstance(mViewModel.getServiceProviderProfileBean(serviceProviderList.get(pos)), null).setImage(serviceProviderList.get(pos).getImageUrl())
                .setVehicalNoPlateImg(serviceProviderList.get(pos).getVehicleImage()).checkInIsApproved(true).setBtnVisible(false).show(getChildFragmentManager()));
        serviceProviderAdapter.setHasStableIds(true);
    }

    private void setUpCommercialVisitorAdapter() {
        commercialGuestList = new ArrayList<>();
        commercialVisitorAdapter = new CommercialVisitorCheckOutAdapter(commercialGuestList, pos -> VisitorProfileDialog.newInstance(mViewModel.getCommercialGuestProfileBean(commercialGuestList.get(pos)), null).setBtnLabel(getString(R.string.check_out)).setImage(commercialGuestList.get(pos).getImageUrl()).
                setVehicalNoPlateImg(commercialGuestList.get(pos).getVehicleImage())
                .setIsCommercialGuest(true).setBtnVisible(false).show(getChildFragmentManager()));
        commercialVisitorAdapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(commercialVisitorAdapter);
    }

    private void setUpGuestAdapter() {
        guestsList = new ArrayList<>();
        guestAdapter = new GuestCheckOutAdapter(guestsList, pos -> VisitorProfileDialog.newInstance(mViewModel.getGuestProfileBean(guestsList.get(pos)), null).setImage(guestsList.get(pos).getImageUrl()).setVehicalNoPlateImg(guestsList.get(pos).getVehicleImage()).setBtnVisible(false).checkInIsApproved(true).show(getChildFragmentManager()));
        guestAdapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(guestAdapter);
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
                mViewModel.getCheckOutData(guestPage, search, listOf);
                break;

            case 1:
                if (mViewModel.getDataManager().isCommercial()) {
                    commercialStaffList.clear();
                    commercialStaffAdapter.notifyDataSetChanged();
                } else {
                    houseKeepingList.clear();
                    houseKeepingAdapter.notifyDataSetChanged();
                }
                hkPage = 0;
                mViewModel.getCheckOutData(hkPage, search, listOf);
                break;

            case 2:
                serviceProviderList.clear();
                spPage = 0;
                mViewModel.getCheckOutData(spPage, search, listOf);
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
        commercialStaffAdapter.notifyDataSetChanged();

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
            if (commercialStaffAdapter != null) {
                commercialStaffAdapter.showLoading(isShowLoader);
                commercialStaffAdapter.notifyDataSetChanged();
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
