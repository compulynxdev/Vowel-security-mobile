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
import com.evisitor.ui.main.activity.checkin.adapter.GuestCheckInAdapter;
import com.evisitor.ui.main.activity.checkin.adapter.HouseKeepingCheckInAdapter;
import com.evisitor.ui.main.activity.checkin.adapter.ServiceProviderCheckInAdapter;
import com.evisitor.ui.main.home.guest.expected.GuestNavigator;
import com.evisitor.ui.main.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class CheckInFragment extends BaseFragment<FragmentCheckInBinding,CheckInViewModel> implements GuestNavigator {
    private List<Guests> list;
    private List<ServiceProvider> serviceProviders;
    private List<HouseKeeping> houseKeepings;
    private GuestCheckInAdapter adapter;
    private ServiceProviderCheckInAdapter serviceProviderAdapter;
    private HouseKeepingCheckInAdapter houseKeepingAdapter;
    private static TabLayout tabLayout;
    private static EditText etSearch;
    private RecyclerViewScrollListener scrollListener;
    private int page;
    private int listOf=0;
     public static CheckInFragment newInstance(EditText et_Search, TabLayout tab_Layout) {
        CheckInFragment fragment = new CheckInFragment();
        Bundle args = new Bundle();
        tabLayout=tab_Layout;
        etSearch = et_Search;
        fragment.setArguments(args);
        return fragment;
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
        getViewModel().setNavigator(this);

        list=new ArrayList<>();
        houseKeepings = new ArrayList<>();
        serviceProviders = new ArrayList<>();


        setUpGuestAdapter();

        setUpServiceProviderAdapter();

        setUpHouseKeeperAdapter();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    //guest
                    case 0:
                        listOf = 0;
                        getViewDataBinding().recyclerView.setAdapter(adapter);
                        doSearch(etSearch.getText().toString());
                        break;
                    //house keeping
                    case 1:
                        listOf=1;
                        getViewDataBinding().recyclerView.setAdapter(houseKeepingAdapter);
                        doSearch(etSearch.getText().toString());
                        break;
                    //service provider
                    case 2 :
                        listOf = 2;
                        getViewDataBinding().recyclerView.setAdapter(serviceProviderAdapter);
                        doSearch(etSearch.getText().toString());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
        });

        getViewModel().getHouseKeepingListData().observe(this, list -> {
            houseKeepingAdapter.showLoading(false);
            houseKeepingAdapter.notifyDataSetChanged();

            if (page == 0) houseKeepings.clear();

            houseKeepings.addAll(list);
            houseKeepingAdapter.notifyDataSetChanged();
        });

        getViewModel().getServiceProviderListData().observe(this, guests -> {
            serviceProviderAdapter.showLoading(false);
            serviceProviderAdapter.notifyDataSetChanged();

            if (page == 0) serviceProviders.clear();

            serviceProviders.addAll(guests);
            serviceProviderAdapter.notifyDataSetChanged();
        });

    }

    private void setUpHouseKeeperAdapter() {
        houseKeepingAdapter = new HouseKeepingCheckInAdapter(houseKeepings, getBaseActivity(), guests -> {
            List<VisitorProfileBean> beans = getViewModel().getHouseKeepingCheckInProfileBean(guests);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                getViewModel().checkOut(1);
            }).setBtnLabel(getString(R.string.check_out)).show(getFragmentManager());

        });
     }

    private void setUpServiceProviderAdapter() {
        serviceProviderAdapter = new ServiceProviderCheckInAdapter(serviceProviders, getBaseActivity(), guests -> {
            List<VisitorProfileBean> beans = getViewModel().getServiceProviderCheckInProfileBean(guests);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                getViewModel().checkOut(2);
            }).setBtnLabel(getString(R.string.check_out)).show(getFragmentManager());

        });
     }

    private void setUpGuestAdapter() {
        adapter = new GuestCheckInAdapter(list, getBaseActivity(), guests -> {
            List<VisitorProfileBean> beans = getViewModel().getGuestCheckInProfileBean(guests);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                getViewModel().checkOut(0);
            }).setBtnLabel(getString(R.string.check_out)).show(getFragmentManager());

        });

        getViewDataBinding().recyclerView.setAdapter(adapter);

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
}
