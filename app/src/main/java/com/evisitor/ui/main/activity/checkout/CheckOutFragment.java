package com.evisitor.ui.main.activity.checkout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

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
    private EditText etSearch;
    private int listOf=0;
    private OnFragmentInteraction listener;
    private RecyclerViewScrollListener scrollListener;
    private int page=0;

    public static CheckOutFragment newInstance(EditText et_Search,int listOf, OnFragmentInteraction interaction) {
        CheckOutFragment fragment = new CheckOutFragment();
        Bundle args = new Bundle();
        fragment.setData(et_Search,listOf,interaction);
        fragment.setArguments(args);
        return fragment;
    }

    private void setData(EditText et_Search, int listOf, OnFragmentInteraction interaction){
        etSearch=et_Search;
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

    public void setGuestsList(int listOf) {
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
        doSearch(etSearch.getText().toString());
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

        setUpSearch();

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                switch (listOf){
                    case 0 :
                        setGuestAdapterLoading(true);
                        page++;
                        getViewModel().getGuestListData(page, etSearch.getText().toString().trim(),listOf);
                        break;

                    case 1 :
                        setHKAdapterLoading(true);
                        page++;
                        getViewModel().getGuestListData(page, etSearch.getText().toString().trim(),listOf);
                        break;

                    case 2 :
                        setSPAdapterLoading(true);
                        page++;
                        getViewModel().getGuestListData(page, etSearch.getText().toString().trim(),listOf);
                        break;


                }
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);
        updateUI();
    }

    private void setUpHouseKeeperAdapter() {
        houseKeepingAdapter = new HouseKeepingCheckOutAdapter(houseKeepingList, getBaseActivity());
        houseKeepingAdapter.setHasStableIds(true);
    }

    private void setUpServiceProviderAdapter() {
        serviceProviderAdapter = new ServiceProviderCheckOutAdapter(serviceProviderList, getBaseActivity());
        serviceProviderAdapter.setHasStableIds(true);
    }

    private void setUpGuestAdapter() {
        guestAdapter = new GuestCheckOutAdapter(guestsList, getBaseActivity());
        guestAdapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(guestAdapter);
    }

    private void setUpSearch() {

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty() || s.toString().length() >= 2) {
                    doSearch(s.toString());
                }
            }
        });

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                return true;
            }
            return false;
        });

    }

    private void doSearch(String search) {
        scrollListener.onDataCleared();
        guestsList.clear();
        serviceProviderList.clear();
        houseKeepingList.clear();
        this.page = 0;
        getViewModel().getGuestListData(page, search.trim(),listOf);
    }

    @Override
    public void onExpectedGuestSuccess(List<Guests> tmpGuestsList) {
        if (page == 0) guestsList.clear();

        guestsList.addAll(tmpGuestsList);
        guestAdapter.notifyDataSetChanged();

        if (listOf == 0) listener.totalCount(guestsList.size());
    }

    @Override
    public void onExpectedHKSuccess(List<HouseKeeping> tmpHouseKeepingList) {
        if (page == 0) houseKeepingList.clear();

        houseKeepingList.addAll(tmpHouseKeepingList);
        houseKeepingAdapter.notifyDataSetChanged();
        if (listOf == 1) listener.totalCount(houseKeepingList.size());
    }

    @Override
    public void onExpectedSPSuccess(List<ServiceProvider> tmpSPList) {
        if (page == 0) serviceProviderList.clear();

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
        doSearch(etSearch.getText().toString());
    }

    private void updateUI() {
        getViewDataBinding().swipeToRefresh.setRefreshing(true);
        doSearch(etSearch.getText().toString());
    }

    public interface OnFragmentInteraction{
        void totalCount(int size);
    }
}
