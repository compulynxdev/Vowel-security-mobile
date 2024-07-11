package com.evisitor.ui.dialog;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.DialogAlertBinding;
import com.evisitor.ui.base.BaseDialog;


public class AlertDialog extends BaseDialog<DialogAlertBinding, AlertViewModel> implements View.OnClickListener {

    private static final String TAG = "AlertDialog";
    private NegativeListener negativeListener;
    private PositiveListener positiveListener;

    //default param
    private String msg = "";
    private String mTitle = "";
    private String positiveBtnLabel = "";
    private String negativeBtnLabel = "";
    private boolean isCloseBtnShow = false;
    private boolean isNegativeBtnShow = true;
    private int negativeBtnColor = R.color.red;
    private int negativeBtnBgDrawable = -1;
    private int positiveBtnColor = R.color.white;
    private int positiveBtnBgDrawable = -1;

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

    public AlertDialog setPositiveBtnLabel(String positiveBtnLabel) {
        this.positiveBtnLabel = positiveBtnLabel;
        return this;
    }

    public AlertDialog setNegativeBtnLabel(String negativeBtnLabel) {
        this.negativeBtnLabel = negativeBtnLabel;
        return this;
    }

    public AlertDialog setCloseBtnShow(boolean closeBtnShow) {
        isCloseBtnShow = closeBtnShow;
        return this;
    }

    public AlertDialog setNegativeBtnShow(boolean negativeBtnShow) {
        isNegativeBtnShow = negativeBtnShow;
        return this;
    }

    public AlertDialog setNegativeBtnColor(int negativeBtnColor) {
        this.negativeBtnColor = negativeBtnColor;
        return this;
    }

    public AlertDialog setNegativeBtnBgDrawable(int negativeBtnBgDrawable) {
        this.negativeBtnBgDrawable = negativeBtnBgDrawable;
        return this;
    }

    public AlertDialog setPositiveBtnColor(int positiveBtnColor) {
        this.positiveBtnColor = positiveBtnColor;
        return this;
    }

    public AlertDialog setPositiveBtnBgDrawable(int positiveBtnBgDrawable) {
        this.positiveBtnBgDrawable = positiveBtnBgDrawable;
        return this;
    }

    public AlertDialog setOnNegativeClickListener(NegativeListener negativeListener) {
        this.negativeListener = negativeListener;
        return this;
    }

    public AlertDialog setOnPositiveClickListener(PositiveListener positiveListener) {
        this.positiveListener = positiveListener;
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
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(AlertViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().tvTitle.setText(mTitle);
        getViewDataBinding().tvMsg.setText(msg);

        getViewDataBinding().imgClose.setOnClickListener(this);
        getViewDataBinding().btnPositive.setOnClickListener(this);
        getViewDataBinding().btnNegative.setOnClickListener(this);
        setUpConfiguration();
    }

    private void setUpConfiguration() {
        getViewDataBinding().imgClose.setVisibility(isCloseBtnShow ? View.VISIBLE : View.GONE);
        getViewDataBinding().btnPositive.setTextColor(getResources().getColor(positiveBtnColor));
        getViewDataBinding().btnNegative.setTextColor(getResources().getColor(negativeBtnColor));
        if (!positiveBtnLabel.isEmpty())
            getViewDataBinding().btnPositive.setText(positiveBtnLabel);
        if (!negativeBtnLabel.isEmpty())
            getViewDataBinding().btnNegative.setText(negativeBtnLabel);
        if (isNegativeBtnShow) {
            getViewDataBinding().btnPositive.setBackground(ContextCompat.getDrawable(getBaseActivity(), R.drawable.bg_bottom_right_primary_corner));
            getViewDataBinding().btnNegative.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().btnPositive.setBackground(ContextCompat.getDrawable(getBaseActivity(), R.drawable.bg_bottom_lr_primary_corner));
            getViewDataBinding().btnNegative.setVisibility(View.GONE);
        }

        if (positiveBtnBgDrawable != -1)
            getViewDataBinding().btnPositive.setBackground(ContextCompat.getDrawable(getBaseActivity(), positiveBtnBgDrawable));

        if (negativeBtnBgDrawable != -1)
            getViewDataBinding().btnPositive.setBackground(ContextCompat.getDrawable(getBaseActivity(), negativeBtnBgDrawable));
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();

        if (v.getId() == R.id.img_close) {
            dismiss();
        } else if (v.getId() == R.id.btn_negative) {
            if (negativeListener != null) {
                negativeListener.onNegativeClick(this);
            }
        } else if (v.getId() == R.id.btn_positive) {
            if (positiveListener != null) {
                positiveListener.onPositiveClick(this);
            }
        }
    }

    public interface NegativeListener {
        void onNegativeClick(AlertDialog dialog);
    }

    public interface PositiveListener {
        void onPositiveClick(AlertDialog dialog);
    }
}
