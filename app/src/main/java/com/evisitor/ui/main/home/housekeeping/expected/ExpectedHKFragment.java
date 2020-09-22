package com.evisitor.ui.main.home.housekeeping.expected;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.FragmentExpectedBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.home.scan.ScanIDActivity;
import com.evisitor.ui.main.idverification.IdVerificationCallback;
import com.evisitor.ui.main.idverification.IdVerificationDialog;
import com.evisitor.ui.main.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;
import com.sharma.mrzreader.MrzRecord;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ExpectedHKFragment extends BaseFragment<FragmentExpectedBinding, ExpectedHKViewModel> implements ExpectedHKNavigator {
    private final int SCAN_RESULT = 101;
    private RecyclerViewScrollListener scrollListener;
    private ExpectedHKAdapter adapter;
    private String search = "";
    private int page = 0;
    private List<HouseKeepingResponse.ContentBean> list;

    public static ExpectedHKFragment newInstance() {
        ExpectedHKFragment fragment = new ExpectedHKFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public synchronized void setSearch(String search) {
        this.search = search;
        doSearch(search);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_expected;
    }

    @Override
    public ExpectedHKViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(ExpectedHKViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().setCheckInOutNavigator(this);
        setUpAdapter();
    }

    private void setUpAdapter() {
        list = new ArrayList<>();
        adapter = new ExpectedHKAdapter(list, pos -> {
            List<VisitorProfileBean> visitorProfileBeanList = getViewModel().setClickVisitorDetail(list.get(pos));
            VisitorProfileDialog.newInstance(visitorProfileBeanList, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                decideNextProcess();
            }).setFlatId(String.valueOf(list.get(pos).getFlatId())).setImage(list.get(pos).getImageUrl()).setBtnLabel(getString(R.string.check_in)).show(getFragmentManager());
        });
        adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                setAdapterLoading(true);
                page++;

                getViewModel().getExpectedHKListData(page, search);
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);
        updateUI();
    }

    private void decideNextProcess() {
        HouseKeepingResponse.ContentBean tmpBean = getViewModel().getDataManager().getHouseKeeping();
        if (tmpBean.getCheckInStatus()) {
            getViewModel().approveByCall(true);
        } else {
            if (tmpBean.getDocumentId().isEmpty()) {
                showCheckinOptions();
            } else {
                IdVerificationDialog.newInstance(new IdVerificationCallback() {
                    @Override
                    public void onScanClick(IdVerificationDialog dialog) {
                        dialog.dismiss();
                        Intent i = ScanIDActivity.getStartIntent(getContext());
                        startActivityForResult(i, SCAN_RESULT);
                    }

                    @Override
                    public void onSubmitClick(IdVerificationDialog dialog, String id) {
                        dialog.dismiss();

                        if (tmpBean.getDocumentId().equals(id))
                            showCheckinOptions();
                        else {
                            showToast(R.string.alert_id);
                        }
                    }
                }).show(getFragmentManager());
            }
        }
    }

    private void updateUI() {
        getViewDataBinding().swipeToRefresh.setRefreshing(true);
        doSearch(search);
    }

    private void doSearch(String search) {
        scrollListener.onDataCleared();
        list.clear();
        this.page = 0;
        getViewModel().getExpectedHKListData(page, search.trim());
    }

    private void showCheckinOptions() {
        AlertDialog alert = AlertDialog.newInstance()
                .setCloseBtnShow(true)
                .setTitle(getString(R.string.check_in))
                .setMsg(getString(R.string.msg_check_in_option))
                .setPositiveBtnLabel(getString(R.string.approve_by_call))
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    showCallDialog();
                });


        HouseKeepingResponse.ContentBean bean = getViewModel().getDataManager().getHouseKeeping();
        if (!bean.isNotificationStatus() || bean.getFlatNo().isEmpty()) {
            alert.setNegativeBtnShow(false).show(getFragmentManager());
        } else {
            alert.setNegativeBtnColor(R.color.colorPrimary)
                    .setNegativeBtnShow(true)
                    .setNegativeBtnLabel(getString(R.string.send_notification))
                    .setOnNegativeClickListener(dialog1 -> {
                        dialog1.dismiss();
                        getViewModel().sendNotification();
                    })
                    .show(getFragmentManager());
        }
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
                    getViewModel().approveByCall(false);
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    getViewModel().approveByCall(true);
                }).show(getFragmentManager());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SCAN_RESULT && data != null) {
                MrzRecord mrzRecord = (MrzRecord) data.getSerializableExtra("Record");
                assert mrzRecord != null;
                if (getViewModel().getDataManager().getHouseKeeping().getDocumentId().equals(mrzRecord.getDocumentNumber()))
                    showCheckinOptions();
                else {
                    showToast(R.string.alert_id);
                }
            }
        }
    }

    @Override
    public void onExpectedHKSuccess(List<HouseKeepingResponse.ContentBean> houseKeepingList) {
        if (page == 0) this.list.clear();

        this.list.addAll(houseKeepingList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void hideSwipeToRefresh() {
        setAdapterLoading(false);
        getViewDataBinding().swipeToRefresh.setRefreshing(false);
    }

    private void setAdapterLoading(boolean isShowLoader) {
        if (adapter != null) {
            adapter.showLoading(isShowLoader);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshList() {
        doSearch(search);
    }
}
