package com.evisitor.ui.dialog;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.DialogAlertBinding;
import com.evisitor.ui.base.BaseDialog;


public class AlertDialog extends BaseDialog<DialogAlertBinding, AlertViewModel> implements View.OnClickListener {

    private CancelListener cancelListener;
    private OKListener okListener;

    //default param
    private String msg = "";
    private String mTitle = "";
    private String labelOkBtn = "";
    private String labelCancelBtn = "";
    private boolean isCancelVisible = true;

    public static AlertDialog newInstance() {

        Bundle args = new Bundle();
        AlertDialog fragment = new AlertDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public AlertDialog setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public AlertDialog setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public AlertDialog setLabelOkBtn(String labelOkBtn) {
        this.labelOkBtn = labelOkBtn;
        return this;
    }

    public AlertDialog setLabelCancelBtn(String labelCancelBtn) {
        this.labelCancelBtn = labelCancelBtn;
        return this;
    }

    public AlertDialog setCancelGone() {
        this.isCancelVisible = false;
        return this;
    }

    public AlertDialog setCancelViewVisible() {
        this.isCancelVisible = true;
        return this;
    }

    public AlertDialog setOnCancelClickListener(CancelListener cancelListener) {
        this.cancelListener = cancelListener;
        return this;
    }

    public AlertDialog setOnOKClickListener(OKListener okListener) {
        this.okListener = okListener;
        return this;
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_alert;
    }

    @Override
    public AlertViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(AlertViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViewDataBinding().tvTitle.setText(mTitle);
        getViewDataBinding().tvMsg.setText(msg);

        if (!labelOkBtn.isEmpty())
            getViewDataBinding().btnOk.setText(labelOkBtn);
        if (!labelCancelBtn.isEmpty())
            getViewDataBinding().btnCancel.setText(labelCancelBtn);
        if (isCancelVisible) {
            getViewDataBinding().btnOk.setBackground(getResources().getDrawable(R.drawable.bg_bottom_right_primary_corner));
            getViewDataBinding().btnCancel.setVisibility(View.VISIBLE);
            getViewDataBinding().btnCancel.setOnClickListener(this);
        } else {
            getViewDataBinding().btnOk.setBackground(getResources().getDrawable(R.drawable.bg_bottom_lr_primary_corner));
            getViewDataBinding().btnCancel.setVisibility(View.GONE);
        }
        getViewDataBinding().btnOk.setOnClickListener(this);
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (cancelListener != null) {
                    cancelListener.onCancelClick(this);
                }
                break;

            case R.id.btn_ok:
                if (okListener != null) {
                    okListener.onOkClick(this);
                }
                break;
        }
    }

    public interface CancelListener {
        void onCancelClick(AlertDialog dialog);
    }

    public interface OKListener {
        void onOkClick(AlertDialog dialog);
    }
}
