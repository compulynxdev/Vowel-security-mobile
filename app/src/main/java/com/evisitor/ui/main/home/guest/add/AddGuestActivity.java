package com.evisitor.ui.main.home.guest.add;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.HostDetailBean;
import com.evisitor.data.model.HouseDetailBean;
import com.evisitor.data.model.IdentityBean;
import com.evisitor.databinding.ActivityAddGuestBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.dialog.ImagePickBottomSheetDialog;
import com.evisitor.ui.dialog.ImagePickCallback;
import com.evisitor.ui.main.MainActivity;
import com.evisitor.ui.main.home.guest.add.dialogs.HostPickerBottomSheetDialog;
import com.evisitor.ui.main.home.guest.add.dialogs.PickerBottomSheetDialog;
import com.evisitor.util.PermissionUtils;
import com.sharma.mrzreader.MrzRecord;

import java.util.ArrayList;
import java.util.List;

public class AddGuestActivity extends BaseActivity<ActivityAddGuestBinding,AddGuestViewModel> implements AddGuestNavigator, View.OnClickListener {

    private String houseId = "";  //also called flat id
    private String residentId = "";  //also called host id
    private String ownerId = "";  //also called house owner id
    private Bitmap bmp_profile;
    private List<HostDetailBean> hostDetailList;
    private String idType = "";

    public static Intent getStartIntent(Context context){
        return new Intent(context,AddGuestActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_guest;
    }

    @Override
    public AddGuestViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(AddGuestViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_add_guest);

        setUp();
        setUpHouseNoSearch();
        setIntentData(getIntent());
        randomCheckInObserver();
    }

