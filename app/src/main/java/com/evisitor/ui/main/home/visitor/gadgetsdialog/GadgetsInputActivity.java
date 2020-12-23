package com.evisitor.ui.main.home.visitor.gadgetsdialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.DeviceBean;
import com.evisitor.databinding.GadgetsInputDialogBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.util.AppLogger;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class GadgetsInputActivity extends BaseActivity<GadgetsInputDialogBinding, GadgetsInputViewModel> implements View.OnClickListener {

    List<DeviceBean> beans;
    GadgetsAdapter adapter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, GadgetsInputActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.gadgets_input_dialog;
    }

    @Override
    public GadgetsInputViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(GadgetsInputViewModel.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
    }

    private void setUp() {
        getViewDataBinding().toolbar.tvTitle.setText(R.string.enter_gadget_info);
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
        ImageView imgSearch = findViewById(R.id.img_search);
        imgSearch.setImageDrawable(getDrawable(R.drawable.ic_add));
        imgSearch.setVisibility(View.VISIBLE);
        imgSearch.setOnClickListener(this);
        getViewDataBinding().btnOk.setOnClickListener(this);
        beans = new ArrayList<>();
        beans.add(new DeviceBean("Device 1", "", "", "", "", ""));
        adapter = new GadgetsAdapter(beans, deviceList -> beans = deviceList);
        getViewDataBinding().recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.btn_ok:
                if (!beans.isEmpty()) {
                    if (getViewModel().verifyDeviceDetails(beans)) {
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        String yourListAsString = new Gson().toJson(beans);
                        AppLogger.i("Device List", yourListAsString);
                        bundle.putString("data", yourListAsString);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        showAlert(R.string.alert, R.string.please_fill_details).show(getSupportFragmentManager());
                    }
                }
                break;

            case R.id.img_search:
                if (beans.size() < 5) {
                    if (getViewModel().verifyDeviceDetails(beans)) {
                        beans.add(new DeviceBean("Device ".concat(String.valueOf(beans.size() + 1)), "", "", "", "", ""));
                        adapter.notifyDataSetChanged();
                    } else
                        showAlert(R.string.alert, R.string.please_fill_details).show(getSupportFragmentManager());
                } else
                    showAlert(R.string.alert, R.string.can_enter_more_device).show(getSupportFragmentManager());
                break;
        }
    }

}
