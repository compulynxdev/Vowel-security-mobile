package com.evisitor.ui.main.commercial.visitor.staff.expected;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.OfficeStaffResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.FragmentExpectedBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.main.home.idverification.IdVerificationCallback;
import com.evisitor.ui.main.home.idverification.IdVerificationDialog;
import com.evisitor.ui.main.home.scan.ScanIDActivity;
import com.evisitor.ui.main.home.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;
import com.sharma.mrzreader.MrzRecord;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ExpectedOfficeStaffFragment extends BaseFragment<FragmentExpectedBinding, ExpectedOfficeStaffViewModel> implements ExpectedOfficeStaffNavigator {
    private final int SCAN_RESULT = 101;
    private RecyclerViewScrollListener scrollListener;
    private ExpectedOfficeStaffAdapter adapter;
    private String search = "";
    private int page = 0;
    private List<OfficeStaffResponse.ContentBean> list;

    public static ExpectedOfficeStaffFragment newInstance() {
        ExpectedOfficeStaffFragment fragment = new ExpectedOfficeStaffFragment();
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
    public ExpectedOfficeStaffViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(ExpectedOfficeStaffViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().tvNoData.setText(getString(R.string.no_expected_ofc_staff));
        setUpAdapter();
    }

    private void setUpAdapter() {
        list = new ArrayList<>();
        adapter = new ExpectedOfficeStaffAdapter(list, pos -> {
            List<VisitorProfileBean> visitorProfileBeanList = mViewModel.setClickVisitorDetail(list.get(pos));
            VisitorProfileDialog.newInstance(visitorProfileBeanList, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                decideNextProcess();
            }).setImage(list.get(pos).getImageUrl()).setBtnLabel(getString(R.string.check_in)).show(getFragmentManager());
        });
        adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                setAdapterLoading(true);
                page++;

                mViewModel.getExpectedOfficeListData(page, search);
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);
        updateUI();
    }

    private void decideNextProcess() {
        OfficeStaffResponse.ContentBean tmpBean = mViewModel.getDataManager().getOfficeStaff();
        if (tmpBean.getDocumentId().isEmpty()) {
            mViewModel.doCheckIn();
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
                        mViewModel.doCheckIn();
                    else {
                        showToast(R.string.alert_id);
                    }
                }
            }).show(getFragmentManager());
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
        mViewModel.getExpectedOfficeListData(page, search.trim());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SCAN_RESULT && data != null) {
                String identityNo = "";
                MrzRecord mrzRecord = (MrzRecord) data.getSerializableExtra("Record");
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
                }

                if (mViewModel.getDataManager().getHouseKeeping().getDocumentId().equals(identityNo))
                    mViewModel.doCheckIn();
                else {
                    showToast(R.string.alert_id);
                }
            }
        }
    }

    @Override
    public void onExpectedOFSuccess(List<OfficeStaffResponse.ContentBean> houseKeepingList) {
        if (page == 0) this.list.clear();

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
