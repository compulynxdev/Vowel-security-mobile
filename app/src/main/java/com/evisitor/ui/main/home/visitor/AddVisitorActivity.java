package com.evisitor.ui.main.home.visitor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.AddVisitorData;
import com.evisitor.data.model.GuestConfigurationResponse;
import com.evisitor.data.model.HostDetailBean;
import com.evisitor.data.model.HouseDetailBean;
import com.evisitor.data.model.IdentityBean;
import com.evisitor.databinding.ActivityAddVisitorBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.dialog.ImagePickBottomSheetDialog;
import com.evisitor.ui.dialog.ImagePickCallback;
import com.evisitor.ui.dialog.country.CountrySelectionDialog;
import com.evisitor.ui.main.MainActivity;
import com.evisitor.ui.main.home.visitor.dialogs.SelectionBottomSheetDialog;
import com.evisitor.ui.main.rejectreason.InputDialog;
import com.evisitor.util.AppConstants;
import com.evisitor.util.PermissionUtils;
import com.sharma.mrzreader.MrzRecord;

import java.util.List;

public class AddVisitorActivity extends BaseActivity<ActivityAddVisitorBinding, AddVisitorViewModel> implements AddVisitorNavigator, View.OnClickListener {

    private String countryCode = "254";
    private String houseId = "";  //also called flat id
    private String residentId = "";  //also called host id
    // private String ownerId = "";  //also called house owner id
    private Bitmap bmp_profile;
    private List<HostDetailBean> hostDetailList;
    private String idType = "";
    private Boolean isGuest = null;
    private GuestConfigurationResponse configurationResponse;

