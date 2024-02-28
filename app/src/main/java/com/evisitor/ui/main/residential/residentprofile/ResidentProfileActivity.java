package com.evisitor.ui.main.residential.residentprofile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.data.model.ResidentProfile;
import com.evisitor.databinding.ActivityResidentProfileBinding;
import com.evisitor.ui.base.BaseActivity;

public class ResidentProfileActivity extends BaseActivity<ActivityResidentProfileBinding, ResidentProfileViewModel> implements ResidentProfileNavigator {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ResidentProfileActivity.class);
    }

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
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(ResidentProfileViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
        setIntentData();
    }

    private void setUp() {
        getViewModel().setNavigator(this);
        getViewDataBinding().toolbar.imgBack.setVisibility(View.VISIBLE);
        getViewDataBinding().toolbar.imgBack.setOnClickListener(v -> onBackPressed());
        getViewDataBinding().toolbar.tvTitle.setText(R.string.resident_profile);
    }

    private void setIntentData() {
        ResidentProfile profile = (ResidentProfile) getIntent().getSerializableExtra("profile");
        assert profile != null;
        if (profile.getType().equalsIgnoreCase("OWNER") || profile.getType().equalsIgnoreCase("TENANT")) {
            getViewDataBinding().familyGroup.setVisibility(View.GONE);
        } else getViewDataBinding().familyGroup.setVisibility(View.VISIBLE);

        getViewDataBinding().tvName.setText(profile.getFullName().isEmpty() ? getString(R.string.na) : profile.getFullName());
        getViewDataBinding().tvOwnername.setText(profile.getOwnerName().isEmpty() ? getString(R.string.na) : profile.getOwnerName());
        getViewDataBinding().tvEmail.setText(profile.getEmail().isEmpty() ? getString(R.string.na) : profile.getEmail());
        getViewDataBinding().tvGender.setText(profile.getGender().isEmpty() ? getString(R.string.na) : profile.getGender());
        getViewDataBinding().tvType.setText(profile.getType().isEmpty() ? getString(R.string.na) : profile.getType());
        getViewDataBinding().tvAddress.setText(profile.getPremiseName().isEmpty() ? getString(R.string.na) : profile.getPremiseName());
        getViewDataBinding().tvRelationship.setText(profile.getRelationship().isEmpty() ? getString(R.string.na) : profile.getRelationship());
        getViewDataBinding().tvContact.setText(profile.getPrimaryNo().isEmpty() ? getString(R.string.na) : "+".concat(profile.getDialingCode()).concat(profile.getPrimaryNo()));
        getViewDataBinding().tvSecondaryContact.setText(profile.getSecondaryNo().isEmpty() ? getString(R.string.na) : "+".concat(profile.getSecDialingCode()).concat(profile.getSecondaryNo()));

        if (profile.getImage().isEmpty()) {
            Glide.with(this)
                    .load(R.drawable.ic_person)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(getViewDataBinding().imgUser);
        } else {
            Glide.with(this)
                    .load(mViewModel.getDataManager().getImageBaseURL().concat(profile.getImage()))
                    .centerCrop()
                    .placeholder(R.drawable.ic_person)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(getViewDataBinding().imgUser);
        }

        getViewDataBinding().btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResidentVehicleDialog.newInstance(profile, (isCheckIn, profile1, vehicleNo) -> getViewModel().doCheckInCheckOut(isCheckIn, profile1,vehicleNo)).show(getSupportFragmentManager());
            }
        });
    }

    @Override
    public void openNext() {
        onBackPressed();
    }
}
