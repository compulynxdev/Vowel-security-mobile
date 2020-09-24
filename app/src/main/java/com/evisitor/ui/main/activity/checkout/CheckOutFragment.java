package com.evisitor.ui.main.activity.checkout;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseKeeping;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.databinding.FragmentCheckOutBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.main.activity.ActivityNavigator;
import com.evisitor.ui.main.activity.checkout.adapter.GuestCheckOutAdapter;
import com.evisitor.ui.main.activity.checkout.adapter.HouseKeepingCheckOutAdapter;
import com.evisitor.ui.main.activity.checkout.adapter.ServiceProviderCheckOutAdapter;
import com.evisitor.ui.main.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class CheckOutFragment extends BaseFragment<FragmentCheckOutBinding, CheckOutViewModel> implements ActivityNavigator {

    private List<Guests> guestsList;
    private List<ServiceProvider> serviceProviderList;
    private List<HouseKeeping> houseKeepingList;

    private GuestCheckOutAdapter guestAdapter;
    private ServiceProviderCheckOutAdapter serviceProviderAdapter;
    private HouseKeepingCheckOutAdapter houseKeepingAdapter;
    private int listOf=0;
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
        switch (listOf){
            //guest
            case 0:
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().setNavigator(this);

        guestsList = new ArrayList<>();
        houseKeepingList = new ArrayList<>();
        serviceProviderList = new ArrayList<>();

        setUpGuestAdapter();
        setUpServiceProviderAdapter();
        setUpHouseKeeperAdapter();

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                switch (listOf){
                    case 0 :
                        setGuestAdapterLoading(true);
                        guestPage++;
                        getViewModel().getCheckOutData(guestPage, search, listOf);
                        break;

                    case 1 :
                        setHKAdapterLoading(true);
                        hkPage++;
                        getViewModel().getCheckOutData(hkPage, search, listOf);
                        break;

                    case 2 :
                        setSPAdapterLoading(true);
                        spPage++;
                        getViewModel().getCheckOutData(spPage, search, listOf);
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
        houseKeepingAdapter = new HouseKeepingCheckOutAdapter(houseKeepingList, pos -> VisitorProfileDialog.newInstance(getViewModel().getHouseKeepingCheckInProfileBean(houseKeepingList.get(pos)), null).setFlatId(houseKeepingList.get(pos).getFlatId()).setImage(houseKeepingList.get(pos).getImageUrl()).setBtnVisible(false).show(getChildFragmentManager()));
        houseKeepingAdapter.setHasStableIds(true);
    }

    private void setUpServiceProviderAdapter() {
        serviceProviderAdapter = new ServiceProviderCheckOutAdapter(serviceProviderList, pos -> VisitorProfileDialog.newInstance(getViewModel().getServiceProviderCheckInProfileBean(serviceProviderList.get(pos)), null).setFlatId(serviceProviderList.get(pos).getFlatId()).setImage(serviceProviderList.get(pos).getImageUrl()).setBtnVisible(false).show(getChildFragmentManager()));
        serviceProviderAdapter.setHasStableIds(true);
    }

    private void setUpGuestAdapter() {
        guestAdapter = new GuestCheckOutAdapter(guestsList, pos -> VisitorProfileDialog.newInstance(getViewModel().getGuestCheckInProfileBean(guestsList.get(pos)), null).setFlatId(guestsList.get(pos).getFlatId()).setImage(guestsList.get(pos).getImageUrl()).setBtnVisible(false).show(getChildFragmentManager()));
        guestAdapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(guestAdapter);
    }

    public void doSearch(String search) {
        this.search = search;
        scrollListener.onDataCleared();
        switch (listOf) {
            case 0:
                guestsList.clear();
                guestPage = 0;
                getViewModel().getCheckOutData(guestPage, search, listOf);
                break;

            case 1:
                houseKeepingList.clear();
                hkPage = 0;
                getViewModel().getCheckOutData(hkPage, search, listOf);
                break;

            case 2:
                serviceProviderList.clear();
                spPage = 0;
                getViewModel().getCheckOutData(spPage, search, listOf);
                break;
        }
    }

    @Override
    public void onExpectedGuestSuccess(List<Guests> tmpGuestsList) {
        if (guestPage == 0) guestsList.clear();

        guestsList.addAll(tmpGuestsList);
        guestAdapter.notifyDataSetChanged();

        if (listOf == 0) listener.totalCount(guestsList.size());
    }

    @Override
    public void onExpectedHKSuccess(List<HouseKeeping> tmpHouseKeepingList) {
        if (hkPage == 0) houseKeepingList.clear();

        houseKeepingList.addAll(tmpHouseKeepingList);
        houseKeepingAdapter.notifyDataSetChanged();
        if (listOf == 1) listener.totalCount(houseKeepingList.size());
    }

    @Override
    public void onExpectedSPSuccess(List<ServiceProvider> tmpSPList) {
        if (spPage == 0) serviceProviderList.clear();

        serviceProviderList.addAll(tmpSPList);
        serviceProviderAdapter.notifyDataSetChanged();

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

    public interface OnFragmentInteraction{
        void totalCount(int size);
    }
}
