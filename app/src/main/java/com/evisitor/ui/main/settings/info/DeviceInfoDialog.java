package com.evisitor.ui.main.settings.info;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.DialogDeviceInfoBinding;
import com.evisitor.ui.base.BaseDialog;
import com.evisitor.util.CommonUtils;

public class DeviceInfoDialog extends BaseDialog<DialogDeviceInfoBinding, DeviceInfoViewModel> {

    private static final String TAG = "DeviceInfoDialog";

    public static DeviceInfoDialog newInstance() {
        Bundle args = new Bundle();

        DeviceInfoDialog fragment = new DeviceInfoDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(getBaseActivity());
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_device_info;
    }

    @Override
    public DeviceInfoViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(DeviceInfoViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().imgClose.setOnClickListener(view1 -> dismiss());

        getViewDataBinding().tvName.setText(getString(R.string.model, Build.MODEL));
        getViewDataBinding().tvId.setText(getString(R.string.MacID, CommonUtils.getDeviceId(getBaseActivity())));
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager);
    }
}
