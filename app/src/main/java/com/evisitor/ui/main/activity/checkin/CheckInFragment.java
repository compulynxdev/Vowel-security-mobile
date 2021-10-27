package com.evisitor.ui.main.activity.checkin;

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
import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.data.model.CommercialVisitorResponse;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseKeeping;
import com.evisitor.data.model.PropertyInfoResponse;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.FragmentCheckInBinding;
import com.evisitor.ui.base.BaseFragment;
import com.evisitor.ui.base.DialogManager;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.activity.ActivityNavigator;
import com.evisitor.ui.main.activity.checkin.adapter.CommercialStaffCheckInAdapter;
import com.evisitor.ui.main.activity.checkin.adapter.CommercialVisitorCheckInAdapter;
import com.evisitor.ui.main.activity.checkin.adapter.GuestCheckInAdapter;
import com.evisitor.ui.main.activity.checkin.adapter.HouseKeepingCheckInAdapter;
import com.evisitor.ui.main.activity.checkin.adapter.ServiceProviderCheckInAdapter;
import com.evisitor.ui.main.home.idverification.IdVerificationCallback;
import com.evisitor.ui.main.home.idverification.IdVerificationDialog;
import com.evisitor.ui.main.home.visitorprofile.VisitorProfileDialog;
import com.evisitor.ui.main.residential.sp.ExpectedSPFragment;
import com.evisitor.util.AppUtils;
import com.evisitor.util.pagination.RecyclerViewScrollListener;
import com.google.gson.reflect.TypeToken;
import com.smartengines.MainResultStore;
import com.smartengines.ScanSmartActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class CheckInFragment extends BaseFragment<FragmentCheckInBinding, CheckInViewModel> implements ActivityNavigator {
    private final int SCAN_RESULT = 101;
    private List<CommercialVisitorResponse.CommercialGuest> commercialGuestList;
    private List<Guests> guestsList;
    private List<ServiceProvider> serviceProviderList;
    private List<HouseKeeping> houseKeepingList;
    private List<CommercialStaffResponse.ContentBean> commercialStaffList;
    private CommercialStaffCheckInAdapter commercialStaffCheckInAdapter;
    private GuestCheckInAdapter guestAdapter;
    private CommercialVisitorCheckInAdapter commercialVisitorAdapter;
    private ServiceProviderCheckInAdapter serviceProviderAdapter;
    private HouseKeepingCheckInAdapter houseKeepingAdapter;
    private OnFragmentInteraction listener;
    private RecyclerViewScrollListener scrollListener;
    private int guestPage, hkPage, spPage;
    private int listOf = 0,type=0;
    private String search = "",identity="",name="";
    public static BixolonLabelPrinter bixolonLabelPrinter;
    private boolean mIsConnected = false;
    private PropertyInfoResponse propertyInfo;

    public static CheckInFragment newInstance(int listOf, OnFragmentInteraction listener) {
        CheckInFragment fragment = new CheckInFragment();
        Bundle args = new Bundle();
        fragment.setData(listOf, listener);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setCheckInOutNavigator(this);
    }

    private void setData(int listOf, OnFragmentInteraction interaction) {
        this.listOf = listOf;
        listener = interaction;
    }

    public void setCheckInList(int listOf) {
        this.listOf = listOf;
        switch (listOf) {
            //guest
            case 0:
                if (mViewModel.getDataManager().isCommercial())
                    getViewDataBinding().recyclerView.setAdapter(commercialVisitorAdapter);
                else
                    getViewDataBinding().recyclerView.setAdapter(guestAdapter);
                break;

            //house
            case 1:
                if (mViewModel.getDataManager().isCommercial())
                    getViewDataBinding().recyclerView.setAdapter(commercialStaffCheckInAdapter);
                else
                    getViewDataBinding().recyclerView.setAdapter(houseKeepingAdapter);
                break;

            //service
            case 2:
                getViewDataBinding().recyclerView.setAdapter(serviceProviderAdapter);
                break;
        }
        doSearch(search);
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        houseKeepingList = new ArrayList<>();
        serviceProviderList = new ArrayList<>();

        if (mViewModel.getDataManager().isCommercial()) {
            setUpCommercialGuestAdapter();
            setUpCommercialStaffAdapter();
        } else {
            setUpGuestAdapter();
            setUpHouseKeeperAdapter();
        }
        setUpServiceProviderAdapter();
        scrollListener = new RecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                switch (listOf) {
                    case 0:
                        setGuestAdapterLoading(true);
                        guestPage++;
                        mViewModel.getCheckInData(guestPage, search, listOf);
                        break;

                    case 1:
                        setHKAdapterLoading(true);
                        hkPage++;
                        mViewModel.getCheckInData(hkPage, search, listOf);
                        break;

                    case 2:
                        setSPAdapterLoading(true);
                        spPage++;
                        mViewModel.getCheckInData(spPage, search, listOf);
                        break;
                }
            }
        };
        getViewDataBinding().recyclerView.addOnScrollListener(scrollListener);

        getViewDataBinding().swipeToRefresh.setOnRefreshListener(this::updateUI);
        getViewDataBinding().swipeToRefresh.setColorSchemeResources(R.color.colorPrimary);
        updateUI();


        bixolonLabelPrinter = new BixolonLabelPrinter(getActivity(), mHandler, Looper.getMainLooper());
        final int ANDROID_NOUGAT = 24;

        if(Build.VERSION.SDK_INT >= ANDROID_NOUGAT)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getViewModel().getPropertyInfo().observe(this, propertyInfoResponse -> {
            propertyInfo = propertyInfoResponse;
            getViewModel().getImage(propertyInfoResponse.getImage(),true);
        });
    }

    private void setUpCommercialStaffAdapter() {
        commercialStaffList = new ArrayList<>();
        commercialStaffCheckInAdapter = new CommercialStaffCheckInAdapter(commercialStaffList, getBaseActivity(), bean -> {
            List<VisitorProfileBean> beans = mViewModel.getCommercialStaffBean(bean);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                if(mViewModel.getDataManager().isCheckOutFeature()){
                    name = bean.getFullName();
                    type=3;
                    identity = bean.getDocumentId();
                    scanIdDialog(bean.getFullName(),3);
                }
                else mViewModel.staffCheckOut();
            }).setImage(bean.getImageUrl()).setBtnLabel(getString(R.string.check_out))
                    .setVehicalNoPlateImg(bean.getVehicleImage()).show(getFragmentManager());
        });
    }

    private void setUpCommercialGuestAdapter() {
        commercialGuestList = new ArrayList<>();
        commercialVisitorAdapter = new CommercialVisitorCheckInAdapter(commercialGuestList, getBaseActivity(), new CommercialVisitorCheckInAdapter.ItemClickCallback() {
            @Override
            public void onItemClick(int pos) {
                CommercialVisitorResponse.CommercialGuest guests = commercialGuestList.get(pos);
                List<VisitorProfileBean> beans = mViewModel.getCommercialGuestProfileBean(guests);
                VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                    visitorProfileDialog.dismiss();
                    if (!guests.getHost().isEmpty() && guests.getHostCheckOutTime().isEmpty())
                        showCallDialog(0);
                    else{
                        if(mViewModel.getDataManager().isCheckOutFeature() && !guests.isMinor()){
                            name = guests.getName();
                            type=0;
                            identity = guests.getIdentityNo();
                            scanIdDialog(guests.getIdentityNo(),0);
                        }else {
                            mViewModel.checkOut(0);
                        }
                    }
                    // mViewModel.checkOut(0);
                }).setIsCommercialGuest(true).setImage(guests.getImageUrl()).setVehicalNoPlateImg(guests.getVehicleImage()).setBtnLabel(getString(R.string.check_out)).show(getFragmentManager());
            }

            @Override
            public void onPrintClick(int pos) {
                CommercialVisitorResponse.CommercialGuest guests = commercialGuestList.get(pos);
                mViewModel.getCommercialGuestProfileBean(guests);
                if(guests.getImageUrl()!=null)
                    getViewModel().getImage(guests.getImageUrl(),false);

                AlertDialog alertDialog =  AlertDialog.newInstance().setMsg(getString(R.string.connect_if_not_connected)).setNegativeBtnLabel(getString(R.string.connect_with_printer)).setPositiveBtnLabel(getString(R.string.yes)).setOnPositiveClickListener(new AlertDialog.PositiveListener() {
                    @Override
                    public void onPositiveClick(AlertDialog dialog) {
                        dialog.dismiss();
                        printLabel(false);
                    }
                }).setOnNegativeClickListener(dialog -> {
                    if(bixolonLabelPrinter!=null)
                        bixolonLabelPrinter.findBluetoothPrinters();
                }).setTitle(getString(R.string.printer));
                alertDialog.show(getChildFragmentManager());
            }
        });

        getViewDataBinding().recyclerView.setAdapter(commercialVisitorAdapter);
    }

    private void setUpHouseKeeperAdapter() {
        houseKeepingAdapter = new HouseKeepingCheckInAdapter(houseKeepingList, getBaseActivity(), houseKeeping -> {
            List<VisitorProfileBean> beans = mViewModel.getHouseKeepingProfileBean(houseKeeping);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                if (houseKeeping.isCheckOutFeature() && !houseKeeping.isHostCheckOut())
                    showCallDialog(1);
                else {
                    if(mViewModel.getDataManager().isCheckOutFeature()){
                        name = houseKeeping.getName();
                        type=1;
                        identity = houseKeeping.getIdentityNo();
                        scanIdDialog(houseKeeping.getIdentityNo(),1);
                    }else {
                        if (isNetworkConnected(true))
                            mViewModel.checkOut(1);
                    }
                }
            }).setImage(houseKeeping.getImageUrl()).setVehicalNoPlateImg(houseKeeping.getVehicleImage()).setBtnLabel(getString(R.string.check_out)).show(getFragmentManager());
        });
    }

    private void setUpServiceProviderAdapter() {
        serviceProviderAdapter = new ServiceProviderCheckInAdapter(serviceProviderList, getBaseActivity(), new ServiceProviderCheckInAdapter.OnItemClickListener() {
            @Override
            public void ItemClick(ServiceProvider serviceProvider) {
                List<VisitorProfileBean> beans = mViewModel.getServiceProviderProfileBean(serviceProvider);
                VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                    visitorProfileDialog.dismiss();
                    if (serviceProvider.isCheckOutFeature() && !serviceProvider.isHostCheckOut())
                        showCallDialog(2);
                    else{
                        if(mViewModel.getDataManager().isCheckOutFeature()){
                            name = serviceProvider.getName();
                            type=2;
                            identity = serviceProvider.getIdentityNo();
                            scanIdDialog(serviceProvider.getIdentityNo(),2);
                        }
                        else
                            mViewModel.checkOut(2);
                    }
                }).setImage(serviceProvider.getImageUrl()).setVehicalNoPlateImg(serviceProvider.getVehicleImage()).setBtnLabel(getString(R.string.check_out)).show(getFragmentManager());

            }

            @Override
            public void printClick(ServiceProvider serviceProvider) {
                mViewModel.getServiceProviderProfileBean(serviceProvider);
                if(serviceProvider.getImageUrl()!=null)
                    getViewModel().getImage(serviceProvider.getImageUrl(),false);
                AlertDialog alertDialog =  AlertDialog.newInstance().setMsg(getString(R.string.connect_if_not_connected)).setNegativeBtnLabel(getString(R.string.connect_with_printer)).setPositiveBtnLabel(getString(R.string.yes)).setOnPositiveClickListener(new AlertDialog.PositiveListener() {
                    @Override
                    public void onPositiveClick(AlertDialog dialog) {
                        dialog.dismiss();
                        printLabel(true);
                    }
                }).setOnNegativeClickListener(dialog -> bixolonLabelPrinter.findBluetoothPrinters()).setTitle(getString(R.string.printer));
                alertDialog.show(getChildFragmentManager());
            }
        });
    }

    private void setUpGuestAdapter() {
        guestsList = new ArrayList<>();
        guestAdapter = new GuestCheckInAdapter(guestsList, getBaseActivity(), pos -> {
            Guests guests = guestsList.get(pos);
            List<VisitorProfileBean> beans = mViewModel.getGuestProfileBean(guests);
            VisitorProfileDialog.newInstance(beans, visitorProfileDialog -> {
                visitorProfileDialog.dismiss();
                if (guests.isCheckOutFeature() && !guests.isHostCheckOut())
                    showCallDialog(0);
                else{
                    if(mViewModel.getDataManager().isCheckOutFeature() && !guests.isMinor()){
                        name = guests.getName();
                        type=0;
                        identity = guests.getIdentityNo();
                        scanIdDialog(guests.getIdentityNo(),0);
                    }else {
                        mViewModel.checkOut(0);
                    }
                }
            }).setImage(guests.getImageUrl()).setBtnLabel(getString(R.string.check_out)).setVehicalNoPlateImg(guests.getVehicleImage()).show(getFragmentManager());
        });

        getViewDataBinding().recyclerView.setAdapter(guestAdapter);
    }

    private void showCallDialog(int type) {
        AlertDialog.newInstance()
                .setNegativeBtnShow(false)
                .setCloseBtnShow(true)
                .setTitle(getString(R.string.check_out))
                .setMsg(mViewModel.getDataManager().isCommercial() ? getString(R.string.commercial_msg_check_out_call) : getString(R.string.msg_check_out_call))
                .setPositiveBtnLabel(getString(R.string.approve))
                /*.setNegativeBtnLabel(getString(R.string.reject))
                .setOnNegativeClickListener(dialog1 -> {
                    dialog1.dismiss();
                    showAlert(R.string.alert, R.string.check_out_rejected);
                })*/
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    if (isNetworkConnected(true))
                        mViewModel.checkOut(type);
                }).show(getFragmentManager());
    }

    public void doSearch(String search) {
        this.search = search;
        if (scrollListener != null) {
            scrollListener.onDataCleared();
        }
        switch (listOf) {
            case 0:
                if (mViewModel.getDataManager().isCommercial()) {
                    commercialGuestList.clear();
                    commercialVisitorAdapter.notifyDataSetChanged();
                } else {
                    guestsList.clear();
                    guestAdapter.notifyDataSetChanged();
                }

                guestPage = 0;
                mViewModel.getCheckInData(guestPage, search, listOf);
                break;

            case 1:
                if (mViewModel.getDataManager().isCommercial()) {
                    commercialStaffList.clear();
                    commercialStaffCheckInAdapter.notifyDataSetChanged();
                } else {
                    houseKeepingList.clear();
                    houseKeepingAdapter.notifyDataSetChanged();
                }
                hkPage = 0;
                mViewModel.getCheckInData(hkPage, search, listOf);
                break;

            case 2:
                serviceProviderList.clear();
                serviceProviderAdapter.notifyDataSetChanged();
                spPage = 0;
                mViewModel.getCheckInData(spPage, search, listOf);
                break;
        }
    }

    @Override
    public void onExpectedCommercialGuestSuccess(int page, List<CommercialVisitorResponse.CommercialGuest> tmpGuestsList) {
        if (page == 0) commercialGuestList.clear();

        commercialGuestList.addAll(tmpGuestsList);
        commercialVisitorAdapter.notifyDataSetChanged();

        if (commercialGuestList.size() == 0) {
            getViewDataBinding().recyclerView.setVisibility(View.GONE);
            getViewDataBinding().tvNoData.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().tvNoData.setVisibility(View.GONE);
            getViewDataBinding().recyclerView.setVisibility(View.VISIBLE);
        }

        if (listOf == 0) listener.totalCount(commercialGuestList.size());
    }

    @Override
    public void onExpectedGuestSuccess(int page, List<Guests> tmpGuestsList) {
        if (page == 0) guestsList.clear();

        guestsList.addAll(tmpGuestsList);
        guestAdapter.notifyDataSetChanged();

        if (guestsList.size() == 0) {
            getViewDataBinding().recyclerView.setVisibility(View.GONE);
            getViewDataBinding().tvNoData.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().tvNoData.setVisibility(View.GONE);
            getViewDataBinding().recyclerView.setVisibility(View.VISIBLE);
        }

        if (listOf == 0) listener.totalCount(guestsList.size());
    }

    @Override
    public void onExpectedHKSuccess(int page, List<HouseKeeping> tmpHouseKeepingList) {
        if (page == 0) houseKeepingList.clear();

        houseKeepingList.addAll(tmpHouseKeepingList);
        houseKeepingAdapter.notifyDataSetChanged();

        if (houseKeepingList.size() == 0) {
            getViewDataBinding().recyclerView.setVisibility(View.GONE);
            getViewDataBinding().tvNoData.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().tvNoData.setVisibility(View.GONE);
            getViewDataBinding().recyclerView.setVisibility(View.VISIBLE);
        }

        if (listOf == 1) listener.totalCount(houseKeepingList.size());
    }

    @Override
    public void onExpectedOfficeSuccess(int page, List<CommercialStaffResponse.ContentBean> staffList) {
        if (page == 0) commercialStaffList.clear();

        commercialStaffList.addAll(staffList);
        commercialStaffCheckInAdapter.notifyDataSetChanged();

        if (commercialStaffList.size() == 0) {
            getViewDataBinding().recyclerView.setVisibility(View.GONE);
            getViewDataBinding().tvNoData.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().tvNoData.setVisibility(View.GONE);
            getViewDataBinding().recyclerView.setVisibility(View.VISIBLE);
        }

        if (listOf == 1) listener.totalCount(commercialStaffList.size());
    }

    @Override
    public void onExpectedSPSuccess(int page, List<ServiceProvider> tmpSPList) {
        if (page == 0) serviceProviderList.clear();

        serviceProviderList.addAll(tmpSPList);
        serviceProviderAdapter.notifyDataSetChanged();

        if (serviceProviderList.size() == 0) {
            getViewDataBinding().recyclerView.setVisibility(View.GONE);
            getViewDataBinding().tvNoData.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().tvNoData.setVisibility(View.GONE);
            getViewDataBinding().recyclerView.setVisibility(View.VISIBLE);
        }

        if (listOf == 2) listener.totalCount(serviceProviderList.size());
    }

    @Override
    public void hideSwipeToRefresh() {
        getViewDataBinding().swipeToRefresh.setRefreshing(false);
        switch (listOf) {
            case 0:
                setGuestAdapterLoading(false);
                break;

            case 1:
                setHKAdapterLoading(false);
                break;

            case 2:
                setSPAdapterLoading(false);
                break;
        }
    }

    private void setGuestAdapterLoading(boolean isShowLoader) {
        if (mViewModel.getDataManager().isCommercial()) {
            if (commercialVisitorAdapter != null) {
                commercialVisitorAdapter.showLoading(isShowLoader);
                commercialVisitorAdapter.notifyDataSetChanged();
            }
        } else {
            if (guestAdapter != null) {
                guestAdapter.showLoading(isShowLoader);
                guestAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setHKAdapterLoading(boolean isShowLoader) {
        if (mViewModel.getDataManager().isCommercial()) {
            if (commercialStaffCheckInAdapter != null) {
                commercialStaffCheckInAdapter.showLoading(isShowLoader);
                commercialStaffCheckInAdapter.notifyDataSetChanged();
            }
        } else {
            if (houseKeepingAdapter != null) {
                houseKeepingAdapter.showLoading(isShowLoader);
                houseKeepingAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setSPAdapterLoading(boolean isShowLoader) {
        if (serviceProviderAdapter != null) {
            serviceProviderAdapter.showLoading(isShowLoader);
            serviceProviderAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshList() {
        doSearch(search);
    }

    private void updateUI() {
        getViewDataBinding().swipeToRefresh.setRefreshing(true);
        doSearch(search);
    }

    public interface OnFragmentInteraction {
        void totalCount(int size);
    }


    void scanIdDialog(String identity, int type){
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
                if (identity.equalsIgnoreCase(id)){
                    //staff
                    if(type==3){
                        mViewModel.staffCheckOut();
                    }else{
                        mViewModel.checkOut(type);
                    }
                }
                else {
                    showToast(R.string.alert_id);
                }
            }
        }).show(getFragmentManager());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Guests tmpBean = mViewModel.getDataManager().getGuestDetail();
        if (resultCode == RESULT_OK) {
            if (requestCode == SCAN_RESULT /*&& data != null*/) {
                String identityNo = MainResultStore.instance.getScannedIDData().idNumber;
                String fullName = MainResultStore.instance.getScannedIDData().name;
                if (identity.equalsIgnoreCase(identityNo)) {
                    if (name.equalsIgnoreCase(fullName)) {
                        if(type==3){
                            mViewModel.staffCheckOut();
                        }else{
                            mViewModel.checkOut(type);
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

    public void printLabel(boolean sp) {
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

            if(propertyInfo!=null && !propertyInfo.getImage().isEmpty() && getViewModel().getPropertyImage().getValue()!=null)
                bixolonLabelPrinter.drawImage(AppUtils.getBitmap(Objects.requireNonNull(getViewModel().getPropertyImage().getValue())),450,40,120,1,1,0);

            bixolonLabelPrinter.drawBlock(10,30,800,33,79,3);
            bixolonLabelPrinter.drawText("Commercial Complex", 10, 50, BixolonLabelPrinter.FONT_SIZE_12,
                    mHorizontalMultiplier, mVerticalMultiplier, 0, 0, false, true, 70);
            bixolonLabelPrinter.drawBlock(10,90,800,93,79,3);

            if(sp){
                bixolonLabelPrinter.drawText(getString(R.string.data_name,serviceProvider.getName()), 10, 130, mFontSize,
                        mHorizontalMultiplier, mVerticalMultiplier, 0, 0, false, true, 70);
                bixolonLabelPrinter.drawText(getString(R.string.data_identity,serviceProvider.getIdentityNo()), 10, 170, mFontSize,
                        mHorizontalMultiplier, mVerticalMultiplier, 0, 0, false, true, 70);
                bixolonLabelPrinter.drawText("Type: Service Provider", 10, 210, mFontSize,
                        mHorizontalMultiplier, mVerticalMultiplier, 0, 0, false, true, 70);
                if(serviceProvider!=null && !serviceProvider.getImageUrl().isEmpty() && getViewModel().getVisitorImage().getValue()!=null)
                    bixolonLabelPrinter.drawImage(AppUtils.getBitmap(Objects.requireNonNull(getViewModel().getVisitorImage().getValue())),450,240,120,1,1,0);
            }
            //visitor details to be printed
            else{
                CommercialVisitorResponse.CommercialGuest commercialGuest = getViewModel().getDataManager().getCommercialVisitorDetail();
                bixolonLabelPrinter.drawText(getString(R.string.data_name,commercialGuest.getName()), 10, 130, mFontSize,
                        mHorizontalMultiplier, mVerticalMultiplier, 0, 0, false, true, 70);
                if(commercialGuest.getIdentityNo()!=null && !commercialGuest.getIdentityNo().isEmpty())
                bixolonLabelPrinter.drawText(getString(R.string.data_identity,commercialGuest.getIdentityNo()), 10, 170, mFontSize,
                        mHorizontalMultiplier, mVerticalMultiplier, 0, 0, false, true, 70);
                bixolonLabelPrinter.drawText("Type: Visitor", 10, 210, mFontSize,
                        mHorizontalMultiplier, mVerticalMultiplier, 0, 0, false, true, 70);
                if(commercialGuest!=null && !commercialGuest.getImageUrl().isEmpty() && getViewModel().getVisitorImage().getValue()!=null)
                    bixolonLabelPrinter.drawImage(AppUtils.getBitmap(Objects.requireNonNull(getViewModel().getVisitorImage().getValue())),450,240,120,1,1,0);
            }
            //QR code
            //bixolonLabelPrinter.drawQrCode(serviceProvider.getIdentityNo(), 400, 130, model, eccLevel, 7, rotation);
            bixolonLabelPrinter.print(1, 1);
            bixolonLabelPrinter.endTransactionPrint();
        }else{
            //bixolonLabelPrinter.findBluetoothPrinters();
            showAlert(R.string.alert,R.string.no_printer_connected);
        }
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
                    CheckInFragment.dispatchMessage(msg);
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
                        DialogManager.showBluetoothDialog(getActivity(), (Set<BluetoothDevice>) msg.obj,true);
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
               // Toast.makeText(getActivity(), buffer.toString(), Toast.LENGTH_SHORT).show();
                break;
            case BixolonLabelPrinter.PROCESS_GET_INFORMATION_MODEL_NAME:
            case BixolonLabelPrinter.PROCESS_GET_INFORMATION_FIRMWARE_VERSION:
            case BixolonLabelPrinter.PROCESS_EXECUTE_DIRECT_IO:
               // Toast.makeText(getActivity(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
