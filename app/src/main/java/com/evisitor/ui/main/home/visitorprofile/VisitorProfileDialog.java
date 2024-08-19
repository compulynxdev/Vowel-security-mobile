package com.evisitor.ui.main.home.visitorprofile;

import static com.evisitor.util.AppConstants.ADD_FAMILY_MEMBER;
import static com.evisitor.util.AppConstants.SCAN_RESULT;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.evisitor.EVisitor;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.DataManager;
import com.evisitor.data.model.CommercialStaffResponse;
import com.evisitor.data.model.CommercialVisitorResponse;
import com.evisitor.data.model.DeviceBean;
import com.evisitor.data.model.Guests;
import com.evisitor.data.model.HouseKeepingResponse;
import com.evisitor.data.model.ServiceProvider;
import com.evisitor.data.model.VisitorProfileBean;
import com.evisitor.databinding.DialogVisitorProfileBinding;
import com.evisitor.ui.base.BaseDialog;
import com.evisitor.ui.dialog.ImagePickBottomSheetDialog;
import com.evisitor.ui.dialog.ImagePickCallback;
import com.evisitor.ui.main.commercial.gadgets.GadgetsInputActivity;
import com.evisitor.ui.main.commercial.secondryguest.SecondaryGuestInputActivity;
import com.evisitor.util.PermissionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class VisitorProfileDialog extends BaseDialog<DialogVisitorProfileBinding, VisitorProfileViewModel> implements View.OnClickListener {
    private List<DeviceBean> deviceBeanList;
    private VisitorProfileCallback callback;
    private List<VisitorProfileBean> visitorInfoList;
    private String btnLabel = "";
    private String image = "", documentImage = "", vehicalNoPlateImg = "";
    private boolean isBtnVisible = true;
    private boolean isCommercialGuest = false;
    private VisitorProfileAdapter infoAdapter;
    private boolean checkInIsApproved;

    public static VisitorProfileDialog newInstance(List<VisitorProfileBean> visitorInfoList, VisitorProfileCallback callback) {
        Bundle args = new Bundle();
        VisitorProfileDialog fragment = new VisitorProfileDialog();
        fragment.setArguments(args);
        fragment.setOnClick(callback);
        fragment.setData(visitorInfoList);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(getBaseActivity());
    }

    private void setData(List<VisitorProfileBean> visitorInfoList) {
        this.visitorInfoList = visitorInfoList;
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_visitor_profile;
    }

    public VisitorProfileDialog setBtnLabel(String btnLabel) {
        this.btnLabel = btnLabel;
        return this;
    }

    public VisitorProfileDialog setBtnVisible(boolean btnVisible) {
        isBtnVisible = btnVisible;
        return this;
    }

    public VisitorProfileDialog setImage(String image) {
        this.image = image;
        return this;
    }

    public VisitorProfileDialog setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
        return this;
    }

    public VisitorProfileDialog setVehicalNoPlateImg(String vehicalNoPlateImg) {
        this.vehicalNoPlateImg = vehicalNoPlateImg;
        return this;
    }

    public VisitorProfileDialog checkInIsApproved(boolean isApproved) {
        checkInIsApproved = isApproved;
        return this;
    }

    public VisitorProfileDialog setIsCommercialGuest(boolean isTrue) {
        this.isCommercialGuest = isTrue;
        return this;
    }

    @Override
    public VisitorProfileViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(VisitorProfileViewModel.class);
    }

    private void setOnClick(VisitorProfileCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().imgClose.setOnClickListener(this);
        getViewDataBinding().tvClickImage.setOnClickListener(this);
        getViewDataBinding().viewNoPlateImg.setOnClickListener(this);

        getViewDataBinding().btnOk.setVisibility(isBtnVisible ? View.VISIBLE : View.GONE);
        getViewDataBinding().btnOk.setOnClickListener(this);
        getViewDataBinding().imgProfile.setOnClickListener(this);
        getViewDataBinding().tvDocumentImage.setOnClickListener(this);
        getViewDataBinding().showNoPlatImage.setOnClickListener(this);
        if (!btnLabel.isEmpty()) {
            getViewDataBinding().btnOk.setText(btnLabel);
        }
        getViewModel().doFindBodyTemperature();
        DataManager dataManager = EVisitor.getInstance().getDataManager();

        if (image.isEmpty()) {
            Glide.with(this).load(R.drawable.ic_person).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(getViewDataBinding().imgProfile);
        } else {
            Glide.with(this).load(mViewModel.getDataManager().getImageBaseURL().concat(image)).centerCrop().placeholder(R.drawable.ic_person).diskCacheStrategy(DiskCacheStrategy.ALL).into(getViewDataBinding().imgProfile);
        }
        setUpAdapter();

        if (isCommercialGuest && mViewModel.getDataManager().isCommercial()) {
            CommercialVisitorResponse.CommercialGuest guest = mViewModel.getDataManager().getCommercialVisitorDetail();
            if (guest != null) {
                deviceBeanList = guest.getDeviceBeanList();
                if (deviceBeanList != null && !deviceBeanList.isEmpty()) {
                    getViewDataBinding().tvGadgetsInfo.setVisibility(View.VISIBLE);
                    getViewDataBinding().tvGadgetsInfo.setText(getString(R.string.view_gadgets_info).concat(" : ").concat(String.valueOf(deviceBeanList.size())));
                    getViewDataBinding().tvGadgetsInfo.setOnClickListener(this);
                } else if (btnLabel.equalsIgnoreCase(getString(R.string.check_in)) && checkInIsApproved) {
                    getViewDataBinding().tvGadgetsInfo.setVisibility(View.GONE);
                    getViewDataBinding().tvClickImage.setVisibility(View.GONE);
                } else if (btnLabel.equalsIgnoreCase(getString(R.string.check_in))) {
                    getViewDataBinding().tvGadgetsInfo.setVisibility(View.VISIBLE);
                    getViewDataBinding().tvGadgetsInfo.setText(getString(R.string.add_gadgets_info));
                    getViewDataBinding().tvGadgetsInfo.setOnClickListener(this);
                    getViewDataBinding().tvClickImage.setVisibility(View.VISIBLE);
                } else {
                    getViewDataBinding().tvGadgetsInfo.setVisibility(View.GONE);
                }
            } else {
                getViewDataBinding().tvGadgetsInfo.setVisibility(View.GONE);
            }
        } else {
            getViewDataBinding().tvGadgetsInfo.setVisibility(View.GONE);
            if (btnLabel.equalsIgnoreCase(getString(R.string.check_in))) {
                getViewDataBinding().tvClickImage.setVisibility(View.VISIBLE);
            }
        }

        if (mViewModel.getDataManager().getGuestConfiguration().isGuestGroupFeature()) {
            if (isCommercialGuest && mViewModel.getDataManager().isCommercial()) {
                CommercialVisitorResponse.CommercialGuest guests = mViewModel.getDataManager().getCommercialVisitorDetail();
                if (guests != null && !guests.getGuestList().isEmpty()) {
                    getViewDataBinding().tvSecondaryGuestInfo.setVisibility(View.VISIBLE);
                    getViewDataBinding().tvSecondaryGuestInfo.setText(getString(R.string.view_secondary_guest, String.valueOf(guests.getGuestList().size())));
                    getViewDataBinding().tvSecondaryGuestInfo.setOnClickListener(this);
                }
            } else {
                Guests guests = mViewModel.getDataManager().getGuestDetail();
                if (guests != null && !guests.getGuestList().isEmpty()) {
                    getViewDataBinding().tvSecondaryGuestInfo.setVisibility(View.VISIBLE);
                    getViewDataBinding().tvSecondaryGuestInfo.setText(getString(R.string.view_secondary_guest, String.valueOf(guests.getGuestList().size())));
                    getViewDataBinding().tvSecondaryGuestInfo.setOnClickListener(this);
                }
            }
        } else {
            getViewDataBinding().tvSecondaryGuestInfo.setVisibility(View.GONE);
        }

        if (!documentImage.isEmpty()) {
            getViewDataBinding().tvDocumentImage.setVisibility(View.VISIBLE);
        } else getViewDataBinding().tvDocumentImage.setVisibility(View.GONE);

        if (vehicalNoPlateImg != null && !vehicalNoPlateImg.isEmpty()) {
            getViewDataBinding().viewNoPlateImg.setVisibility(View.VISIBLE);
        } else getViewDataBinding().viewNoPlateImg.setVisibility(View.GONE);

        getViewModel().getNumberPlate().observe(this, s -> infoAdapter.setNumberPlate(s));


        updateTemperatureUI();

        if (mViewModel.getDataManager().capturesVehicleModel()) {
            updateVehicleModelUI();
        }

        getViewDataBinding().etTemperature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty() && s.toString().length() > 1) {
                    if (Double.parseDouble(s.toString()) >= 34 && Double.parseDouble(s.toString()) <= 40) {
                        if (dataManager.isCommercial()) {
                            CommercialVisitorResponse.CommercialGuest guests = dataManager.getCommercialVisitorDetail();
                            if (guests != null) {
                                guests.setBodyTemperature(getViewDataBinding().etTemperature.getText().toString());
                                dataManager.setCommercialVisitorDetail(guests);
                            }

                            CommercialStaffResponse.ContentBean staff = dataManager.getCommercialStaff();
                            if (staff != null) {
                                staff.setBodyTemperature(getViewDataBinding().etTemperature.getText().toString());
                                dataManager.setCommercialStaff(staff);
                            }

                        } else {
                            Guests guests = dataManager.getGuestDetail();
                            if (guests != null) {
                                guests.setBodyTemperature(getViewDataBinding().etTemperature.getText().toString());
                                dataManager.setGuestDetail(guests);
                            }

                            HouseKeepingResponse.ContentBean hkBean = dataManager.getHouseKeeping();
                            if (hkBean != null) {
                                hkBean.setBodyTemperature(getViewDataBinding().etTemperature.getText().toString());
                                dataManager.setHouseKeeping(hkBean);
                            }
                        }

                        ServiceProvider spBean = dataManager.getSpDetail();
                        if (spBean != null) {
                            spBean.setBodyTemperature(getViewDataBinding().etTemperature.getText().toString());
                            dataManager.setSPDetail(spBean);
                        }
                    } else {
                        getViewDataBinding().etTemperature.setText("");
                        showToast(R.string.temperature_should_be_30);
                    }

                }
            }
        });

    }

    private void updateVehicleModelUI() {
        if (checkInIsApproved || btnLabel.equalsIgnoreCase(getString(R.string.check_out))) {
        } else {
            getViewDataBinding().tvVehicleModel.setVisibility(View.VISIBLE);
            getViewDataBinding().etVehicleModel.setVisibility(View.VISIBLE);

            getViewDataBinding().etVehicleModel.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (mViewModel.getDataManager().isCommercial()) {
                        CommercialVisitorResponse.CommercialGuest guests = mViewModel.getDataManager().getCommercialVisitorDetail();
                        if (guests != null) {
                            guests.setEnteredVehicleModel(editable.toString());
                            mViewModel.getDataManager().setCommercialVisitorDetail(guests);
                        }
                    } else {
                        Guests guests = mViewModel.getDataManager().getGuestDetail();
                        if (guests != null) {
                            guests.setEnteredVehicleModel(editable.toString());
                            mViewModel.getDataManager().setGuestDetail(guests);
                        }
                    }
                    ServiceProvider spBean = mViewModel.getDataManager().getSpDetail();
                    if (spBean != null) {
                        spBean.setEnteredVehicleModel(editable.toString());
                        mViewModel.getDataManager().setSPDetail(spBean);
                    }
                }
            });
        }
    }

    private void updateTemperatureUI() {
        getViewDataBinding().tvTitle.setText(getString(R.string.body_temp, ""));

        getViewModel().getBodyTemperature().observe(this, s -> {
            if (checkInIsApproved || btnLabel.equalsIgnoreCase(getString(R.string.check_out))) {
                if (s.equals("")) {
                    getViewDataBinding().tvTitle.setVisibility(View.GONE);
                    getViewDataBinding().etTemperature.setVisibility(View.GONE);
                } else {
                    getViewDataBinding().etTemperature.setText(s);
                    getViewDataBinding().etTemperature.setEnabled(false);
                    getViewDataBinding().etTemperature.setCursorVisible(false);
                }
            } else {
                getViewDataBinding().etTemperature.setText(s);
                getViewDataBinding().tvTitle.setText(getString(R.string.body_temp, ""));
            }
        });
    }

    private void setUpAdapter() {
        infoAdapter = new VisitorProfileAdapter(getBaseActivity(), visitorInfoList);
        getViewDataBinding().recyclerView.setAdapter(infoAdapter);
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        int id = v.getId();

        if (id == R.id.btn_ok) {
            if (callback != null) {
                callback.onOkayClick(this);
            } else {
                dismiss();
            }
        } else if (id == R.id.img_close) {
            dismiss();
        } else if (id == R.id.img_profile) {
            showFullImage(image);
        } else if (id == R.id.tv_secondary_guest_info) {
            Guests tmpBean = mViewModel.getDataManager().getGuestDetail();
            Intent intent = SecondaryGuestInputActivity.getStartIntent(getBaseActivity());
            if (tmpBean != null) {
                if (!tmpBean.getGuestList().isEmpty()) {
                    intent.putExtra("list", new Gson().toJson(tmpBean.guestList));
                }
            } else {
                CommercialVisitorResponse.CommercialGuest guest = mViewModel.getDataManager().getCommercialVisitorDetail();
                if (guest != null && !guest.getGuestList().isEmpty()) {
                    intent.putExtra("list", new Gson().toJson(guest.guestList));
                }
            }
            startActivityForResult(intent, ADD_FAMILY_MEMBER);
        } else if (id == R.id.show_no_plat_image) {
            Bitmap bitmap = null;
            DataManager dataManager = mViewModel.getDataManager();
            if (dataManager.isCommercial()) {
                CommercialVisitorResponse.CommercialGuest guests = dataManager.getCommercialVisitorDetail();
                if (guests != null) {
                    bitmap = guests.getNo_plate_bmp_img();
                }
                CommercialStaffResponse.ContentBean staff = dataManager.getCommercialStaff();
                if (staff != null) {
                    bitmap = staff.getBitmapVehicleImage();
                }
            } else {
                Guests guests = dataManager.getGuestDetail();
                if (guests != null) {
                    bitmap = guests.getNo_plate_bmp_img();
                }
                HouseKeepingResponse.ContentBean hkBean = dataManager.getHouseKeeping();
                if (hkBean != null) {
                    bitmap = hkBean.getVehicalBitmapImg();
                }
            }

            ServiceProvider spBean = dataManager.getSpDetail();
            if (spBean != null) {
                bitmap = spBean.getVehicleBitMapImage();
            }
            showBitmapImage(bitmap);
        } else if (id == R.id.tv_click_image) {
            if (PermissionUtils.RequestMultiplePermissionCamera(getBaseActivity())) {
                ImagePickBottomSheetDialog.newInstance(new ImagePickCallback() {
                    @Override
                    public void onImageReceived(Bitmap bitmap) {
                        if (bitmap != null) {
                            getViewDataBinding().showNoPlatImage.setVisibility(View.VISIBLE);
                            getViewModel().numberPlateVerification(bitmap);
                        } else {
                            getViewDataBinding().showNoPlatImage.setVisibility(View.GONE);
                        }
                        DataManager dataManager = mViewModel.getDataManager();
                        if (dataManager.isCommercial()) {
                            CommercialVisitorResponse.CommercialGuest guests = dataManager.getCommercialVisitorDetail();
                            if (guests != null) {
                                guests.setNo_plate_bmp_img(bitmap);
                                dataManager.setCommercialVisitorDetail(guests);
                            }

                            CommercialStaffResponse.ContentBean staff = dataManager.getCommercialStaff();
                            if (staff != null) {
                                staff.setBitmapVehicleImage(bitmap);
                                dataManager.setCommercialStaff(staff);
                            }

                        } else {
                            Guests guests = dataManager.getGuestDetail();
                            if (guests != null) {
                                guests.setNo_plate_bmp_img(bitmap);
                                dataManager.setGuestDetail(guests);
                            }

                            HouseKeepingResponse.ContentBean hkBean = dataManager.getHouseKeeping();
                            if (hkBean != null) {
                                hkBean.setVehicalBitmapImg(bitmap);
                                dataManager.setHouseKeeping(hkBean);
                            }
                        }

                        ServiceProvider spBean = dataManager.getSpDetail();
                        if (spBean != null) {
                            spBean.setVehicleBitMapImage(bitmap);
                            dataManager.setSPDetail(spBean);
                        }
                    }

                    @Override
                    public void onView() {

                    }
                }, "").show(getBaseActivity().getSupportFragmentManager());
            }
        } else if (id == R.id.tv_document_image) {
            showFullImage(documentImage);
        } else if (id == R.id.tv_gadgets_info) {
            Intent i = GadgetsInputActivity.getStartIntent(getContext());
            if (!deviceBeanList.isEmpty()) {
                i.putExtra("list", new Gson().toJson(deviceBeanList));
            }
            i.putExtra("add", btnLabel.equalsIgnoreCase(getString(R.string.check_in)));
            startActivityForResult(i, SCAN_RESULT);
        } else if (id == R.id.view_no_plate_img) {
            if (!vehicalNoPlateImg.isEmpty()) {
                showFullImage(vehicalNoPlateImg);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_RESULT && data != null) {
            Type listType = new TypeToken<List<DeviceBean>>() {
            }.getType();
            deviceBeanList.clear();
            deviceBeanList.addAll(Objects.requireNonNull(mViewModel.getDataManager().getGson().fromJson(data.getStringExtra("data"), listType)));
            CommercialVisitorResponse.CommercialGuest guest = mViewModel.getDataManager().getCommercialVisitorDetail();
            guest.setDeviceBeanList(deviceBeanList);
            mViewModel.getDataManager().setCommercialVisitorDetail(guest);
            getViewDataBinding().tvGadgetsInfo.setText(getString(R.string.view_gadgets_info).concat(" : ").concat(String.valueOf(deviceBeanList.size())));
        }
    }
}
