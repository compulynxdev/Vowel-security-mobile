package com.evisitor.ui.main.residential.residentprofile;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityResidentProfileBinding;
import com.evisitor.ui.base.BaseActivity;

public class ResidentProfileActivity extends BaseActivity<ActivityResidentProfileBinding, ResidentProfileViewModel> implements ResidentProfileNavigator {

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_resident_profile;
    }

    @Override
    public ResidentProfileViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(ResidentProfileViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().getResidentData(getIntent().getStringExtra("code")).observe(this, residentProfile -> {
            if (residentProfile.getType().equalsIgnoreCase("OWNER") || residentProfile.getType().equalsIgnoreCase("TENANT")) {
                getViewDataBinding().familyGroup.setVisibility(View.GONE);
            } else getViewDataBinding().familyGroup.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void openNextActivity() {
        onBackPressed();
    }
}
