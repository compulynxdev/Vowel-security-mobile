package com.evisitor.ui.main.residential.staff.expected;

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
import com.evisitor.ui.main.home.idverification.IdVerificationCallback;
import com.evisitor.ui.main.home.idverification.IdVerificationDialog;
import com.evisitor.ui.main.home.rejectreason.InputDialog;
import com.evisitor.ui.main.home.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;
import com.smartengines.MainResultStore;
import com.smartengines.ScanSmartActivity;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setCheckInOutNavigator(this);
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
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(ExpectedHKViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().tvNoData.setText(mViewModel.getDataManager().isCommercial() ? getString(R.string.no_expected_ofc_staff) : getString(R.string.no_expedted_hk));
        setUpAdapter();
    }

    private void setUpAdapter() {
        list = new ArrayList<>();
        adapter = new ExpectedHKAdapter(list, pos -> {
            List<VisitorProfileBean> visitorProfileBeanList = mViewModel.setClickVisitorDetail(list.get(pos));
            VisitorProfileDialog.newInstance(visitorProfileBeanList, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                decideNextProcess();
            }).setImage(list.get(pos).getImageUrl()).setDocumentImage(list.get(pos).getDocumentImage()).setBtnLabel(getString(R.string.check_in)).show(getFragmentManager());
        });
        adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                setAdapterLoading(true);
                page++;

                mViewModel.getExpectedHKListData(page, search);
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);
        updateUI();
    }

    private void decideNextProcess() {
        HouseKeepingResponse.ContentBean tmpBean = mViewModel.getDataManager().getHouseKeeping();
        if (tmpBean.getCheckInStatus()) {
            mViewModel.approveByCall(true, null);
        } else {
            if (tmpBean.getDocumentId().isEmpty()) {
                showCheckinOptions();
            } else {
                IdVerificationDialog.newInstance(new IdVerificationCallback() {
                    @Override
                    public void onScanClick(IdVerificationDialog dialog) {
                        dialog.dismiss();
                        //Intent i = ScanIDActivity.getStartIntent(getContext());
                        Intent i = ScanSmartActivity.getStartIntent(getContext());
                        startActivityForResult(i, SCAN_RESULT);
                    }

                    @Override
                    public void onSubmitClick(IdVerificationDialog dialog, String id) {
                        dialog.dismiss();

                        if (tmpBean.getDocumentId().equalsIgnoreCase(id))
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
        if (scrollListener != null) {
            scrollListener.onDataCleared();
        }
        list.clear();
        this.page = 0;
        mViewModel.getExpectedHKListData(page, search.trim());
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


        HouseKeepingResponse.ContentBean bean = mViewModel.getDataManager().getHouseKeeping();
        if (!bean.isNotificationStatus() || bean.getFlatNo().isEmpty()) {
            alert.setNegativeBtnShow(false).show(getFragmentManager());
        } else {
            alert.setNegativeBtnColor(R.color.colorPrimary)
                    .setNegativeBtnShow(true)
                    .setNegativeBtnLabel(getString(R.string.send_notification))
                    .setOnNegativeClickListener(dialog1 -> {
                        dialog1.dismiss();
                        mViewModel.sendNotification();
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
                    showReasonDialog();
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    mViewModel.approveByCall(true, null);
                }).show(getFragmentManager());
    }

    private void showReasonDialog() {
        InputDialog.newInstance().setTitle(getString(R.string.are_you_sure))
                .setOnPositiveClickListener((dialog, input) -> {
                    dialog.dismiss();
                    mViewModel.approveByCall(false, input);
                }).show(getFragmentManager());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SCAN_RESULT /*&& data != null*/) {
                String identityNo = MainResultStore.instance.getScannedIDData().idNumber;
                String fullName = MainResultStore.instance.getScannedIDData().name;
                /*MrzRecord mrzRecord = (MrzRecord) data.getSerializableExtra("Record");
                assert mrzRecord != null;

                String code = mrzRecord.getCode1() + "" + mrzRecord.getCode2();
                switch (code.toLowerCase()) {
                    case "p<":
                    case "p":
                        identityNo = mrzRecord.getDocumentNumber();
                        break;

                    case "ac":
                    case "id":
                        identityNo = mrzRecord.getOptional2().length() == 9 ?
                                mrzRecord.getOptional2().substring(0, mrzRecord.getOptional2().length() - 1) :
                                mrzRecord.getOptional2();
                        break;
                }*/

                if (mViewModel.getDataManager().getHouseKeeping().getDocumentId().equalsIgnoreCase(identityNo)) {
                    if (mViewModel.getDataManager().getHouseKeeping().getFullName().equalsIgnoreCase(fullName)) {
                        showCheckinOptions();
                    } else {
                        showToast(R.string.alert_invalid_name);
                    }
                } else {
                    showToast(R.string.alert_invalid_id);
                }
            }
        }
    }

    @Override
    public void onExpectedHKSuccess(List<HouseKeepingResponse.ContentBean> houseKeepingList) {
        if (this.page == 0) this.list.clear();

        this.list.addAll(houseKeepingList);
        adapter.notifyDataSetChanged();

        if (this.list.size() == 0) {
            getViewDataBinding().recyclerView.setVisibility(View.GONE);
            getViewDataBinding().tvNoData.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().tvNoData.setVisibility(View.GONE);
            getViewDataBinding().recyclerView.setVisibility(View.VISIBLE);
        }
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
