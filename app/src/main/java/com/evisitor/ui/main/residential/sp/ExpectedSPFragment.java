package com.evisitor.ui.main.residential.sp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bixolon.labelprinter.BixolonLabelPrinter;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.PropertyInfoResponse;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.FragmentExpectedBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.base.DialogManager;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.home.idverification.IdVerificationCallback;
import com.evisitor.ui.main.home.idverification.IdVerificationDialog;
import com.evisitor.ui.main.home.rejectreason.InputDialog;
import com.evisitor.ui.main.home.visitorprofile.VisitorProfileDialog;
import com.evisitor.util.AppConstants;
import com.evisitor.util.AppUtils;
import com.evisitor.util.pagination.RecyclerViewScrollListener;
import com.smartengines.MainResultStore;
import com.smartengines.ScanSmartActivity;

import org.xml.sax.HandlerBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class ExpectedSPFragment extends BaseFragment<FragmentExpectedBinding, ExpectedSpViewModel> implements ExpectedSPNavigator {
    private final int SCAN_RESULT = 101;
    private RecyclerViewScrollListener scrollListener;
    private ExpectedSPAdapter adapter;
    private String search = "";
    private int page = 0;
    private List<ServiceProvider> spList;
    public static BixolonLabelPrinter bixolonLabelPrinter;
    private boolean mIsConnected = false;
    private PropertyInfoResponse propertyInfo;
    private final Handler connectHandler = new Handler();


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

        bixolonLabelPrinter = new BixolonLabelPrinter(getActivity(), mHandler, Looper.getMainLooper());
        final int ANDROID_NOUGAT = 24;

        if(Build.VERSION.SDK_INT >= ANDROID_NOUGAT)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getViewModel().getPropertyInfo().observe(this, new Observer<PropertyInfoResponse>() {
            @Override
            public void onChanged(PropertyInfoResponse propertyInfoResponse) {
                propertyInfo = propertyInfoResponse;
            }
        });
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
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(ExpectedSpViewModel.class);
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

                            if (tmpBean.getIdentityNo().equalsIgnoreCase(id))
                                showCheckinOptions();
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
                            showCheckinOptions();
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



    @Override
    public void printLabel() {

                if(mIsConnected){
                    int mFontSize = BixolonLabelPrinter.FONT_SIZE_10;
                    int mHorizontalMultiplier = 1;
                    int mVerticalMultiplier = 1;
                    int model = BixolonLabelPrinter.QR_CODE_MODEL2;
                    int eccLevel = BixolonLabelPrinter.ECC_LEVEL_15;
                    int rotation = BixolonLabelPrinter.ROTATION_NONE;
                    //Bitmap logo = urlImageToBitmap();

                    ServiceProvider serviceProvider = getViewModel().getDataManager().getSpDetail();
                    bixolonLabelPrinter.beginTransactionPrint();

                   /* if(propertyInfo!=null && !propertyInfo.getImage().isEmpty())
                       bixolonLabelPrinter.drawImageFile(propertyInfo.getImage(),200,10,80,0,0,0);*/

                    bixolonLabelPrinter.drawBlock(10,30,800,33,79,3);
                    bixolonLabelPrinter.drawText("Commercial Complex", 10, 50, BixolonLabelPrinter.FONT_SIZE_12,
                            mHorizontalMultiplier, mVerticalMultiplier, 0, 0, false, true, 70);
                    bixolonLabelPrinter.drawBlock(10,90,800,93,79,3);

                    //User details to be printed
                    bixolonLabelPrinter.drawText(getString(R.string.data_name,serviceProvider.getName()), 10, 130, mFontSize,
                            mHorizontalMultiplier, mVerticalMultiplier, 0, 0, false, true, 70);
                    bixolonLabelPrinter.drawText(getString(R.string.data_identity,serviceProvider.getIdentityNo()), 10, 170, mFontSize,
                            mHorizontalMultiplier, mVerticalMultiplier, 0, 0, false, true, 70);
                    bixolonLabelPrinter.drawText("Type: Service Provider", 10, 210, mFontSize,
                            mHorizontalMultiplier, mVerticalMultiplier, 0, 0, false, true, 70);

                    //QR code
                    //bixolonLabelPrinter.drawQrCode(serviceProvider.getIdentityNo(), 400, 130, model, eccLevel, 7, rotation);
                    bixolonLabelPrinter.print(1, 1);
                    bixolonLabelPrinter.endTransactionPrint();
                }else{
                    //bixolonLabelPrinter.findBluetoothPrinters();
                    showAlert(R.string.alert,R.string.no_printer_connected);
                }
           }

    @Override
    public void showPrintDialog() {
      AlertDialog alertDialog =  AlertDialog.newInstance().setMsg(getString(R.string.do_you_want_print_label)).setNegativeBtnLabel(getString(R.string.connect_with_printer)).setPositiveBtnLabel(getString(R.string.yes)).setOnPositiveClickListener(new AlertDialog.PositiveListener() {
           @Override
           public void onPositiveClick(AlertDialog dialog) {
                     dialog.dismiss();
                     printLabel();
           }
       }).setOnNegativeClickListener(dialog -> bixolonLabelPrinter.findBluetoothPrinters()).setTitle(getString(R.string.printer).concat(String.valueOf(mIsConnected)));
      alertDialog.show(getChildFragmentManager());
    }

    private final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case BixolonLabelPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1)
                    {
                        case BixolonLabelPrinter.STATE_CONNECTED:
                            //setStatus(getString(R.string.title_connected_to, connectedDeviceName));
                            mIsConnected = true;
                            //invalidateOptionsMenu();
                            break;

                        case BixolonLabelPrinter.STATE_CONNECTING:
                            //setStatus(getResources().getString(R.string.title_connecting));
                            break;

                        case BixolonLabelPrinter.STATE_NONE:
                            //Log.e("NONE", msg.toString());
                           // setStatus(getResources().getString(R.string.title_not_connected));
                            mIsConnected = false;
                            //invalidateOptionsMenu();
                            break;
                    }
                    break;

                case BixolonLabelPrinter.MESSAGE_READ:
                    ExpectedSPFragment.dispatchMessage(msg);
                    break;

                case BixolonLabelPrinter.MESSAGE_DEVICE_NAME:
                    //connectedDeviceName = msg.getData().getString(BixolonLabelPrinter.DEVICE_NAME);
                    Toast.makeText(getActivity(), "connectedDeviceName", Toast.LENGTH_LONG).show();
                    break;

                case BixolonLabelPrinter.MESSAGE_TOAST:
                    Toast.makeText(getActivity(), msg.getData().getString(BixolonLabelPrinter.TOAST), Toast.LENGTH_SHORT).show();
                    break;

                case BixolonLabelPrinter.MESSAGE_LOG:
                    Toast.makeText(getActivity(), msg.getData().getString(BixolonLabelPrinter.LOG), Toast.LENGTH_SHORT).show();
                    break;

                case BixolonLabelPrinter.MESSAGE_BLUETOOTH_DEVICE_SET:
                    if(msg.obj == null)
                    {
                        Toast.makeText(getActivity(), "No paired device", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        DialogManager.showBluetoothDialog(getActivity(), (Set<BluetoothDevice>) msg.obj);
                    }
                    break;

                case BixolonLabelPrinter.MESSAGE_USB_DEVICE_SET:
                    if(msg.obj == null)
                    {
                        Toast.makeText(getActivity(), "No connected device", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case BixolonLabelPrinter.MESSAGE_OUTPUT_COMPLETE:
                    Toast.makeText(getActivity(), "Transaction Print complete", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    @SuppressLint("HandlerLeak")
    private static void dispatchMessage(Message msg)
    {
        switch (msg.arg1)
        {
            case BixolonLabelPrinter.PROCESS_GET_STATUS:
                byte[] report = (byte[]) msg.obj;
                StringBuffer buffer = new StringBuffer();
                if((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_PAPER_EMPTY) == BixolonLabelPrinter.STATUS_1ST_BYTE_PAPER_EMPTY)
                {
                    buffer.append("Paper Empty.\n");
                }
                if((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_COVER_OPEN) == BixolonLabelPrinter.STATUS_1ST_BYTE_COVER_OPEN)
                {
                    buffer.append("Cover open.\n");
                }
                if((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_CUTTER_JAMMED) == BixolonLabelPrinter.STATUS_1ST_BYTE_CUTTER_JAMMED)
                {
                    buffer.append("Cutter jammed.\n");
                }
                if((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_TPH_OVERHEAT) == BixolonLabelPrinter.STATUS_1ST_BYTE_TPH_OVERHEAT)
                {
                    buffer.append("TPH(thermal head) overheat.\n");
                }
                if((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_AUTO_SENSING_FAILURE) == BixolonLabelPrinter.STATUS_1ST_BYTE_AUTO_SENSING_FAILURE)
                {
                    buffer.append("Gap detection error. (Auto-sensing failure)\n");
                }
                if((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_RIBBON_END_ERROR) == BixolonLabelPrinter.STATUS_1ST_BYTE_RIBBON_END_ERROR)
                {
                    buffer.append("Ribbon end error.\n");
                }

                if(report.length == 2)
                {
                    if((report[1] & BixolonLabelPrinter.STATUS_2ND_BYTE_BUILDING_IN_IMAGE_BUFFER) == BixolonLabelPrinter.STATUS_2ND_BYTE_BUILDING_IN_IMAGE_BUFFER)
                    {
                        buffer.append("On building label to be printed in image buffer.\n");
                    }
                    if((report[1] & BixolonLabelPrinter.STATUS_2ND_BYTE_PRINTING_IN_IMAGE_BUFFER) == BixolonLabelPrinter.STATUS_2ND_BYTE_PRINTING_IN_IMAGE_BUFFER)
                    {
                        buffer.append("On printing label in image buffer.\n");
                    }
                    if((report[1] & BixolonLabelPrinter.STATUS_2ND_BYTE_PAUSED_IN_PEELER_UNIT) == BixolonLabelPrinter.STATUS_2ND_BYTE_PAUSED_IN_PEELER_UNIT)
                    {
                        buffer.append("Issued label is paused in peeler unit.\n");
                    }
                }
                if(buffer.length() == 0)
                {
                    buffer.append("No error");
                }
                Toast.makeText(newInstance().getContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
                break;
            case BixolonLabelPrinter.PROCESS_GET_INFORMATION_MODEL_NAME:
            case BixolonLabelPrinter.PROCESS_GET_INFORMATION_FIRMWARE_VERSION:
            case BixolonLabelPrinter.PROCESS_EXECUTE_DIRECT_IO:
                Toast.makeText(newInstance().getContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
