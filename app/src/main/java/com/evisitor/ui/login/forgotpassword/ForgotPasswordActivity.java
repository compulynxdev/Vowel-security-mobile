package com.evisitor.ui.login.forgotpassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityForgotPasswordBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.util.NetworkUtils;

public class ForgotPasswordActivity extends BaseActivity<ActivityForgotPasswordBinding, ForgotPasswordViewModel> implements ForgotPasswordNavigator, View.OnClickListener {

    String type = "reset";

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ForgotPasswordActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_forgot_password;
    }

    @Override
    public ForgotPasswordViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(ForgotPasswordViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        setUp();
    }

    private void setUp() {
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(v -> onBackPressed());
        imgBack.setColorFilter(getResources().getColor(R.color.black));
        getViewDataBinding().header.tvTitle.setText(R.string.forgot_password);
        getViewDataBinding().header.tvTitle.setTextColor(getResources().getColor(R.color.black));
        getViewDataBinding().rbEmail.setOnClickListener(this);
        getViewDataBinding().rbAdministrator.setOnClickListener(this);
        getViewDataBinding().btnContinue.setOnClickListener(this);
        getViewDataBinding().btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                if (getViewDataBinding().etUsername.getText().toString().isEmpty()) {
                    showAlert(R.string.alert, R.string.please_enter_username);
                } else {
                    getViewDataBinding().submitGroup.setVisibility(View.VISIBLE);
                    getViewDataBinding().btnContinue.setVisibility(View.GONE);
                    getViewDataBinding().etUsername.setEnabled(false);
                }
                break;

            case R.id.btn_submit:
                if (NetworkUtils.isNetworkConnected(this)) {
                    getViewModel().verifyData(getViewDataBinding().etUsername.getText().toString(), getViewDataBinding().etEmail.getText().toString(), type);
                } else showAlert(R.string.alert, R.string.alert_internet);
                break;

            case R.id.rb_email:
                setCheckBoxData();
            case R.id.rb_administrator:
                setCheckBoxData();
                break;

        }
    }

    private void setCheckBoxData() {
        if (getViewDataBinding().rbEmail.isChecked()) {
            getViewDataBinding().etEmail.setVisibility(View.VISIBLE);
            type = "reset";
        } else {
            getViewDataBinding().etEmail.setVisibility(View.GONE);
            type = "request";
        }
    }

    @Override
    public void openNextActivity() {
        onBackPressed();
    }
}
