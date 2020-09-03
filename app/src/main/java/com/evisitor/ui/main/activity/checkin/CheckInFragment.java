package com.evisitor.ui.main.activity.checkin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseKeeping;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.FragmentCheckInBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.activity.checkin.adapter.GuestCheckInAdapter;
import com.evisitor.ui.main.activity.checkin.adapter.HouseKeepingCheckInAdapter;
import com.evisitor.ui.main.activity.checkin.adapter.ServiceProviderCheckInAdapter;
import com.evisitor.ui.main.home.guest.expected.ExpectedGuestNavigator;
import com.evisitor.ui.main.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class CheckInFragment extends BaseFragment<FragmentCheckInBinding, CheckInViewModel> implements ExpectedGuestNavigator {

    private List<Guests> list;
    private List<ServiceProvider> serviceProviders;
    private List<HouseKeeping> houseKeepings;
    private GuestCheckInAdapter adapter;
    private ServiceProviderCheckInAdapter serviceProviderAdapter;
    private HouseKeepingCheckInAdapter houseKeepingAdapter;
    private EditText etSearch;
    private OnFragmentInteraction listener;
    private RecyclerViewScrollListener scrollListener;
    private int page;
    private int listOf=0;
     public static CheckInFragment newInstance(EditText et_Search,int listOf,OnFragmentInteraction listener) {
        CheckInFragment fragment = new CheckInFragment();
        Bundle args = new Bundle();
         fragment.setData(et_Search, listOf,listener);
        fragment.setArguments(args);
        return fragment;
    }

    private void setData(EditText et_Search, int listOf, OnFragmentInteraction interaction){
        etSearch=et_Search;
        this.listOf = listOf;
        listener = interaction;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().setCheckInOutNavigator(this);

        list=new ArrayList<>();
        houseKeepings = new ArrayList<>();
        serviceProviders = new ArrayList<>();

        setUpGuestAdapter();

        setUpServiceProviderAdapter();

        setUpHouseKeeperAdapter();

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);


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

            if (listOf==0)  listener.totalCount(list.size());
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
        houseKeepingAdapter = new HouseKeepingCheckInAdapter(houseKeepings, getBaseActivity(), houseKeeping -> {
            List<VisitorProfileBean> beans = getViewModel().getHouseKeepingCheckInProfileBean(houseKeeping);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                if (houseKeeping.isCheckOutFeature() && !houseKeeping.isHostCheckOut())
                    showCallDialog(1);
                else getViewModel().checkOut(1);
            }).setBtnLabel(getString(R.string.check_out)).show(getFragmentManager());
        });
     }

    private void setUpServiceProviderAdapter() {
        serviceProviderAdapter = new ServiceProviderCheckInAdapter(serviceProviders, getBaseActivity(), serviceProvider -> {
            List<VisitorProfileBean> beans = getViewModel().getServiceProviderCheckInProfileBean(serviceProvider);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                if (serviceProvider.isCheckOutFeature() && !serviceProvider.isHostCheckOut())
                    showCallDialog(2);
                else getViewModel().checkOut(2);
            }).setBtnLabel(getString(R.string.check_out)).show(getFragmentManager());
        });
     }

    private void setUpGuestAdapter() {
        adapter = new GuestCheckInAdapter(list, getBaseActivity(), pos -> {
            Guests guests = list.get(pos);
            List<VisitorProfileBean> beans = getViewModel().getGuestCheckInProfileBean(guests);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                if (guests.isCheckOutFeature() && !guests.isHostCheckOut())
                    showCallDialog(0);
                else getViewModel().checkOut(0);
            }).setBtnLabel(getString(R.string.check_out)).show(getFragmentManager());
        });

        getViewDataBinding().recyclerView.setAdapter(adapter);
    }

    private void showCallDialog(int type) {
        AlertDialog.newInstance()
                .setNegativeBtnShow(true)
                .setCloseBtnShow(true)
                .setTitle(getString(R.string.check_out))
                .setMsg(getString(R.string.msg_check_in_call))
                .setPositiveBtnLabel(getString(R.string.approve))
                .setNegativeBtnLabel(getString(R.string.reject))
                .setOnNegativeClickListener(dialog1 -> {
                    dialog1.dismiss();
                    showAlert(R.string.alert, R.string.check_out_rejected);
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    getViewModel().checkOut(type);
                }).show(getFragmentManager());
    }

    private void setUpSearch() {
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
