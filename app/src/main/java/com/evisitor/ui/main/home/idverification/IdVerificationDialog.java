package com.evisitor.ui.main.home.idverification;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.DialogIdVerificationBinding;
import com.evisitor.ui.base.BaseDialog;

public class IdVerificationDialog extends BaseDialog<DialogIdVerificationBinding, IdVerificationViewModel> implements View.OnClickListener {
    private static final String TAG = "IdVerificationDialog";

    private IdVerificationCallback callback;

    public static IdVerificationDialog newInstance(IdVerificationCallback callback) {
        Bundle args = new Bundle();
        IdVerificationDialog fragment = new IdVerificationDialog();
        fragment.setCallback(callback);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(getBaseActivity());
    }

    private void setCallback(IdVerificationCallback callback) {
        this.callback = callback;
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_id_verification;
    }

    @Override
    public IdVerificationViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(IdVerificationViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().imgClose.setOnClickListener(this);
        getViewDataBinding().btnScan.setOnClickListener(this);
        getViewDataBinding().btnSubmit.setOnClickListener(this);
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        switch (v.getId()) {
            case R.id.img_close:
                dismissDialog(TAG);
                break;

            case R.id.btn_scan:
                if (callback != null) {
                    callback.onScanClick(this);
                } else dismissDialog(TAG);
                break;

            case R.id.btn_submit:
                if (callback != null) {
                    callback.onSubmitClick(this, getViewDataBinding().etData.getText().toString().trim());
                } else dismissDialog(TAG);
                break;
        }
    }
}
