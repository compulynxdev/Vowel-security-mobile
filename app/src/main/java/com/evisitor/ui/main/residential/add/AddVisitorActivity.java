package com.evisitor.ui.main.residential.add;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.AddVisitorData;
import com.evisitor.data.model.CompanyBean;
import com.evisitor.data.model.GuestConfigurationResponse;
import com.evisitor.data.model.HostDetailBean;
import com.evisitor.data.model.HouseDetailBean;
import com.evisitor.data.model.IdentityBean;
import com.evisitor.data.model.ProfileBean;
import com.evisitor.databinding.ActivityAddVisitorBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.base.BaseViewModel;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.dialog.ImagePickBottomSheetDialog;
import com.evisitor.ui.dialog.ImagePickCallback;
import com.evisitor.ui.dialog.country.CountrySelectionDialog;
import com.evisitor.ui.dialog.selection.SelectionBottomSheetDialog;
import com.evisitor.ui.main.MainActivity;
import com.evisitor.ui.main.home.rejectreason.InputDialog;
import com.evisitor.util.AppConstants;
import com.evisitor.util.PermissionUtils;
import com.sharma.mrzreader.MrzRecord;
import com.smartengines.Constant;
import com.smartengines.MainResultStore;
import com.smartengines.ScanSmartActivity;
import com.smartengines.ScannedIDData;

import java.util.ArrayList;
import java.util.List;

import static com.evisitor.util.AppConstants.SCAN_RESULT;

public class AddVisitorActivity extends BaseActivity<ActivityAddVisitorBinding, AddVisitorViewModel> implements AddVisitorNavigator, View.OnClickListener, BaseViewModel.GuestConfigurationCallback {

    private String countryCode = "254";
    private String houseId = "";  //also called flat id
    private String residentId = "";  //also called host id
    // private String ownerId = "";  //also called house owner id
    private Bitmap bmp_profile;
    private List<HostDetailBean> hostDetailList;
    private String idType = "";
    private Boolean isGuest;

    public static Intent getStartIntent(Context context) {
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
        mViewModel.setNavigator(this);
        getViewDataBinding().toolbar.tvTitle.setText(R.string.title_add_visitor);
        setIntentData(getIntent());
        setUp();
        setUpHouseNoSearch();
        randomCheckInObserver();
        setUpProfileSearch();
        setUpCompanySearch();
    }

    private void updateFieldConfigurationUI() {
        GuestConfigurationResponse guestConfiguration = mViewModel.getDataManager().getGuestConfiguration();
        if (isGuest == null || isGuest) {
            getViewDataBinding().llNumber.setVisibility(guestConfiguration.getGuestField().isContactNo() ? View.VISIBLE : View.GONE);
            getViewDataBinding().etAddress.setVisibility(guestConfiguration.getGuestField().isAddress() ? View.VISIBLE : View.GONE);
            getViewDataBinding().tvGender.setVisibility(guestConfiguration.getGuestField().isGender() ? View.VISIBLE : View.GONE);
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
                    updateVisitorUI(mViewModel.getVisitorTypeList().get(0).toString());
                    if (!mViewModel.getDataManager().getGuestConfiguration().isDataUpdated) {
                        mViewModel.doGetGuestConfiguration(this);
                    }
                    break;

                case AppConstants.CONTROLLER_SP:
                    getViewDataBinding().toolbar.tvTitle.setText(R.string.title_add_sp);
                    getViewDataBinding().tvVisitorType.setVisibility(View.GONE);
                    getViewDataBinding().tvAssignedTo.setVisibility(View.VISIBLE);
                    updateVisitorUI(mViewModel.getVisitorTypeList().get(1).toString());
                    break;

                default:
                    if (!mViewModel.getDataManager().getGuestConfiguration().isDataUpdated) {
                        mViewModel.doGetGuestConfiguration(this);
                    }
                    break;
            }
        }

