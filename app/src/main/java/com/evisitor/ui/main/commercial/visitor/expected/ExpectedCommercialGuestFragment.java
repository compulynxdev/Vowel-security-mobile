package com.evisitor.ui.main.commercial.visitor.expected;

import static android.app.Activity.RESULT_OK;
import static com.evisitor.util.AppConstants.ADD_FAMILY_MEMBER;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.CheckInTemperature;
import com.evisitor.data.model.CommercialVisitorResponse;
import com.evisitor.data.model.MrzResponse;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.FragmentExpectedCommercialGuestBinding;
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

public class ExpectedCommercialGuestFragment extends BaseFragment<FragmentExpectedCommercialGuestBinding, ExpectedCommercialGuestViewModel> implements ExpectedCommercialGuestNavigator {
    private static final int MRZ_RESULT = 104;
    private static final int ID_RESULT = 105;
    private static final int SCAN_RESULT = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private final List<CheckInTemperature> guestIds = new ArrayList<>();
    private List<CommercialVisitorResponse.CommercialGuest> guestsList;
    private RecyclerViewScrollListener scrollListener;
    private ExpectedCommercialGuestAdapter adapter;
    private String search = "";
    private int page = 0;

    public static ExpectedCommercialGuestFragment newInstance() {
        ExpectedCommercialGuestFragment fragment = new ExpectedCommercialGuestFragment();
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
        return R.layout.fragment_expected_commercial_guest;
    }

    @Override
    public ExpectedCommercialGuestViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(ExpectedCommercialGuestViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        guestsList = new ArrayList<>();
        adapter = new ExpectedCommercialGuestAdapter(guestsList, getContext(), pos -> {
            CommercialVisitorResponse.CommercialGuest guestBean = guestsList.get(pos);
            List<VisitorProfileBean> visitorProfileBeanList = mViewModel.setClickVisitorDetail(guestBean);
            VisitorProfileDialog.newInstance(visitorProfileBeanList, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                decideNextProcess();
            }).setBtnLabel(getString(R.string.check_in)).setBtnVisible(guestBean.getStatus().equalsIgnoreCase("PENDING"))
                    .setImage(guestBean.getImageUrl()).setIsCommercialGuest(true).checkInIsApproved(guestBean.isCheckInApproved()).show(getFragmentManager());
        });
        adapter.setHasStableIds(true);
        getViewDataBinding().recyclerView.setAdapter(adapter);

        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                setAdapterLoading(true);
                page++;

