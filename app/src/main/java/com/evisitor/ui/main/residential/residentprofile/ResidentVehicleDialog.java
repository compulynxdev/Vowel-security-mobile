package com.evisitor.ui.main.residential.residentprofile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.ResidentProfile;
import com.evisitor.databinding.DialogResidentVehicleBinding;
import com.evisitor.ui.base.BaseDialog;
import com.evisitor.ui.dialog.ImagePickBottomSheetDialog;
import com.evisitor.ui.dialog.ImagePickCallback;
import com.evisitor.ui.dialog.selection.SelectionBottomSheetDialog;
import com.evisitor.util.PermissionUtils;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

public class ResidentVehicleDialog extends BaseDialog<DialogResidentVehicleBinding,ResidentVehicleViewModel> implements View.OnClickListener{

    static ResidentProfile profile;
    String vehicleNo = "";
    static ResidentCallBack callBack;

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    public static ResidentVehicleDialog newInstance(ResidentProfile tmpProfile,ResidentCallBack residentCallBack ) {
        Bundle args = new Bundle();
        ResidentVehicleDialog fragment = new ResidentVehicleDialog();
        fragment.setArguments(args);
        profile=tmpProfile;
        callBack = residentCallBack;
        return fragment;
    }

    interface  ResidentCallBack{
        void doCheckInOut(boolean isCheckIn, ResidentProfile profile, String vehicleNo);
    }
    @Override
    public int getLayoutId() {
        return R.layout.dialog_resident_vehicle;
    }

    @Override
    public ResidentVehicleViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(ResidentVehicleViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().tvSelectVehicle.setOnClickListener(this);
        getViewDataBinding().btnIn.setOnClickListener(this);
        getViewDataBinding().btnOut.setOnClickListener(this);
        getViewModel().setNavigator(getBaseActivity());
        if(!profile.getVehicleList().isEmpty()){
            getViewDataBinding().tvSelectVehicle.setVisibility(View.VISIBLE);
            getViewDataBinding().tvOr.setVisibility(View.VISIBLE);
        }
        getViewDataBinding().etVehicle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    vehicleNo = s.toString();
                }
            }
        });

        getViewDataBinding().imgVehicle.setOnClickListener(this);
        getViewModel().getNumberPlate().observe(this, s -> {
            if(!s.isEmpty()){
                getViewDataBinding().etVehicle.setText(s.toUpperCase());
                vehicleNo = s;
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_in) {
            dismiss();
            if (callBack != null) {
                callBack.doCheckInOut(true, profile, vehicleNo);
            }
        } else if (id == R.id.img_vehicle) {
            if (PermissionUtils.RequestMultiplePermissionCamera(getActivity())) {
                ImagePickBottomSheetDialog.newInstance(new ImagePickCallback() {
                    @Override
                    public void onImageReceived(Bitmap bitmap) {
                        if (bitmap != null) {
                            getViewModel().numberPlateVerification(bitmap);
                        }
                    }

                    @Override
                    public void onView() {

                    }
                }, "").show(getFragmentManager());
            }
        } else if (id == R.id.btn_out) {
            dismiss();
            if (callBack != null) {
                callBack.doCheckInOut(false, profile, vehicleNo);
            }
        } else if (id == R.id.tv_select_vehicle) {
            SelectionBottomSheetDialog.newInstance(getString(R.string.select_vehicle), profile.getVehicleList()).setItemSelectedListener(pos -> {
                vehicleNo = profile.getVehicleList().get(pos);
                getViewDataBinding().tvSelectVehicle.setText(vehicleNo.toUpperCase());
                getViewDataBinding().etVehicle.setText(vehicleNo.toUpperCase());
            }).show(getFragmentManager());
        }
    }
}