        if (intent.hasExtra("Record")) {
            //setMrzData((MrzRecord) Objects.requireNonNull(intent.getSerializableExtra("Record")));
            setSmartScanData();
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
                mViewModel.doGetHouseDetails(s.toString().trim());
            }
        });

        ArrayAdapter<HouseDetailBean> arrayAdapter = new ArrayAdapter<>(AddVisitorActivity.this, android.R.layout.simple_list_item_1, new ArrayList<>());
        arrayAdapter.setNotifyOnChange(true);
        getViewDataBinding().actvHouseNo.setAdapter(arrayAdapter);
        getViewDataBinding().actvHouseNo.setText("", false);
        getViewDataBinding().actvHouseNo.setOnItemClickListener((adapterView, view, i, l) -> {
            HouseDetailBean houseDetailBean = (HouseDetailBean) adapterView.getItemAtPosition(i);
            houseId = String.valueOf(houseDetailBean.getId());
            getViewDataBinding().actvHouseNo.setText(houseDetailBean.getName());

            /*Reset Data once House Info Change*/
            //ownerId = "";
            //getViewDataBinding().tvOwner.setText("");
            residentId = "";
            getViewDataBinding().tvHost.setText("");

            mViewModel.doGetHostDetails(houseId);
            /*End Here*/
        });

        mViewModel.doGetHouseDetails().observe(AddVisitorActivity.this, houseDetailList -> {
            arrayAdapter.clear();
            arrayAdapter.addAll(houseDetailList);
        });

        mViewModel.doGetHostDetails().observe(this, hostDetailList -> {
            this.hostDetailList = hostDetailList;
            getViewDataBinding().tvHost.setVisibility(View.VISIBLE);
            //setUpOwner(false);
        });
    }

    private void setUpProfileSearch() {
        getViewDataBinding().actvWorkProfile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.doGetProfileSuggestions(s.toString());
            }
        });

        ArrayAdapter<ProfileBean> arrayAdapter = new ArrayAdapter<>(AddVisitorActivity.this, android.R.layout.simple_list_item_1, new ArrayList<>());
        getViewDataBinding().actvWorkProfile.setAdapter(arrayAdapter);
        getViewDataBinding().actvWorkProfile.setOnItemClickListener((adapterView, view, i, l) -> {
            ProfileBean profileBean = (ProfileBean) adapterView.getItemAtPosition(i);
            getViewDataBinding().actvWorkProfile.setText(profileBean.getProfileName());
        });
        mViewModel.getProfileSuggestions().observe(AddVisitorActivity.this, profileBeanList -> {
            arrayAdapter.clear();
            arrayAdapter.addAll(profileBeanList);
        });
    }

    private void setUpCompanySearch() {
        getViewDataBinding().actvCompanyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.doGetCompanySuggestions(s.toString());
            }
        });
        ArrayAdapter<CompanyBean> arrayAdapter = new ArrayAdapter<>(AddVisitorActivity.this, android.R.layout.simple_list_item_1, new ArrayList<>());
        getViewDataBinding().actvCompanyName.setAdapter(arrayAdapter);
        getViewDataBinding().actvCompanyName.setOnItemClickListener((adapterView, view, i, l) -> {
            CompanyBean companyBean = (CompanyBean) adapterView.getItemAtPosition(i);
            getViewDataBinding().actvCompanyName.setText(companyBean.getCompanyName());
        });

        mViewModel.getCompanySuggestions().observe(AddVisitorActivity.this, companyBeanList -> {
            arrayAdapter.clear();
            arrayAdapter.addAll(companyBeanList);
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
        countryCode = getViewModel().getDataManager().getPropertyDialingCode();
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        ImageView imgScan = findViewById(R.id.img_search);
        if (isGuest == null)
            imgScan.setVisibility(View.VISIBLE);
        imgScan.setImageDrawable(getResources().getDrawable(R.drawable.ic_scan));
        imgScan.setOnClickListener(v -> {
            //Intent i = ScanIDActivity.getStartIntent(getContext());
            Intent i = ScanSmartActivity.getStartIntent(getContext());
            startActivityForResult(i, SCAN_RESULT);
        });
        setOnClickListener(imgBack, getViewDataBinding().tvVisitorType, getViewDataBinding().tvNationality, getViewDataBinding().tvAssignedTo, getViewDataBinding().tvEmployment, getViewDataBinding().tvIdentity, getViewDataBinding().tvGender, getViewDataBinding().tvOwner, getViewDataBinding().tvHost
                , getViewDataBinding().frameImg, getViewDataBinding().btnAdd, getViewDataBinding().rlCode);
        getViewDataBinding().tvCode.setText("+".concat(countryCode));
        getViewDataBinding().etIdentity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty() || s.toString().trim().length() > 1) {
                    getViewDataBinding().tvIdentity.setVisibility(s.toString().trim().isEmpty() ? View.GONE : View.VISIBLE);
                    getViewDataBinding().tvNationality.setVisibility(s.toString().trim().isEmpty() ? View.GONE : View.VISIBLE);
                }
            }
        });
    }

    private void setOnClickListener(View... views) {
        for (View view : views)
            view.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                SelectionBottomSheetDialog.newInstance(getString(R.string.select_visitor_type), mViewModel.getVisitorTypeList()).setItemSelectedListener(pos -> {
                    String value = mViewModel.getVisitorTypeList().get(pos).toString();
                    updateVisitorUI(value);
                }).show(getSupportFragmentManager());
                break;

            case R.id.tv_assigned_to:
                SelectionBottomSheetDialog.newInstance(getString(R.string.select_assigned_to), mViewModel.getAssignedToList()).setItemSelectedListener(pos -> {
                    String value = mViewModel.getAssignedToList().get(pos).toString();
                    getViewDataBinding().tvAssignedTo.setText(value);
                    if (value.equals("Property")) {
                        getViewDataBinding().groupResident.setVisibility(View.GONE);
                    } else {
                        getViewDataBinding().groupResident.setVisibility(View.VISIBLE);
                    }
                }).show(getSupportFragmentManager());
                break;

            case R.id.tv_identity:
                SelectionBottomSheetDialog.newInstance(getString(R.string.select_identity_type), mViewModel.getIdentityTypeList()).setItemSelectedListener(pos -> {
                    IdentityBean bean = (IdentityBean) mViewModel.getIdentityTypeList().get(pos);
                    getViewDataBinding().tvIdentity.setText(bean.getTitle());
                    idType = bean.getKey();
                }).show(getSupportFragmentManager());
                break;

            case R.id.tv_gender:
                SelectionBottomSheetDialog.newInstance(getString(R.string.select_gender), mViewModel.getGenderList()).setItemSelectedListener(pos -> getViewDataBinding().tvGender.setText(mViewModel.getGenderList().get(pos))).show(getSupportFragmentManager());
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
                CountrySelectionDialog.newInstance(true, countryResponse -> {
                    countryCode = countryResponse.getDial_code();
                    getViewDataBinding().tvCode.setText("+".concat(countryCode));
                }).show(getSupportFragmentManager());
                break;

            case R.id.tv_nationality:
                CountrySelectionDialog.newInstance(false, countryResponse -> getViewDataBinding().tvNationality.setText(countryResponse.getNationality())).show(getSupportFragmentManager());
                break;

            case R.id.tv_employment:
                SelectionBottomSheetDialog.newInstance(getString(R.string.select_employment), mViewModel.getEmploymentTypeList()).setItemSelectedListener(pos -> {
                    String value = mViewModel.getEmploymentTypeList().get(pos).toString();
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
                visitorData.nationality = getViewDataBinding().tvNationality.getText().toString();
                visitorData.name = getViewDataBinding().etName.getText().toString().trim();
                visitorData.contact = getViewDataBinding().etContact.getText().toString().trim();
                visitorData.address = getViewDataBinding().etAddress.getText().toString().trim();
                visitorData.gender = getViewDataBinding().tvGender.getText().toString();
                visitorData.houseId = houseId;
                visitorData.residentId = residentId;
                if (isGuest) {
                    if (mViewModel.doVerifyGuestInputs(visitorData, mViewModel.getDataManager().getGuestConfiguration())) {
                        mViewModel.doCheckGuestStatus(getViewDataBinding().etIdentity.getText().toString().trim(), idType);
                    }
                } else {
                    visitorData.assignedTo = getViewDataBinding().tvAssignedTo.getText().toString();
                    visitorData.isResident = !visitorData.assignedTo.equalsIgnoreCase("Property");
                    visitorData.visitorType = getViewDataBinding().tvVisitorType.getText().toString();
                    visitorData.nationality = getViewDataBinding().tvNationality.getText().toString();
                    visitorData.employment = getViewDataBinding().tvEmployment.getText().toString();
                    visitorData.isCompany = !visitorData.employment.equalsIgnoreCase("Self");
                    visitorData.profile = getViewDataBinding().actvWorkProfile.getText().toString().trim();
                    visitorData.companyName = getViewDataBinding().actvCompanyName.getText().toString().trim();
                    visitorData.companyAddress = getViewDataBinding().etCompanyAddress.getText().toString().trim();


                    if (mViewModel.doVerifySPInputs(visitorData)) {
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
                    doAddSp(false, visitorData);
                }).show(getSupportFragmentManager());
    }


    private void doAddSp(boolean isAccept, AddVisitorData visitorData) {
        visitorData.bmp_profile = bmp_profile;
        visitorData.vehicleNo = getViewDataBinding().etVehicle.getText().toString().trim();
        visitorData.dialingCode = countryCode;
        visitorData.isAccept = isAccept;
        mViewModel.doAddSp(visitorData);
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
        setIdentity();
        updateFieldConfigurationUI();
    }

    private void setIdentity() {
        if (mViewModel.getDataManager().isIdentifyFeature() || !isGuest) {
            getViewDataBinding().etIdentity.setVisibility(View.VISIBLE);
            getViewDataBinding().tvIdentity.setVisibility(View.VISIBLE);
            getViewDataBinding().tvNationality.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().etIdentity.setVisibility(View.GONE);
            getViewDataBinding().tvIdentity.setVisibility(View.GONE);
            getViewDataBinding().tvNationality.setVisibility(View.GONE);
        }
    }

    private void randomCheckInObserver() {
        mViewModel.getGuestStatusMutableData().observe(this, isBlock -> {
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
                            doAddGuest(true, null);
                        }).show(getSupportFragmentManager());
            }
        });
    }


    private void showReasonGuestDialog() {
        InputDialog.newInstance().setTitle(getString(R.string.are_you_sure))
                .setOnPositiveClickListener((dialog, input) -> {
                    dialog.dismiss();
                    doAddGuest(false, input);
                }).show(getSupportFragmentManager());
    }

    private void doAddGuest(boolean isAccept, String input) {
        AddVisitorData addVisitorData = new AddVisitorData();
        addVisitorData.isAccept = isAccept;
        addVisitorData.bmp_profile = bmp_profile;
        addVisitorData.identityNo = getViewDataBinding().etIdentity.getText().toString().trim();
        addVisitorData.idType = idType;
        addVisitorData.nationality = getViewDataBinding().tvNationality.getText().toString();
        addVisitorData.name = getViewDataBinding().etName.getText().toString();
        addVisitorData.vehicleNo = getViewDataBinding().etVehicle.getText().toString().trim();
        addVisitorData.contact = getViewDataBinding().etContact.getText().toString();
        addVisitorData.dialingCode = countryCode;
        addVisitorData.address = getViewDataBinding().etAddress.getText().toString();
        addVisitorData.gender = (mViewModel.getDataManager().getGuestConfiguration().getGuestField().isGender()) ? getViewDataBinding().tvGender.getText().toString() : "";
        addVisitorData.houseId = houseId;
        addVisitorData.residentId = residentId;

        if (!isAccept) {
            addVisitorData.rejectedReason = input;
        }
        mViewModel.doAddGuest(addVisitorData);
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

    @Override
    public void onSuccess(GuestConfigurationResponse configurationResponse) {
        updateFieldConfigurationUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SCAN_RESULT /*&& data != null*/) {
                /*MrzRecord mrzRecord = (MrzRecord) data.getSerializableExtra("Record");
                assert mrzRecord != null;
                setMrzData(mrzRecord);*/
                /*for (String name : MainResultStore.instance.getFieldNames()) {
                    Log.e("FieldName",name);
                }*/
                setSmartScanData();
            }
        }
    }

    private void setSmartScanData() {
        getViewDataBinding().tvIdentity.setEnabled(true);
        ScannedIDData scannedData = MainResultStore.instance.getScannedIDData();
        getViewDataBinding().etIdentity.setText(scannedData.idNumber);
        getViewDataBinding().etName.setText(scannedData.name);
        if (scannedData.nationality.isEmpty()) {
            getViewDataBinding().tvNationality.setEnabled(true);
        } else {
            getViewDataBinding().tvNationality.setEnabled(false);
            getViewDataBinding().tvNationality.setText(scannedData.nationality);
        }

        String gender = scannedData.gender;
        if (gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("Male"))
            getViewDataBinding().tvGender.setText(R.string.male);
        else if (gender.equalsIgnoreCase("F") || gender.equalsIgnoreCase("Female"))
            getViewDataBinding().tvGender.setText(R.string.female);
        else getViewDataBinding().tvGender.setText("");
        bmp_profile = scannedData.userImage;
        if (bmp_profile != null) {
            getViewDataBinding().imgUser.setImageBitmap(bmp_profile);
        } else {
            getViewDataBinding().imgUser.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_person));
        }

        switch (MainResultStore.instance.getDocumentType()) {
            case Constant.ID_KENYAN:
            case Constant.ID_UGANDA:
            case Constant.ID_TANZANIA:
            case Constant.ID_RWANDA:
            case Constant.ID_UAE:
            case Constant.ID_AADHAAR:
            case Constant.ID_PANCARD:
                IdentityBean bean = (IdentityBean) mViewModel.getIdentityTypeList().get(0);
                getViewDataBinding().tvIdentity.setEnabled(false);
                getViewDataBinding().tvIdentity.setText(bean.getTitle());
                idType = bean.getKey();
                break;

            case Constant.PASSPORT_KENYAN:
            case Constant.PASSPORT_UGANDA:
            case Constant.PASSPORT_TANZANIA:
            case Constant.PASSPORT_TANZANIA_OLD:
                    /*case Constant.PASSPORT_RWANDA:
                    case Constant.PASSPORT_UAE:*/
            case Constant.PASSPORT_INDIA:
                bean = (IdentityBean) mViewModel.getIdentityTypeList().get(2);
                getViewDataBinding().tvIdentity.setEnabled(false);
                getViewDataBinding().tvIdentity.setText(bean.getTitle());
                idType = bean.getKey();
                break;
        }
    }

    void setMrzData(MrzRecord mrzRecord) {
        assert mrzRecord != null;
        String code = mrzRecord.getCode1() + "" + mrzRecord.getCode2();
        switch (code.toLowerCase()) {
            case "p<":
            case "p":
                IdentityBean bean = (IdentityBean) mViewModel.getIdentityTypeList().get(1);
                getViewDataBinding().tvIdentity.setText(bean.getTitle());
                idType = bean.getKey();
                getViewDataBinding().etIdentity.setText(mrzRecord.getDocumentNumber());
                break;

            case "ac":
            case "id":
                bean = (IdentityBean) mViewModel.getIdentityTypeList().get(0);
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
