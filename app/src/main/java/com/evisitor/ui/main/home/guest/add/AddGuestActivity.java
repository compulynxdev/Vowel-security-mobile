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
import com.evisitor.databinding.ActivityAddGuestBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.dialog.ImagePickBottomSheetDialog;
import com.evisitor.ui.dialog.ImagePickCallback;
import com.evisitor.ui.main.home.guest.add.dialogs.GenderPickerBottomSheetDialog;
import com.evisitor.ui.main.home.guest.add.dialogs.HostPickerBottomSheetDialog;
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
    }

    private void setIntentData(Intent intent) {
        if (getIntent().hasExtra("Record")) {
            MrzRecord mrzRecord = (MrzRecord) intent.getSerializableExtra("Record");
            assert mrzRecord != null;

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
                getViewModel().doGetHouseDetails(s.toString()).observe(AddGuestActivity.this, houseDetailList -> {
                    ArrayAdapter<HouseDetailBean> arrayAdapter = new ArrayAdapter<>(AddGuestActivity.this, android.R.layout.simple_list_item_1, houseDetailList);
                    getViewDataBinding().actvHouseNo.setThreshold(2);
                    getViewDataBinding().actvHouseNo.setAdapter(arrayAdapter);

                    getViewDataBinding().actvHouseNo.setOnItemClickListener((adapterView, view, i, l) -> {
                        HouseDetailBean houseDetailBean = (HouseDetailBean) adapterView.getItemAtPosition(i);
                        houseId = String.valueOf(houseDetailBean.getId());
                        getViewDataBinding().actvHouseNo.setText(houseDetailBean.getName());

                        getHostData(houseId);
                    });
                });
            }
        });
    }

    private void getHostData(String houseId) {
        getViewModel().doGetHostDetails(houseId).observe(this, hostDetailList -> {
            this.hostDetailList = hostDetailList;
            getViewDataBinding().hostGroup.setVisibility(View.VISIBLE);

            setUpOwner(hostDetailList, false);
        });
    }

    private void setUpOwner(List<HostDetailBean> hostDetailList, boolean isDialogShow) {
        List<HostDetailBean> ownerList = new ArrayList<>();
        for (HostDetailBean tmp : hostDetailList) {
            if (tmp.isIsOwner()) ownerList.add(tmp);
        }

        if (ownerList.size() == 1) {
            HostDetailBean bean = ownerList.get(0);
            ownerId = String.valueOf(bean.getId());
            getViewDataBinding().tvOwner.setText(bean.getFullName());
        } else {
            if (isDialogShow) {
                HostPickerBottomSheetDialog.newInstance(ownerList, bean -> {
                    ownerId = String.valueOf(bean.getId());
                    getViewDataBinding().tvOwner.setText(bean.getFullName());
                }).show(getSupportFragmentManager());
            }
        }
    }

    private void setUp() {
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        setOnClickListener(imgBack, getViewDataBinding().tvGender, getViewDataBinding().tvOwner, getViewDataBinding().tvHost
                , getViewDataBinding().frameImg, getViewDataBinding().btnAdd);
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

            case R.id.tv_gender:
                GenderPickerBottomSheetDialog.newInstance().setItemSelectedListener(data -> getViewDataBinding().tvGender.setText(data)).show(getSupportFragmentManager());
                break;

            case R.id.tv_owner:
                if (hostDetailList != null)
                    setUpOwner(hostDetailList, true);
                break;

            case R.id.tv_host:
                if (hostDetailList != null) {
                    HostPickerBottomSheetDialog.newInstance(hostDetailList, bean -> {
                        residentId = String.valueOf(bean.getId());
                        getViewDataBinding().tvHost.setText(bean.getFullName());
                    }).show(getSupportFragmentManager());
                }
                break;

            case R.id.btn_add:
                getViewModel().doVerifyInputsAndAddGuest(bmp_profile, getViewDataBinding().etIdentity.getText().toString(), getViewDataBinding().etName.getText().toString()
                        , getViewDataBinding().etVehicle.getText().toString(), getViewDataBinding().etContact.getText().toString()
                        , getViewDataBinding().etAddress.getText().toString(), getViewDataBinding().tvGender.getText().toString()
                        , getViewDataBinding().actvHouseNo.getText().toString()
                        , houseId, ownerId, residentId);
                break;
        }
    }

    @Override
    public void onSuccess() {
        AlertDialog.newInstance()
                .setNegativeBtnShow(true)
                .setCloseBtnShow(false)
                .setTitle(getString(R.string.check_in))
                .setMsg(getString(R.string.msg_check_in_call))
                .setPositiveBtnLabel(getString(R.string.approve))
                .setNegativeBtnLabel(getString(R.string.reject))
                .setOnNegativeClickListener(dialog1 -> {
                    dialog1.dismiss();
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                }).show(getSupportFragmentManager());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntentData(intent);
    }
}
