package com.evisitor.ui.main.activity.checkout;

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
import com.evisitor.databinding.FragmentCheckOutBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.main.activity.ActivityNavigator;
import com.evisitor.ui.main.activity.checkout.adapter.CommercialGuestCheckOutAdapter;
import com.evisitor.ui.main.activity.checkout.adapter.GuestCheckOutAdapter;
import com.evisitor.ui.main.activity.checkout.adapter.HouseKeepingCheckOutAdapter;
import com.evisitor.ui.main.activity.checkout.adapter.ServiceProviderCheckOutAdapter;
import com.evisitor.ui.main.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class CheckOutFragment extends BaseFragment<FragmentCheckOutBinding, CheckOutViewModel> implements ActivityNavigator {

    private List<CommercialGuestResponse.CommercialGuest> commercialGuestList;
    private List<Guests> guestsList;
    private List<ServiceProvider> serviceProviderList;
    private List<HouseKeeping> houseKeepingList;

    private GuestCheckOutAdapter guestAdapter;
    private CommercialGuestCheckOutAdapter commercialGuestCheckOutAdapter;
    private ServiceProviderCheckOutAdapter serviceProviderAdapter;
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
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(CheckOutViewModel.class);
    }

    public void setCheckOutList(int listOf) {
        this.listOf = listOf;
        switch (listOf) {
            //guest
            case 0:
                if (mViewModel.getDataManager().isCommercial()) {
                    getViewDataBinding().recyclerView.setAdapter(commercialGuestCheckOutAdapter);
                } else getViewDataBinding().recyclerView.setAdapter(guestAdapter);
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        houseKeepingList = new ArrayList<>();
        serviceProviderList = new ArrayList<>();

        if (mViewModel.getDataManager().isCommercial()) {
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

    private void setUpHouseKeeperAdapter() {
        houseKeepingAdapter = new HouseKeepingCheckOutAdapter(houseKeepingList, pos -> VisitorProfileDialog.newInstance(mViewModel.getHouseKeepingCheckInProfileBean(houseKeepingList.get(pos)), null).setImage(houseKeepingList.get(pos).getImageUrl()).setBtnVisible(false).show(getChildFragmentManager()));
        houseKeepingAdapter.setHasStableIds(true);
    }

    private void setUpServiceProviderAdapter() {
        serviceProviderAdapter = new ServiceProviderCheckOutAdapter(serviceProviderList, pos -> VisitorProfileDialog.newInstance(mViewModel.getServiceProviderCheckInProfileBean(serviceProviderList.get(pos)), null).setImage(serviceProviderList.get(pos).getImageUrl()).setBtnVisible(false).show(getChildFragmentManager()));
        serviceProviderAdapter.setHasStableIds(true);
    }

    private void setUpCommercialGuestAdapter() {
        commercialGuestList = new ArrayList<>();
        commercialGuestCheckOutAdapter = new CommercialGuestCheckOutAdapter(commercialGuestList, pos -> VisitorProfileDialog.newInstance(mViewModel.getCommercialGuestCheckInProfileBean(commercialGuestList.get(pos)), null).setImage(commercialGuestList.get(pos).getImageUrl()).setIsCommercialGuest(true).setBtnVisible(false).show(getChildFragmentManager()));
        commercialGuestCheckOutAdapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(commercialGuestCheckOutAdapter);
    }

    private void setUpGuestAdapter() {
        guestsList = new ArrayList<>();
        guestAdapter = new GuestCheckOutAdapter(guestsList, pos -> VisitorProfileDialog.newInstance(mViewModel.getGuestCheckInProfileBean(guestsList.get(pos)), null).setImage(guestsList.get(pos).getImageUrl()).setBtnVisible(false).show(getChildFragmentManager()));
        guestAdapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(guestAdapter);
    }

    public void doSearch(String search) {
        this.search = search;
        scrollListener.onDataCleared();
        switch (listOf) {
            case 0:
                if (mViewModel.getDataManager().isCommercial())
                    commercialGuestList.clear();
                else
                    guestsList.clear();

                guestPage = 0;
                mViewModel.getCheckOutData(guestPage, search, listOf);
                break;

            case 1:
                houseKeepingList.clear();
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
    public void onExpectedCommercialGuestSuccess(List<CommercialGuestResponse.CommercialGuest> tmpGuestsList) {
        if (guestPage == 0) commercialGuestList.clear();

        commercialGuestList.addAll(tmpGuestsList);
        commercialGuestCheckOutAdapter.notifyDataSetChanged();

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
        if (guestAdapter != null) {
            guestAdapter.showLoading(isShowLoader);
            guestAdapter.notifyDataSetChanged();
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
