package com.evisitor.ui.main.residential.sp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.ServiceProvider;
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

public class ExpectedSPFragment extends BaseFragment<FragmentExpectedBinding, ExpectedSpViewModel> implements ExpectedSPNavigator {
    private final int SCAN_RESULT = 101;
    private RecyclerViewScrollListener scrollListener;
    private ExpectedSPAdapter adapter;
    private String search = "";
    private int page = 0;
    private List<ServiceProvider> spList;


    public static ExpectedSPFragment newInstance() {
        ExpectedSPFragment fragment = new ExpectedSPFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setCheckInOutNavigator(this);
    }

    public void setSearch(String search) {
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
    public ExpectedSpViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(ExpectedSpViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        spList = new ArrayList<>();
        adapter = new ExpectedSPAdapter(spList, pos -> {
            ServiceProvider spBean = spList.get(pos);
            List<VisitorProfileBean> visitorProfileBeanList = mViewModel.setClickVisitorDetail(spBean);
            VisitorProfileDialog.newInstance(visitorProfileBeanList, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                decideNextProcess();
            }).setBtnLabel(getString(R.string.check_in)).setBtnVisible(spBean.getStatus().equalsIgnoreCase("PENDING"))
                    .setImage(spBean.getImageUrl()).show(getFragmentManager());
        });
        adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                setAdapterLoading(true);
                page++;

                mViewModel.getSpListData(page, search);
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);
        updateUI();
    }

    private void decideNextProcess() {
        ServiceProvider tmpBean = mViewModel.getDataManager().getSpDetail();

        if (mViewModel.getDataManager().isCommercial()) {
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

                    if (tmpBean.getIdentityNo().equalsIgnoreCase(id))
                        mViewModel.approveByCall(true, null);
                    else {
                        showToast(R.string.alert_id);
                    }
                }
            }).show(getFragmentManager());
        } else {
            //if check in status true
            if (tmpBean.getCheckInStatus()) {
                mViewModel.approveByCall(true, null);
            } else {
                if (tmpBean.getIdentityNo().isEmpty()) {
                    notifyCheckInRequest();
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

                            if (tmpBean.getIdentityNo().equalsIgnoreCase(id))
                                notifyCheckInRequest();
                            else {
                                showToast(R.string.alert_id);
                            }
                        }
                    }).show(getFragmentManager());
                }
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
        spList.clear();
        this.page = 0;
        mViewModel.getSpListData(page, search);
    }

    private void notifyCheckInRequest() {
        AlertDialog alert = AlertDialog.newInstance()
                .setCloseBtnShow(true)
                .setTitle(getString(R.string.check_in))
                .setMsg(getString(R.string.msg_check_in_option))
                .setPositiveBtnLabel(getString(R.string.approve_by_call))
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    showCallDialog();
                });


        ServiceProvider bean = mViewModel.getDataManager().getSpDetail();
        if (!bean.isNotificationStatus() || bean.getHouseNo().isEmpty()) {
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
                .setMsg(mViewModel.getDataManager().isCommercial() ? getString(R.string.commercial_msg_check_in_call) : getString(R.string.msg_check_in_call))
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

                if (mViewModel.getDataManager().getSpDetail().getIdentityNo().equalsIgnoreCase(identityNo)) {
                    if (mViewModel.getDataManager().getSpDetail().getName().equalsIgnoreCase(fullName)) {
                        if (mViewModel.getDataManager().isCommercial()) {
                            mViewModel.approveByCall(true, null);
                        } else {
                            notifyCheckInRequest();
                        }
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
    public void onExpectedSPSuccess(List<ServiceProvider> spList) {
        if (this.page == 0) this.spList.clear();

        this.spList.addAll(spList);
        adapter.notifyDataSetChanged();

        if (this.spList.size() == 0) {
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
