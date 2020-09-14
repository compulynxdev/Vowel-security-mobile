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
import com.evisitor.util.AppLogger;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements View.OnClickListener, LoginNavigator {

    private String fcmToken = "";

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
        initFcmToken();

        if (getViewModel().getDataManager().isRememberMe()) {
            getViewDataBinding().cbRemember.setChecked(true);
            getViewDataBinding().etUsername.setText(getViewModel().getDataManager().getUsername());
            getViewDataBinding().etPassword.setText(getViewModel().getDataManager().getUserPassword());
        }
    }

    private void initFcmToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        AppLogger.d("Firebase getInstanceId failed : ", String.valueOf(task.getException()));
                        return;
                    }
                    // Get new Instance ID fcmToken
                    fcmToken = Objects.requireNonNull(task.getResult()).getToken();
                    AppLogger.d("Registration Token : ", fcmToken);
                });
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
                    Objects.requireNonNull(getViewDataBinding().etPassword.getText()).toString(), fcmToken,
                    getViewDataBinding().cbRemember.isChecked());
        } else if (v.getId() == R.id.img_info) {
            DeviceInfoDialog.newInstance().show(getSupportFragmentManager());
        }
    }
}
