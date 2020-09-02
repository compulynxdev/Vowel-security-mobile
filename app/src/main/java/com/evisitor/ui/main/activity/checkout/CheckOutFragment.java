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
import com.evisitor.ui.main.activity.checkout.adapter.GuestCheckOutAdapter;
import com.evisitor.ui.main.activity.checkout.adapter.HouseKeepingCheckOutAdapter;
import com.evisitor.ui.main.activity.checkout.adapter.ServiceProviderCheckOutAdapter;
import com.evisitor.ui.main.home.guest.expected.ExpectedGuestNavigator;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class CheckOutFragment extends BaseFragment<FragmentCheckOutBinding, CheckOutViewModel> implements ExpectedGuestNavigator {
    private List<Guests> list;
    private List<ServiceProvider> serviceProviders;
    private List<HouseKeeping> houseKeepings;
    private GuestCheckOutAdapter adapter;
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

    public void setList(int listOf){
        this.listOf = listOf;
        switch (listOf){
            //guest
            case 0:
                getViewDataBinding().recyclerView.setAdapter(adapter);
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

        list=new ArrayList<>();
        houseKeepings = new ArrayList<>();
        serviceProviders = new ArrayList<>();

        setUpGuestAdapter();

        setUpServiceProviderAdapter();

        setUpHouseKeeperAdapter();

        setUpSearch();

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                switch (listOf){
                    case 0 :
                        adapter.showLoading(true);
                        adapter.notifyDataSetChanged();
                        page++;
                        getViewModel().getGuestListData(page, etSearch.getText().toString().trim(),listOf);
                        break;

                    case 1 :
                        houseKeepingAdapter.showLoading(true);
                        houseKeepingAdapter.notifyDataSetChanged();
                        page++;
                        getViewModel().getGuestListData(page, etSearch.getText().toString().trim(),listOf);
                        break;

                    case 2 :
                        serviceProviderAdapter.showLoading(true);
                        serviceProviderAdapter.notifyDataSetChanged();
                        page++;
                        getViewModel().getGuestListData(page, etSearch.getText().toString().trim(),listOf);
                        break;


                }
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);
        getViewModel().getGuestListData().observe(this, guests -> {
            adapter.showLoading(false);
            adapter.notifyDataSetChanged();

            if (page == 0) list.clear();

            list.addAll(guests);
            adapter.notifyDataSetChanged();

            if (listOf==0) listener.totalCount(list.size());
        });

        getViewModel().getHouseKeepingListData().observe(this, list -> {
            houseKeepingAdapter.showLoading(false);
            houseKeepingAdapter.notifyDataSetChanged();

            if (page == 0) houseKeepings.clear();

            houseKeepings.addAll(list);
            houseKeepingAdapter.notifyDataSetChanged();
            if (listOf==1) listener.totalCount(houseKeepings.size());
        });

        getViewModel().getServiceProviderListData().observe(this, guests -> {
            serviceProviderAdapter.showLoading(false);
            serviceProviderAdapter.notifyDataSetChanged();

            if (page == 0) serviceProviders.clear();

            serviceProviders.addAll(guests);
            serviceProviderAdapter.notifyDataSetChanged();

            if (listOf==2) listener.totalCount(serviceProviders.size());
        });
        updateUI();
    }

    private void setUpHouseKeeperAdapter() {
        houseKeepingAdapter = new HouseKeepingCheckOutAdapter(houseKeepings, getBaseActivity());
        houseKeepingAdapter.setHasStableIds(true);
    }

    private void setUpServiceProviderAdapter() {
        serviceProviderAdapter = new ServiceProviderCheckOutAdapter(serviceProviders,getBaseActivity());
        serviceProviderAdapter.setHasStableIds(true);
    }

    private void setUpGuestAdapter() {
        adapter = new GuestCheckOutAdapter(list,getBaseActivity());
        adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);
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
        list.clear();
        serviceProviders.clear();
        houseKeepings.clear();
        this.page = 0;
        getViewModel().getGuestListData(page, search.trim(),listOf);
    }

    @Override
    public void hideSwipeToRefresh() {
        getViewDataBinding().swipeToRefresh.setRefreshing(false);
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
