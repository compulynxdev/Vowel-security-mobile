package com.evisitor.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityLoginBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.main.MainActivity;
import com.evisitor.ui.main.settings.info.DeviceInfoDialog;

import java.util.Objects;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements View.OnClickListener, LoginNavigator {

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public LoginViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(LoginViewModel.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);

        getViewDataBinding().btnLogin.setOnClickListener(this);
        getViewDataBinding().imgInfo.setOnClickListener(this);
        if (getViewModel().getDataManager().isRememberMe()) {
            getViewDataBinding().etUsername.setText(getViewModel().getDataManager().getUsername());
            getViewDataBinding().etPassword.setText(getViewModel().getDataManager().getUserPassword());
        }
    }

    @Override
    public void openMainActivity() {
        hideLoading();
        Intent intent = MainActivity.newIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            getViewModel().doVerifyAndLogin(Objects.requireNonNull(getViewDataBinding().etUsername.getText()).toString(),
                    Objects.requireNonNull(getViewDataBinding().etPassword.getText()).toString(),
                    getViewDataBinding().cbRemember.isChecked());
        } else if (v.getId() == R.id.img_info) {
            DeviceInfoDialog.newInstance().show(getSupportFragmentManager());
        }
    }
}
