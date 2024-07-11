package com.evisitor.ui.main.home.recurrentvisitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.IdentityBean;
import com.evisitor.data.model.RecurrentVisitor;
import com.evisitor.databinding.ActivityFilterRecurrentVisitorBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.dialog.country.CountrySelectionDialog;
import com.evisitor.ui.dialog.selection.SelectionBottomSheetDialog;
import com.evisitor.ui.main.commercial.add.CommercialAddVisitorActivity;
import com.evisitor.ui.main.home.scan.BarcodeScanActivity;
import com.evisitor.ui.main.residential.add.AddVisitorActivity;
import com.evisitor.util.PermissionUtils;

import static com.evisitor.util.AppConstants.SCAN_RESULT;

public class FilterRecurrentVisitorActivity extends BaseActivity<ActivityFilterRecurrentVisitorBinding, FilterRecurrentVisitorViewModel> implements FilterRecurrentVisitorNavigator, View.OnClickListener {

    private String countryCode = "254";
    private String idType = "";
    private Boolean isGuest;
    private Boolean isFilterByID = true;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, FilterRecurrentVisitorActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_filter_recurrent_visitor;
    }

    @Override
    public FilterRecurrentVisitorViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(FilterRecurrentVisitorViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        getViewDataBinding().header.tvTitle.setText(R.string.title_filter_recurrent_visitor);
        getViewDataBinding().header.imgBack.setVisibility(View.VISIBLE);
        setOnClickListener(getViewDataBinding().header.imgBack, getViewDataBinding().tvVisitorType, getViewDataBinding().tvFilterType, getViewDataBinding().tvIdentity,
                getViewDataBinding().rlCode, getViewDataBinding().btnSearch, getViewDataBinding().tvNewVisitor);
        countryCode = getViewModel().getDataManager().getPropertyDialingCode();
        getViewDataBinding().tvCode.setText("+".concat(countryCode));
    }

    private void scanQR() {
        if (PermissionUtils.checkCameraPermission(FilterRecurrentVisitorActivity.this)) {
            Intent i = BarcodeScanActivity.getStartIntent(FilterRecurrentVisitorActivity.this);
            startActivityForResult(i, SCAN_RESULT);
        }
    }

    private void setOnClickListener(View... views) {
        for (View view : views)
            view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.img_back) {
            onBackPressed();
        } else if (id == R.id.tv_visitor_type) {
            SelectionBottomSheetDialog.newInstance(getString(R.string.select_visitor_type), mViewModel.getVisitorTypeList()).setItemSelectedListener(pos -> {
                String value = mViewModel.getVisitorTypeList().get(pos).toString();
                getViewDataBinding().tvVisitorType.setText(value);
                isGuest = value.equals("Guest") || value.equals("Visitor");
            }).show(getSupportFragmentManager());
        } else if (id == R.id.tv_filter_type) {
            SelectionBottomSheetDialog.newInstance(getString(R.string.select_filter_type), mViewModel.getFilterTypeList()).setItemSelectedListener(pos -> {
                String value = mViewModel.getFilterTypeList().get(pos).toString();
                getViewDataBinding().tvFilterType.setText(value);
                if (value.equalsIgnoreCase("Identity")) {
                    isFilterByID = true;
                    getViewDataBinding().groupId.setVisibility(View.VISIBLE);
                    getViewDataBinding().groupOther.setVisibility(View.GONE);

                    getViewDataBinding().etName.setText("");
                    getViewDataBinding().etContact.setText("");
                } else {
                    isFilterByID = false;
                    getViewDataBinding().groupId.setVisibility(View.GONE);
                    getViewDataBinding().groupOther.setVisibility(View.VISIBLE);

                    getViewDataBinding().etIdentity.setText("");
                    getViewDataBinding().tvIdentity.setText("");
                }
            }).show(getSupportFragmentManager());
        } else if (id == R.id.tv_identity) {
            SelectionBottomSheetDialog.newInstance(getString(R.string.select_identity_type), mViewModel.getIdentityTypeList()).setItemSelectedListener(pos -> {
                IdentityBean bean = (IdentityBean) mViewModel.getIdentityTypeList().get(pos);
                getViewDataBinding().tvIdentity.setText(bean.getTitle());
                idType = bean.getKey();
            }).show(getSupportFragmentManager());
        } else if (id == R.id.rl_code) {
            CountrySelectionDialog.newInstance(true, countryResponse -> {
                countryCode = countryResponse.getDial_code();
                getViewDataBinding().tvCode.setText("+".concat(countryCode));
            }).show(getSupportFragmentManager());
        } else if (id == R.id.tv_new_visitor) {
            Intent i = mViewModel.getDataManager().isCommercial() ? CommercialAddVisitorActivity.getStartIntent(getContext()) : AddVisitorActivity.getStartIntent(getContext());
            startActivity(i);
        } else if (id == R.id.btn_search) {
            mViewModel.getFilteredVisitorData(isGuest, isFilterByID, getViewDataBinding().etIdentity.getText().toString().trim(), idType,
                    getViewDataBinding().etName.getText().toString().trim(), countryCode, getViewDataBinding().etContact.getText().toString().trim());
        }
    }

    @Override
    public void onResponseSuccess(RecurrentVisitor recurrentVisitor) {
        Intent i = mViewModel.getDataManager().isCommercial() ? CommercialAddVisitorActivity.getStartIntent(getContext()) : AddVisitorActivity.getStartIntent(getContext());
        i.putExtra("RecurrentData", recurrentVisitor);
        startActivity(i);
    }

}
