package com.evisitor.ui.main.home.sp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.SPResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.ActivityExpectedSpBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.idverification.IdVerificationCallback;
import com.evisitor.ui.main.idverification.IdVerificationDialog;
import com.evisitor.ui.main.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class ExpectedSPActivity extends BaseActivity<ActivityExpectedSpBinding, SPViewModel> implements SPNavigator {

    private List<SPResponse.ContentBean> spList;
    private RecyclerViewScrollListener scrollListener;
    private SPAdapter adapter;
    private int page = 0;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ExpectedSPActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_expected_sp;
    }

    @Override
    public SPViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(SPViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_service_provider);
        setUpAdapter();
        setUpSearch();
    }

    private void setUpAdapter() {
        spList = new ArrayList<>();
        adapter = new SPAdapter(spList, pos -> {
            List<VisitorProfileBean> visitorProfileBeanList = getViewModel().setClickVisitorDetail(spList.get(pos));
            VisitorProfileDialog.newInstance(visitorProfileBeanList, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                IdVerificationDialog.newInstance(new IdVerificationCallback() {
                    @Override
                    public void onScanClick(IdVerificationDialog dialog) {
                        showCheckinOptions();
                    }

                    @Override
                    public void onSubmitClick(IdVerificationDialog dialog) {
                        showCheckinOptions();
                    }
                }).show(getSupportFragmentManager());
            }).setBtnLabel(getString(R.string.check_in)).show(getSupportFragmentManager());
        });
        adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                adapter.showLoading(true);
                adapter.notifyDataSetChanged();
                page++;

                getViewModel().getSpListData(page, getViewDataBinding().etSearch.getText().toString().trim());
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);

        getViewModel().getSpListData().observe(this, spList -> {
            adapter.showLoading(false);
            adapter.notifyDataSetChanged();

            if (page == 0) this.spList.clear();

            this.spList.addAll(spList);
            adapter.notifyDataSetChanged();
        });

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);
        updateUI();
    }

    private void updateUI() {
        getViewDataBinding().swipeToRefresh.setRefreshing(true);
        doSearch(getViewDataBinding().etSearch.getText().toString());
    }

    private void setUpSearch() {
        ImageView imgSearch = findViewById(R.id.img_search);
        imgSearch.setVisibility(View.VISIBLE);
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(v -> onBackPressed());
        imgSearch.setOnClickListener(v -> {
            hideKeyboard();
            getViewDataBinding().searchBar.setVisibility(getViewDataBinding().searchBar.getVisibility() == View.GONE
                    ? View.VISIBLE : View.GONE);

            if (!getViewDataBinding().etSearch.getText().toString().trim().isEmpty()) {
                getViewDataBinding().etSearch.setText("");
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
                if (s.toString().isEmpty() || s.toString().length() >= 2) {
                    doSearch(s.toString());
                }
            }
        });

        getViewDataBinding().etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                return true;
            }
            return false;
        });
    }

    private void doSearch(String search) {
        scrollListener.onDataCleared();
        spList.clear();
        this.page = 0;
        getViewModel().getSpListData(page, search.trim());
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
                    showAlert(R.string.alert, R.string.check_in_rejected);
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    getViewModel().approveByCall();
                }).show(getSupportFragmentManager());
    }

    @Override
    public void hideSwipeToRefresh() {
        getViewDataBinding().swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void refreshList() {
        doSearch(getViewDataBinding().etSearch.getText().toString());
    }
}
