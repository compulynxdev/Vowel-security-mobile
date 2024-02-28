package com.evisitor.ui.main.residential.guest.expected;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.CheckInTemperature;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.FragmentExpectedGuestBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.commercial.secondryguest.SecondaryGuestInputActivity;
import com.evisitor.ui.main.home.idverification.IdVerificationCallback;
import com.evisitor.ui.main.home.idverification.IdVerificationDialog;
import com.evisitor.ui.main.home.rejectreason.InputDialog;
import com.evisitor.ui.main.home.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.pagination.RecyclerViewScrollListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartengines.MainResultStore;
import com.smartengines.ScanSmartActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.evisitor.util.AppConstants.ADD_FAMILY_MEMBER;

public class ExpectedGuestFragment extends BaseFragment<FragmentExpectedGuestBinding, ExpectedGuestViewModel> implements ExpectedGuestNavigator {
    private final int SCAN_RESULT = 101;
    private List<Guests> guestsList;
    private RecyclerViewScrollListener scrollListener;
    private ExpectedGuestAdapter adapter;
    private String search = "";
    List<CheckInTemperature> guestIds = new ArrayList<>();
    private int page = 0;

    public static ExpectedGuestFragment newInstance() {
        ExpectedGuestFragment fragment = new ExpectedGuestFragment();
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
        return R.layout.fragment_expected_guest;
    }

    @Override
    public ExpectedGuestViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(ExpectedGuestViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        guestsList = new ArrayList<>();
        adapter = new ExpectedGuestAdapter(guestsList, getContext(), pos -> {
            Guests guestBean = guestsList.get(pos);
            List<VisitorProfileBean> visitorProfileBeanList = mViewModel.setClickVisitorDetail(guestBean);
            VisitorProfileDialog.newInstance(visitorProfileBeanList, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                decideNextProcess();
            }).setBtnLabel(getString(R.string.check_in)).setBtnVisible(guestBean.getStatus().equalsIgnoreCase("PENDING"))
                    .setImage(guestBean.getImageUrl())
                    .setVehicalNoPlateImg(guestBean.getVehicleImage()).show(getFragmentManager());

        });
        adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                setAdapterLoading(true);
                page++;

                mViewModel.getGuestListData(page, search);
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);
        updateUI();
    }

    private void decideNextProcess() {
        Guests tmpBean = mViewModel.getDataManager().getGuestDetail();
        if (tmpBean.getCheckInStatus() || tmpBean.isVip()) {
            if(tmpBean.getGuestList().isEmpty())
                mViewModel.approveByCall(true, null,guestIds);
            else showSecondaryGuestListForCheckIn();
        }
        else {
            if (!mViewModel.getDataManager().isIdentifyFeature() || tmpBean.getIdentityNo().isEmpty()) {
                if(tmpBean.getGuestList().isEmpty()) showCheckinOptions();
                else showSecondaryGuestListForCheckIn();
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
                            if(tmpBean.getGuestList().isEmpty()) showCheckinOptions();
                            else showSecondaryGuestListForCheckIn();
                        else {
                            showToast(R.string.alert_id);
                        }
                    }
                }).show(getFragmentManager());
            }
        }
    }

    private void showSecondaryGuestListForCheckIn() {
        Guests tmpBean = mViewModel.getDataManager().getGuestDetail();
        Intent i = SecondaryGuestInputActivity.getStartIntent(getBaseActivity());
        if (!tmpBean.getGuestList().isEmpty()) {
            i.putExtra("list", new Gson().toJson(tmpBean.guestList));
        }
        i.putExtra("checkIn", true);
        startActivityForResult(i, ADD_FAMILY_MEMBER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Guests tmpBean = mViewModel.getDataManager().getGuestDetail();
        if (resultCode == RESULT_OK) {
            if (requestCode == SCAN_RESULT /*&& data != null*/) {
                String identityNo = MainResultStore.instance.getScannedIDData().idNumber;
                String fullName = MainResultStore.instance.getScannedIDData().name;
              /*  MrzRecord mrzRecord = (MrzRecord) data.getSerializableExtra("Record");
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
                if (mViewModel.getDataManager().getGuestDetail().getIdentityNo().equalsIgnoreCase(identityNo)) {
                    if (mViewModel.getDataManager().getGuestDetail().getName().equalsIgnoreCase(fullName)) {
                        if(tmpBean.getGuestList().isEmpty()) showCheckinOptions();
                        else showSecondaryGuestListForCheckIn();
                    } else {
                        showToast(R.string.alert_invalid_name);
                    }
                } else {
                    showToast(R.string.alert_invalid_id);
                }
            }else if(requestCode == ADD_FAMILY_MEMBER){
                Type listType = new TypeToken<List<CheckInTemperature>>() {
                }.getType();
                guestIds.clear();
                guestIds.addAll(Objects.requireNonNull(mViewModel.getDataManager().getGson().fromJson(data.getStringExtra("data"), listType)));
                if(tmpBean.getCheckInStatus() || tmpBean.isVip()){
                    mViewModel.approveByCall(true,null,guestIds);
                }else{
                    showCheckinOptions();
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
        guestsList.clear();
        this.page = 0;
        mViewModel.getGuestListData(page, search);
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

        Guests bean = mViewModel.getDataManager().getGuestDetail();
        if (!bean.isNotificationStatus() || bean.getHouseNo().isEmpty()) {
            alert.setNegativeBtnShow(false).show(getFragmentManager());
        } else {
            alert.setNegativeBtnColor(R.color.colorPrimary)
                    .setNegativeBtnShow(true)
                    .setNegativeBtnLabel(getString(R.string.send_notification))
                    .setOnNegativeClickListener(dialog1 -> {
                        dialog1.dismiss();
                        mViewModel.sendNotification(guestIds);
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
                    mViewModel.approveByCall(true, null,guestIds);
                }).show(getFragmentManager());
    }

    private void showReasonDialog() {
        InputDialog.newInstance().setTitle(getString(R.string.are_you_sure))
                .setOnPositiveClickListener((dialog, input) -> {
                    dialog.dismiss();
                    mViewModel.approveByCall(false, input,guestIds);
                }).show(getFragmentManager());
    }

    @Override
    public void onExpectedGuestSuccess(List<Guests> tmpGuestsList) {
        if (this.page == 0) guestsList.clear();

        guestsList.addAll(tmpGuestsList);
        adapter.notifyDataSetChanged();

        if (guestsList.size() == 0) {
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
