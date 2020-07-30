package com.evisitor.ui.main.home.guest.expected;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.ActivityExpectedGuestBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.home.guest.add.AddGuestActivity;
import com.evisitor.ui.main.home.guest.add.scan.ScanIDActivity;
import com.evisitor.ui.main.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.AppConstants;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class ExpectedGuestActivity extends BaseActivity<ActivityExpectedGuestBinding,GuestViewModel> implements GuestNavigator {

    private boolean viewSearch = false;
    private List<Guests> guestsList;
    private RecyclerViewScrollListener scrollListener;
    private GuestAdapter adapter;
    private int page = 0;

    public static Intent getStartIntent(Context context){
        return new Intent(context,ExpectedGuestActivity.class);
    }
    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_expected_guest;
    }

    @Override
    public GuestViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(GuestViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_expected_guests );
        setUpSearch();
        setUpAdapter();

        getViewDataBinding().fabAdd.setOnClickListener(v -> AlertDialog.newInstance()
                .setNegativeBtnShow(true)
                .setCloseBtnShow(true)
                .setTitle(getString(R.string.check_in))
                .setMsg(getString(R.string.msg_add_guest_option))
                .setNegativeBtnColor(R.color.colorPrimary)
                .setPositiveBtnLabel(getString(R.string.manually))
                .setNegativeBtnLabel(getString(R.string.scan_id))
                .setOnNegativeClickListener(dialog1 -> {
                    dialog1.dismiss();
                    Intent i = ScanIDActivity.getStartIntent(this);
                    startActivity(i);
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    Intent i = AddGuestActivity.getStartIntent(this);
                    startActivity(i);
                }).show(getSupportFragmentManager()));
    }

    private void setUpAdapter() {
        guestsList = new ArrayList<>();
        adapter = new GuestAdapter(guestsList, this, guests -> {
            List<VisitorProfileBean> visitorProfileBeanList = getViewModel().setClickVisitorDetail(guests);
            VisitorProfileDialog.newInstance(visitorProfileBeanList, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                showCheckinOptions();
            }).setBtnLabel(getString(R.string.check_in)).show(getSupportFragmentManager());
        });
        getViewDataBinding().recyclerView.setAdapter(adapter);

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(() -> {
            doSearch(getViewDataBinding().etSearch.getText().toString());
            getViewDataBinding().swipeToRefresh.setRefreshing(false);
        });
        getData(page,"");
        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                adapter.showLoading(true);
                adapter.notifyDataSetChanged();
                page += AppConstants.LIMIT;
                getData(page, getViewDataBinding().etSearch.getText().toString());
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);
    }

    private void setUpSearch() {
        ImageView imgSearch = findViewById(R.id.img_search);
        imgSearch.setVisibility(View.VISIBLE);
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(v -> onBackPressed());
        imgSearch.setOnClickListener(v -> {
            if (!viewSearch) {
                getViewDataBinding().searchBar.setVisibility(View.VISIBLE);
                getViewDataBinding().etSearch.setText("");
                viewSearch = true;
            }
            else{
                getViewDataBinding().searchBar.setVisibility(View.GONE);
                getViewDataBinding().etSearch.setText("");
                viewSearch = false;
            }
        });

        getViewDataBinding().etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    doSearch("");
                else {
                    doSearch(s.toString());
                }
            }
        });
    }

    private void getData(int page, String search) {
        getViewModel().getGuestListData(page,search).observe(this, guests -> {
            adapter.showLoading(false);
            adapter.notifyDataSetChanged();

            if (page == 0) guestsList.clear();

            guestsList.addAll(guests);
            adapter.notifyDataSetChanged();
        });

    }
    private void doSearch(String search) {
        scrollListener.onDataCleared();
        guestsList.clear();
        this.page = 0;
        getData(page, search);
    }

    private void showCheckinOptions() {
        AlertDialog.newInstance()
                .setNegativeBtnShow(true)
                .setCloseBtnShow(true)
                .setTitle(getString(R.string.check_in))
                .setMsg(getString(R.string.msg_check_in_option))
                .setNegativeBtnColor(R.color.colorPrimary)
                .setPositiveBtnLabel(getString(R.string.approve_by_call))
                .setNegativeBtnLabel(getString(R.string.send_notification))
                .setOnNegativeClickListener(dialog1 -> {
                    dialog1.dismiss();
                    getViewModel().sendNotification();
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    showCallDialog();
                }).show(getSupportFragmentManager());

    }

    private void showCallDialog() {
        AlertDialog.newInstance()
                .setNegativeBtnShow(true)
                .setCloseBtnShow(true)
                .setTitle(getString(R.string.check_in))
                .setMsg(getString(R.string.msg_check_in_call))
                .setPositiveBtnLabel(getString(R.string.approve))
                .setNegativeBtnLabel(getString(R.string.reject))
                .setOnNegativeClickListener(dialog1 -> {
                    dialog1.dismiss();
                    showAlert(R.string.alert,R.string.check_in_rejected);
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    getViewModel().approveByCall();
                }).show(getSupportFragmentManager());
    }

    @Override
    public void refreshList() {
        doSearch(getViewDataBinding().etSearch.getText().toString());
    }
}