                mViewModel.getExpectedVisitorListData(page, search);
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);
        updateUI();
    }

    private void decideNextProcess() {
        CommercialVisitorResponse.CommercialGuest tmpBean = mViewModel.getDataManager().getCommercialVisitorDetail();


        if (tmpBean.isVip()) {
            doIfCheckInApprovedOrVip(tmpBean);
        } else {
            if (!mViewModel.getDataManager().isIdentifyFeature() || tmpBean.getIdentityNo().isEmpty()) {
                if (tmpBean.getGuestList().isEmpty()) showCheckinOptions();
                else showSecondaryGuestListForCheckIn();
            } else {
                IdVerificationDialog.newInstance(new IdVerificationCallback() {
                    @Override
                    public void onScanClick(IdVerificationDialog dialog) {
                        dialog.dismiss();
                        branchByIdType(tmpBean);
                    }

                    @Override
                    public void onSubmitClick(IdVerificationDialog dialog, String id) {
                        dialog.dismiss();
                        if (tmpBean.getIdentityNo().equalsIgnoreCase(id)) {
                            if (tmpBean.isCheckInApproved()) {
                                doIfCheckInApprovedOrVip(tmpBean);
                            } else if (tmpBean.getGuestList().isEmpty()) showCheckinOptions();
                            else showSecondaryGuestListForCheckIn();
                        } else {
                            showToast(R.string.alert_id);
                        }
                    }
                }).show(getFragmentManager());
            }
        }
    }


    /**
     * @param guestBean
     */
    private void branchByIdType(CommercialVisitorResponse.CommercialGuest guestBean) {
        Intent i;
        switch (guestBean.getDocumentType()) {
            case "passport":
                i = ScanSmartActivity.getStartIntent(getContext());
                startActivityForResult(i, SCAN_RESULT);
                break;
            case "dl":
                i = ScanSmartActivity.getStartIntent(getContext());
                startActivityForResult(i, SCAN_RESULT);
                break;
            default:
                i = ScanSmartActivity.getStartIntent(getContext());
                startActivityForResult(i, SCAN_RESULT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CommercialVisitorResponse.CommercialGuest tmpBean = mViewModel.getDataManager().getCommercialVisitorDetail();

        if (resultCode == RESULT_OK) {
            if (requestCode == SCAN_RESULT /*&& data != null*/) {
                String identityNo = MainResultStore.instance.getScannedIDData().idNumber;
                String fullName = MainResultStore.instance.getScannedIDData().name;

                if (tmpBean.getIdentityNo().equalsIgnoreCase(identityNo)) {

                    if (tmpBean.getName().equalsIgnoreCase(fullName)) {

                        if (tmpBean.isCheckInApproved()) {
                            doIfCheckInApprovedOrVip(tmpBean);
                            return;
                        }
                        if (tmpBean.getGuestList().isEmpty()) showCheckinOptions();
                        else showSecondaryGuestListForCheckIn();
                    } else {
                        showToast(R.string.alert_invalid_name);
                    }
                } else {
                    showToast(R.string.alert_invalid_id);
                }
            } else if (requestCode == ADD_FAMILY_MEMBER) {
                Type listType = new TypeToken<List<CheckInTemperature>>() {
                }.getType();
                guestIds.clear();
                guestIds.addAll(Objects.requireNonNull(mViewModel.getDataManager().getGson().fromJson(data.getStringExtra("data"), listType)));
                if (tmpBean.isCheckInApproved()) {
                    mViewModel.acceptIfCheckInISApproved(guestIds);
                } else {
                    showCheckinOptions();
                }
            } else if (requestCode == MRZ_RESULT) {

                assert data != null;
                MrzResponse mrzResponse = new MrzResponse(data.getStringExtra("result"));
                System.out.println(mrzResponse);
            } else if (requestCode == ID_RESULT) {

                //TODO @KELVIN


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
        mViewModel.getExpectedVisitorListData(page, search);
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

        CommercialVisitorResponse.CommercialGuest bean = mViewModel.getDataManager().getCommercialVisitorDetail();
        if (bean.getHouseNo().isEmpty()) {
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
                .setMsg(getString(R.string.commercial_msg_check_in_call))
                .setPositiveBtnLabel(getString(R.string.approve))
                .setNegativeBtnLabel(getString(R.string.reject))
                .setOnNegativeClickListener(dialog1 -> {
                    dialog1.dismiss();
                    showReasonDialog();
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    mViewModel.approveByCall(true, null, guestIds);
                }).show(getFragmentManager());
    }

    private void showReasonDialog() {
        InputDialog.newInstance().setTitle(getString(R.string.are_you_sure))
                .setOnPositiveClickListener((dialog, input) -> {
                    dialog.dismiss();
                    mViewModel.approveByCall(false, input, guestIds);
                }).show(getFragmentManager());
    }

    @Override
    public void onExpectedGuestSuccess(List<CommercialVisitorResponse.CommercialGuest> tmpGuestsList) {
        if (this.page == 0) guestsList.clear();

        guestsList.addAll(tmpGuestsList);
        adapter.notifyItemRangeChanged(0, tmpGuestsList.size());

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

    private void showSecondaryGuestListForCheckIn() {
        CommercialVisitorResponse.CommercialGuest tmpBean = mViewModel.getDataManager().getCommercialVisitorDetail();
        Intent i = SecondaryGuestInputActivity.getStartIntent(getBaseActivity());
        if (!tmpBean.getGuestList().isEmpty()) {
            i.putExtra("list", new Gson().toJson(tmpBean.guestList));
        }
        i.putExtra("checkIn", true);
        i.putExtra("checkInApproved", tmpBean.isCheckInApproved());
        startActivityForResult(i, ADD_FAMILY_MEMBER);
    }

    @Override
    public void refreshList() {
        getViewDataBinding().recyclerView.getRecycledViewPool().clear();
        doSearch(search);
    }

    private void doIfCheckInApprovedOrVip(CommercialVisitorResponse.CommercialGuest tmpBean) {
        if (tmpBean.getGuestList().isEmpty())
            mViewModel.acceptIfCheckInISApproved(guestIds);
        else showSecondaryGuestListForCheckIn();
    }

}
