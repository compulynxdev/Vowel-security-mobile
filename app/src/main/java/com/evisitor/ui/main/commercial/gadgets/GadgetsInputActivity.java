package com.evisitor.ui.main.commercial.gadgets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.DeviceBean;
import com.evisitor.databinding.GadgetsInputDialogBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.util.AppLogger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GadgetsInputActivity extends BaseActivity<GadgetsInputDialogBinding, GadgetsInputViewModel> implements View.OnClickListener {

    private List<DeviceBean> beans;
    private GadgetsAdapter adapter;
    private boolean isAdd;

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
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(GadgetsInputViewModel.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        setUpIntent(getIntent());
        setUp();
        setUpAdapter();
    }

    private void setUpIntent(Intent intent) {
        beans = new ArrayList<>();
        if (intent.hasExtra("list")) {
            Type listType = new TypeToken<List<DeviceBean>>() {
            }.getType();
            beans.addAll(Objects.requireNonNull(mViewModel.getDataManager().getGson().fromJson(intent.getStringExtra("list"), listType)));
        }
        isAdd = intent.getBooleanExtra("add", false);
    }

    private void setUpAdapter() {
        if (beans.isEmpty())
            beans.add(new DeviceBean(getString(R.string.device).concat(" ").concat("1"), "", "", "", "", ""));
        adapter = new GadgetsAdapter(beans, new GadgetsAdapter.AdapterCallback() {
            @Override
            public void onChangeList(List<DeviceBean> deviceList) {
                beans = deviceList;
            }

            @Override
            public void onRemove(int position) {
                beans.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        adapter.setIsAdd(isAdd);
        getViewDataBinding().recyclerView.setAdapter(adapter);
    }

    private void setUp() {
        getViewDataBinding().toolbar.tvTitle.setText(R.string.enter_gadget_info);
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
        ImageView imgSearch = findViewById(R.id.img_search);
        imgSearch.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add));
        if (isAdd) {
            imgSearch.setVisibility(View.VISIBLE);
            getViewDataBinding().btnOk.setVisibility(View.VISIBLE);
        } else {
            imgSearch.setVisibility(View.GONE);
            getViewDataBinding().btnOk.setVisibility(View.GONE);
        }
        imgSearch.setOnClickListener(this);
        getViewDataBinding().btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.img_back) {
            onBackPressed();
        } else if (id == R.id.btn_ok) {
            if (!beans.isEmpty()) {
                if (mViewModel.verifyDeviceDetails(beans)) {
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
            } else {
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                String yourListAsString = new Gson().toJson(beans);
                AppLogger.i("Device List", yourListAsString);
                bundle.putString("data", yourListAsString);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else if (id == R.id.img_search) {
            if (beans.size() < 5) {
                if (mViewModel.verifyDeviceDetails(beans)) {
                    beans.add(new DeviceBean(getString(R.string.device).concat(" ").concat(String.valueOf(beans.size() + 1)), "", "", "", "", ""));
                    adapter.notifyDataSetChanged();
                    getViewDataBinding().recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                } else
                    showAlert(R.string.alert, R.string.please_fill_details).show(getSupportFragmentManager());
            } else
                showAlert(R.string.alert, R.string.can_enter_more_device).show(getSupportFragmentManager());
        }
    }

}
