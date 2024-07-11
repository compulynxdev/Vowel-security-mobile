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
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(ForgotPasswordViewModel.class);
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
        getViewDataBinding().btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            if (NetworkUtils.isNetworkConnected(this)) {
                getViewModel().verifyData(getViewDataBinding().etUsername.getText().toString(), getViewDataBinding().etEmail.getText().toString(), type);
            } else {
                showAlert(R.string.alert, R.string.alert_internet);
            }
        } else if (v.getId() == R.id.rb_email || v.getId() == R.id.rb_administrator) {
            setCheckBoxData();
        }

    }

    private void setCheckBoxData() {
        if (getViewDataBinding().rbEmail.isChecked()) {
            getViewDataBinding().etEmail.setVisibility(View.VISIBLE);
            getViewDataBinding().tvNote.setVisibility(View.GONE);
            type = "reset";
        } else {
            getViewDataBinding().etEmail.setVisibility(View.GONE);
            getViewDataBinding().tvNote.setVisibility(View.VISIBLE);
            type = "request";
        }
    }

    @Override
    public void openNextActivity() {
        onBackPressed();
    }
}