    private void setIntentData(Intent intent) {
        if (getIntent().hasExtra("Record")) {
            MrzRecord mrzRecord = (MrzRecord) intent.getSerializableExtra("Record");
            assert mrzRecord != null;

            String code = mrzRecord.getCode1() + "" + mrzRecord.getCode2();
            switch (code.toLowerCase()) {
                case "p<":
                case "p":
                    IdentityBean bean = (IdentityBean) getViewModel().getIdentityTypeList().get(1);
                    getViewDataBinding().tvIdentity.setText(bean.getTitle());
                    idType = bean.getKey();
                    break;

                case "ac":
                case "id":
                    bean = (IdentityBean) getViewModel().getIdentityTypeList().get(0);
                    getViewDataBinding().tvIdentity.setText(bean.getTitle());
                    idType = bean.getKey();
                    break;
            }

            //getViewDataBinding().tvIdentity.setText(mrzRecord.getCode().toString().concat(" [").concat(mrzRecord.getCode1() + "").concat(mrzRecord.getCode2() + "]"));

            getViewDataBinding().etIdentity.setText(mrzRecord.getDocumentNumber());
            getViewDataBinding().etName.setText(mrzRecord.getGivenNames().concat(" ").concat(mrzRecord.getSurname()));
            //tv_dob.setText(CalenderUtils.formatDate(mrzRecord.getDateOfBirth().toString(), "dd/MM/yy", "dd/MM/yyyy"));

            if (mrzRecord.getSex() != null) {
                if (mrzRecord.getSex().toString().equalsIgnoreCase("M") || mrzRecord.getSex().toString().equalsIgnoreCase("Male"))
                    getViewDataBinding().tvGender.setText(R.string.male);
                else getViewDataBinding().tvGender.setText(R.string.female);
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

        getViewModel().doGetHouseDetails().observe(AddGuestActivity.this, houseDetailList -> {
            ArrayAdapter<HouseDetailBean> arrayAdapter = new ArrayAdapter<>(AddGuestActivity.this, android.R.layout.simple_list_item_1, houseDetailList);
            getViewDataBinding().actvHouseNo.setThreshold(2);
            getViewDataBinding().actvHouseNo.setAdapter(arrayAdapter);

            getViewDataBinding().actvHouseNo.setOnItemClickListener((adapterView, view, i, l) -> {
                HouseDetailBean houseDetailBean = (HouseDetailBean) adapterView.getItemAtPosition(i);
                houseId = String.valueOf(houseDetailBean.getId());
                getViewDataBinding().actvHouseNo.setText(houseDetailBean.getName());

                /*Reset Data once House Info Change*/
                ownerId = "";
                getViewDataBinding().tvOwner.setText("");
                residentId = "";
                getViewDataBinding().tvHost.setText("");

                getViewModel().doGetHostDetails(houseId);
                /*End Here*/
            });
        });

        getViewModel().doGetHostDetails().observe(this, hostDetailList -> {
            this.hostDetailList = hostDetailList;
            getViewDataBinding().hostGroup.setVisibility(View.VISIBLE);
            setUpOwner(false);
        });
    }

    private void setUpOwner(boolean isDialogShow) {
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
    }

    private void setUp() {
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        setOnClickListener(imgBack, getViewDataBinding().tvIdentity, getViewDataBinding().tvGender, getViewDataBinding().tvOwner, getViewDataBinding().tvHost
                , getViewDataBinding().frameImg, getViewDataBinding().btnAdd);

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

            case R.id.tv_identity:
                PickerBottomSheetDialog.newInstance(getViewModel().getIdentityTypeList()).setItemSelectedListener(pos -> {
                    IdentityBean bean = (IdentityBean) getViewModel().getIdentityTypeList().get(pos);
                    getViewDataBinding().tvIdentity.setText(bean.getTitle());
                    idType = bean.getKey();
                }).show(getSupportFragmentManager());
                break;

            case R.id.tv_gender:
                PickerBottomSheetDialog.newInstance(getViewModel().getGenderList()).setItemSelectedListener(pos -> getViewDataBinding().tvGender.setText(getViewModel().getGenderList().get(pos))).show(getSupportFragmentManager());
                break;

            case R.id.tv_owner:
                setUpOwner(true);
                break;

            case R.id.tv_host:
                if (hostDetailList != null) {
                    if (hostDetailList.isEmpty()) {
                        hostDetailList.add(new HostDetailBean(-1, getString(R.string.no_host_found), false));
                    }
                    HostPickerBottomSheetDialog.newInstance(hostDetailList, bean -> {
                        if (bean.getId() != -1) {
                            residentId = String.valueOf(bean.getId());
                            getViewDataBinding().tvHost.setText(bean.getFullName());
                        }
                    }).show(getSupportFragmentManager());
                }
                break;

            case R.id.btn_add:
                if (getViewModel().doVerifyInputs(getViewDataBinding().etIdentity.getText().toString().trim(), idType
                        , getViewDataBinding().etName.getText().toString().trim(), getViewDataBinding().etContact.getText().toString().trim()
                        , getViewDataBinding().etAddress.getText().toString().trim(), getViewDataBinding().tvGender.getText().toString()
                        , houseId, ownerId, residentId)) {

                    getViewModel().doCheckGuestStatus(getViewDataBinding().etIdentity.getText().toString().trim());
                }
                break;
        }
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

                            getViewModel().doAddGuest(false, bmp_profile, getViewDataBinding().etIdentity.getText().toString().trim(), idType, getViewDataBinding().etName.getText().toString()
                                    , getViewDataBinding().etVehicle.getText().toString().trim(), getViewDataBinding().etContact.getText().toString()
                                    , getViewDataBinding().etAddress.getText().toString(), getViewDataBinding().tvGender.getText().toString()
                                    , getViewDataBinding().actvHouseNo.getText().toString()
                                    , houseId, ownerId, residentId);
                        })
                        .setOnPositiveClickListener(dialog12 -> {
                            dialog12.dismiss();
                            getViewModel().doAddGuest(true, bmp_profile, getViewDataBinding().etIdentity.getText().toString().trim(), idType, getViewDataBinding().etName.getText().toString()
                                    , getViewDataBinding().etVehicle.getText().toString().trim(), getViewDataBinding().etContact.getText().toString()
                                    , getViewDataBinding().etAddress.getText().toString(), getViewDataBinding().tvGender.getText().toString()
                                    , getViewDataBinding().actvHouseNo.getText().toString()
                                    , houseId, ownerId, residentId);
                        }).show(getSupportFragmentManager());
            }
        });
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