    public static Intent getStartIntent(Context context){
        return new Intent(context, AddVisitorActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_visitor;
    }

    @Override
    public AddVisitorViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(AddVisitorViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        getViewDataBinding().toolbar.tvTitle.setText(R.string.title_add_visitor);

        setUp();
        setIntentData(getIntent());
        guestConfigurationObserver();
        setUpHouseNoSearch();
        randomCheckInObserver();
    }

    private void guestConfigurationObserver() {
        getViewModel().doGetGuestConfigurationObserver().observe(this, guestConfiguration -> {
            configurationResponse = guestConfiguration;
            updateFieldConfigurationUI();
        });
    }

    private void updateFieldConfigurationUI() {
        if (configurationResponse == null) configurationResponse = new GuestConfigurationResponse();

        if (isGuest == null || isGuest) {
            getViewDataBinding().llNumber.setVisibility(configurationResponse.getGuestField().isContactNo() ? View.VISIBLE : View.GONE);
            getViewDataBinding().etAddress.setVisibility(configurationResponse.getGuestField().isAddress() ? View.VISIBLE : View.GONE);
            getViewDataBinding().tvGender.setVisibility(configurationResponse.getGuestField().isGender() ? View.VISIBLE : View.GONE);
        } else {
            getViewDataBinding().llNumber.setVisibility(View.VISIBLE);
            getViewDataBinding().etAddress.setVisibility(View.VISIBLE);
            getViewDataBinding().tvGender.setVisibility(View.VISIBLE);
        }
    }

    private void setIntentData(Intent intent) {
        if (intent.hasExtra(AppConstants.FROM)) {
            String from = intent.getStringExtra(AppConstants.FROM);

            if (from == null) from = "";

            switch (from) {
                case AppConstants.CONTROLLER_GUEST:
                    getViewDataBinding().toolbar.tvTitle.setText(R.string.title_add_guest);
                    getViewDataBinding().tvVisitorType.setVisibility(View.GONE);
                    getViewDataBinding().tvAssignedTo.setVisibility(View.GONE);
                    updateVisitorUI(getViewModel().getVisitorTypeList().get(0).toString());
                    getViewModel().doGetGuestConfiguration();
                    break;

                case AppConstants.CONTROLLER_SP:
                    getViewDataBinding().toolbar.tvTitle.setText(R.string.title_add_sp);
                    getViewDataBinding().tvVisitorType.setVisibility(View.GONE);
                    getViewDataBinding().tvAssignedTo.setVisibility(View.VISIBLE);
                    updateVisitorUI(getViewModel().getVisitorTypeList().get(1).toString());
                    break;

                default:
                    getViewModel().doGetGuestConfiguration();
                    break;
            }
        }

        if (intent.hasExtra("Record")) {
            MrzRecord mrzRecord = (MrzRecord) intent.getSerializableExtra("Record");
            assert mrzRecord != null;

            String code = mrzRecord.getCode1() + "" + mrzRecord.getCode2();
            switch (code.toLowerCase()) {
                case "p<":
                case "p":
                    IdentityBean bean = (IdentityBean) getViewModel().getIdentityTypeList().get(1);
                    getViewDataBinding().tvIdentity.setText(bean.getTitle());
                    idType = bean.getKey();
                    getViewDataBinding().etIdentity.setText(mrzRecord.getDocumentNumber());
                    break;

                case "ac":
                case "id":
                    bean = (IdentityBean) getViewModel().getIdentityTypeList().get(0);
                    getViewDataBinding().tvIdentity.setText(bean.getTitle());
                    idType = bean.getKey();

                    getViewDataBinding().etIdentity.setText(mrzRecord.getOptional2().length() == 9 ?
                            mrzRecord.getOptional2().substring(0, mrzRecord.getOptional2().length() - 1) :
                            mrzRecord.getOptional2());
                    break;
            }

            //getViewDataBinding().tvIdentity.setText(mrzRecord.getCode().toString().concat(" [").concat(mrzRecord.getCode1() + "").concat(mrzRecord.getCode2() + "]"));
            getViewDataBinding().etName.setText(mrzRecord.getGivenNames().concat(" ").concat(mrzRecord.getSurname()));
            //tv_dob.setText(CalenderUtils.formatDate(mrzRecord.getDateOfBirth().toString(), "dd/MM/yy", "dd/MM/yyyy"));

            if (mrzRecord.getSex() != null) {
                if (mrzRecord.getSex().toString().equalsIgnoreCase("M") || mrzRecord.getSex().toString().equalsIgnoreCase("Male"))
                    getViewDataBinding().tvGender.setText(R.string.male);
                else if (mrzRecord.getSex().toString().equalsIgnoreCase("F") || mrzRecord.getSex().toString().equalsIgnoreCase("Female"))
                    getViewDataBinding().tvGender.setText(R.string.female);
            }
        }
    }

    private void setUpHouseNoSearch() {
        getViewDataBinding().actvHouseNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getViewModel().doGetHouseDetails(s.toString());
            }
        });

        getViewModel().doGetHouseDetails().observe(AddVisitorActivity.this, houseDetailList -> {
            ArrayAdapter<HouseDetailBean> arrayAdapter = new ArrayAdapter<>(AddVisitorActivity.this, android.R.layout.simple_list_item_1, houseDetailList);
            getViewDataBinding().actvHouseNo.setThreshold(1);
            getViewDataBinding().actvHouseNo.setAdapter(arrayAdapter);

            getViewDataBinding().actvHouseNo.setOnItemClickListener((adapterView, view, i, l) -> {
                HouseDetailBean houseDetailBean = (HouseDetailBean) adapterView.getItemAtPosition(i);
                houseId = String.valueOf(houseDetailBean.getId());
                getViewDataBinding().actvHouseNo.setText(houseDetailBean.getName());

                /*Reset Data once House Info Change*/
                //ownerId = "";
                //getViewDataBinding().tvOwner.setText("");
                residentId = "";
                getViewDataBinding().tvHost.setText("");

                getViewModel().doGetHostDetails(houseId);
                /*End Here*/
            });
        });

        getViewModel().doGetHostDetails().observe(this, hostDetailList -> {
            this.hostDetailList = hostDetailList;
            getViewDataBinding().tvHost.setVisibility(View.VISIBLE);
            //setUpOwner(false);
        });
    }

    /*private void setUpOwner(boolean isDialogShow) {
        if (hostDetailList == null) return;

        List<HostDetailBean> ownerList = new ArrayList<>();
        for (HostDetailBean tmp : hostDetailList) {
            if (tmp.isIsOwner()) ownerList.add(tmp);
        }

        if (ownerList.size() == 1 && ownerList.get(0).getId() != -1) {
            HostDetailBean bean = ownerList.get(0);
            ownerId = String.valueOf(bean.getId());
            getViewDataBinding().tvOwner.setText(bean.getFullName());
        } else {
            if (isDialogShow) {
                if (ownerList.isEmpty()) {
                    //if flat not assigned to owner
                    ownerList.add(new HostDetailBean(-1, getString(R.string.no_owner_found), true));
                }
                HostPickerBottomSheetDialog.newInstance(ownerList, bean -> {
                    if (bean.getId() != -1) {
                        ownerId = String.valueOf(bean.getId());
                        getViewDataBinding().tvOwner.setText(bean.getFullName());
                    }
                }).show(getSupportFragmentManager());
            }
        }
    }*/

    private void setUp() {
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        setOnClickListener(imgBack, getViewDataBinding().tvVisitorType, getViewDataBinding().tvAssignedTo, getViewDataBinding().tvEmployment, getViewDataBinding().tvIdentity, getViewDataBinding().tvGender, getViewDataBinding().tvOwner, getViewDataBinding().tvHost
                , getViewDataBinding().frameImg, getViewDataBinding().btnAdd, getViewDataBinding().rlCode);
        getViewDataBinding().tvCode.setText("+".concat(countryCode));
        if (getViewModel().getDataManager().isIdentifyFeature())
            getViewDataBinding().etIdentity.setVisibility(View.VISIBLE);
        getViewDataBinding().etIdentity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty() || s.toString().trim().length() > 1)
                    getViewDataBinding().tvIdentity.setVisibility(s.toString().trim().isEmpty() ? View.GONE : View.VISIBLE);
            }
        });
    }

    private void setOnClickListener(View... views) {
        for (View view : views)
            view.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.frame_img:
                if (PermissionUtils.RequestMultiplePermissionCamera(this)) {
                    ImagePickBottomSheetDialog.newInstance(new ImagePickCallback() {
                        @Override
                        public void onImageReceived(Bitmap bitmap) {
                            bmp_profile = bitmap;
                            getViewDataBinding().imgUser.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onView() {

                        }
                    }, "").show(getSupportFragmentManager());
                }
                break;

            case R.id.tv_visitor_type:
                SelectionBottomSheetDialog.newInstance(getString(R.string.select_visitor_type), getViewModel().getVisitorTypeList()).setItemSelectedListener(pos -> {
                    String value = getViewModel().getVisitorTypeList().get(pos).toString();
                    updateVisitorUI(value);
                }).show(getSupportFragmentManager());
                break;

            case R.id.tv_assigned_to:
                SelectionBottomSheetDialog.newInstance(getString(R.string.select_assigned_to), getViewModel().getAssignedToList()).setItemSelectedListener(pos -> {
                    String value = getViewModel().getAssignedToList().get(pos).toString();
                    getViewDataBinding().tvAssignedTo.setText(value);
                    if (value.equals("Property")) {
                        getViewDataBinding().groupResident.setVisibility(View.GONE);
                    } else {
                        getViewDataBinding().groupResident.setVisibility(View.VISIBLE);
                    }
                }).show(getSupportFragmentManager());
                break;

            case R.id.tv_identity:
                SelectionBottomSheetDialog.newInstance(getString(R.string.select_identity_type), getViewModel().getIdentityTypeList()).setItemSelectedListener(pos -> {
                    IdentityBean bean = (IdentityBean) getViewModel().getIdentityTypeList().get(pos);
                    getViewDataBinding().tvIdentity.setText(bean.getTitle());
                    idType = bean.getKey();
                }).show(getSupportFragmentManager());
                break;

            case R.id.tv_gender:
                SelectionBottomSheetDialog.newInstance(getString(R.string.select_gender), getViewModel().getGenderList()).setItemSelectedListener(pos -> getViewDataBinding().tvGender.setText(getViewModel().getGenderList().get(pos))).show(getSupportFragmentManager());
                break;

            case R.id.tv_owner:
                //setUpOwner(true);
                break;

            case R.id.tv_host:
                if (hostDetailList != null) {
                    if (hostDetailList.isEmpty()) {
                        hostDetailList.add(new HostDetailBean(-1, getString(R.string.no_host_found)));
                    }
                    SelectionBottomSheetDialog.newInstance(getString(R.string.select_host), hostDetailList).setItemSelectedListener(pos -> {
                        HostDetailBean bean = hostDetailList.get(pos);
                        if (bean.getId() != -1) {
                            residentId = String.valueOf(bean.getId());
                            getViewDataBinding().tvHost.setText(bean.getFullName());
                        }
                    }).show(getSupportFragmentManager());
                }
                break;

            case R.id.rl_code:
                CountrySelectionDialog.newInstance(code -> {
                    countryCode = code;
                    getViewDataBinding().tvCode.setText("+".concat(countryCode));
                }).show(getSupportFragmentManager());
                break;

            case R.id.tv_employment:
                SelectionBottomSheetDialog.newInstance(getString(R.string.select_employment), getViewModel().getEmploymentTypeList()).setItemSelectedListener(pos -> {
                    String value = getViewModel().getEmploymentTypeList().get(pos).toString();
                    getViewDataBinding().tvEmployment.setText(value);
                    if (value.equals("Self")) {
                        getViewDataBinding().groupEmployment.setVisibility(View.GONE);
                    } else {
                        getViewDataBinding().groupEmployment.setVisibility(View.VISIBLE);
                    }
                }).show(getSupportFragmentManager());
                break;

            case R.id.btn_add:
                if (isGuest == null) {
                    showToast(R.string.alert_select_visitor);
                    return;
                }

                AddVisitorData visitorData = new AddVisitorData();
                visitorData.isGuest = isGuest;
                visitorData.identityNo = getViewDataBinding().etIdentity.getText().toString().trim();
                visitorData.idType = idType;
                visitorData.name = getViewDataBinding().etName.getText().toString().trim();
                visitorData.contact = getViewDataBinding().etContact.getText().toString().trim();
                visitorData.address = getViewDataBinding().etAddress.getText().toString().trim();
                visitorData.gender = getViewDataBinding().tvGender.getText().toString();
                visitorData.houseId = houseId;
                visitorData.residentId = residentId;
                if (isGuest) {
                    if (configurationResponse == null)
                        configurationResponse = new GuestConfigurationResponse();
                    if (getViewModel().doVerifyGuestInputs(visitorData, configurationResponse)) {
                        getViewModel().doCheckGuestStatus(getViewDataBinding().etIdentity.getText().toString().trim(), idType);
                    }
                } else {
                    visitorData.assignedTo = getViewDataBinding().tvAssignedTo.getText().toString();
                    visitorData.isResident = !visitorData.assignedTo.equalsIgnoreCase("Property");
                    visitorData.visitorType = getViewDataBinding().tvVisitorType.getText().toString();
                    visitorData.employment = getViewDataBinding().tvEmployment.getText().toString();
                    visitorData.isCompany = !visitorData.employment.equalsIgnoreCase("Self");
                    visitorData.profile = getViewDataBinding().etWorkProfile.getText().toString().trim();
                    visitorData.companyName = getViewDataBinding().etCompanyName.getText().toString().trim();
                    visitorData.companyAddress = getViewDataBinding().etCompanyAddress.getText().toString().trim();


                    if (getViewModel().doVerifySPInputs(visitorData)) {
                        if (visitorData.isResident) {
                            AlertDialog.newInstance()
                                    .setNegativeBtnShow(true)
                                    .setCloseBtnShow(false)
                                    .setTitle(getString(R.string.check_in))
                                    .setMsg(getString(R.string.msg_check_in_call))
                                    .setPositiveBtnLabel(getString(R.string.approve))
                                    .setNegativeBtnLabel(getString(R.string.reject))
                                    .setOnNegativeClickListener(dialog1 -> {
                                        dialog1.dismiss();
                                        showReasonSPDialog(visitorData);
                                    })
                                    .setOnPositiveClickListener(dialog12 -> {
                                        dialog12.dismiss();
                                        doAddSp(true, visitorData);
                                    }).show(getSupportFragmentManager());
                        } else {
                            //for property there is no reject option
                            doAddSp(true, visitorData);
                        }
                    }
                }
                break;
        }
    }

    private void showReasonSPDialog(AddVisitorData visitorData) {
        InputDialog.newInstance().setTitle(getString(R.string.are_you_sure))
                .setOnPositiveClickListener((dialog, input) -> {
                    dialog.dismiss();
                    visitorData.rejectedReason = input;
                    doAddSp(false,visitorData);
                }).show(getSupportFragmentManager());
    }


    private void doAddSp(boolean isAccept, AddVisitorData visitorData) {
        visitorData.bmp_profile = bmp_profile;
        visitorData.vehicleNo = getViewDataBinding().etVehicle.getText().toString().trim();
        visitorData.dialingCode = countryCode;
        visitorData.isAccept = isAccept;
        getViewModel().doAddSp(visitorData);
    }

    private void updateVisitorUI(String visitorType) {
        getViewDataBinding().tvVisitorType.setText(visitorType);
        if (visitorType.equals("Guest")) {
            isGuest = true;
            getViewDataBinding().groupResident.setVisibility(View.VISIBLE);
            getViewDataBinding().groupSp.setVisibility(View.GONE);
        } else {
            isGuest = false;
            getViewDataBinding().groupResident.setVisibility(View.GONE);
            getViewDataBinding().groupSp.setVisibility(View.VISIBLE);
        }
        updateFieldConfigurationUI();
    }

    private void randomCheckInObserver() {
        getViewModel().getGuestStatusMutableData().observe(this, isBlock -> {
            if (isBlock) {
                showAlert(R.string.alert, R.string.msg_block);
            } else {
                AlertDialog.newInstance()
                        .setNegativeBtnShow(true)
                        .setCloseBtnShow(false)
                        .setTitle(getString(R.string.check_in))
                        .setMsg(getString(R.string.msg_check_in_call))
                        .setPositiveBtnLabel(getString(R.string.approve))
                        .setNegativeBtnLabel(getString(R.string.reject))
                        .setOnNegativeClickListener(dialog1 -> {
                            dialog1.dismiss();
                            showReasonGuestDialog();
                        })
                        .setOnPositiveClickListener(dialog12 -> {
                            dialog12.dismiss();
                            doAddGuest(true,null);
                        }).show(getSupportFragmentManager());
            }
        });
    }


    private void showReasonGuestDialog() {
        InputDialog.newInstance().setTitle(getString(R.string.are_you_sure))
                .setOnPositiveClickListener((dialog, input) -> {
                    dialog.dismiss();
                    doAddGuest(false,input);
                }).show(getSupportFragmentManager());
    }

    private void doAddGuest(boolean isAccept, String input) {
        AddVisitorData addVisitorData = new AddVisitorData();
        addVisitorData.isAccept = isAccept;
        addVisitorData.bmp_profile = bmp_profile;
        addVisitorData.identityNo = getViewDataBinding().etIdentity.getText().toString().trim();
        addVisitorData.idType = idType;
        addVisitorData.name = getViewDataBinding().etName.getText().toString();
        addVisitorData.vehicleNo = getViewDataBinding().etVehicle.getText().toString().trim();
        addVisitorData.contact = getViewDataBinding().etContact.getText().toString();
        addVisitorData.dialingCode = countryCode;
        addVisitorData.address = getViewDataBinding().etAddress.getText().toString();
        addVisitorData.gender = (configurationResponse == null || configurationResponse.getGuestField().isGender()) ? getViewDataBinding().tvGender.getText().toString() : "";
        addVisitorData.houseId = houseId;
        addVisitorData.residentId = residentId;

        if(!isAccept){
            addVisitorData.rejectedReason = input;
        }
        getViewModel().doAddGuest(addVisitorData);
    }

    @Override
    public void onSuccess(boolean isAccept) {
        if (isAccept) {
            AlertDialog.newInstance()
                    .setCloseBtnShow(false)
                    .setNegativeBtnShow(false)
                    .setTitle(getString(R.string.success))
                    .setMsg(getString(R.string.check_in_success))
                    .setOnPositiveClickListener(dialog12 -> {
                        dialog12.dismiss();
                        Intent intent = MainActivity.newIntent(this);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }).show(getSupportFragmentManager());
        } else {
            AlertDialog.newInstance()
                    .setCloseBtnShow(false)
                    .setNegativeBtnShow(false)
                    .setTitle(getString(R.string.alert))
                    .setMsg(getString(R.string.check_in_rejected))
                    .setOnPositiveClickListener(dialog12 -> {
                        dialog12.dismiss();
                        Intent intent = MainActivity.newIntent(getContext());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }).show(getSupportFragmentManager());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntentData(intent);
    }
}
