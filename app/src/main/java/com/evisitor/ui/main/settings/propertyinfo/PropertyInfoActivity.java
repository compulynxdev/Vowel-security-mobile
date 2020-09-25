package com.evisitor.ui.main.settings.propertyinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityPropertyInfoBinding;
import com.evisitor.ui.base.BaseActivity;

public class PropertyInfoActivity extends BaseActivity<ActivityPropertyInfoBinding, PropertyInfoViewModel> {

    public static Intent newIntent(Context context) {
        return new Intent(context, PropertyInfoActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_property_info;
    }

    @Override
    public PropertyInfoViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(PropertyInfoViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        getViewDataBinding().toolbar.tvTitle.setText(R.string.property_info);

        getViewDataBinding().toolbar.imgBack.setVisibility(View.VISIBLE);
        getViewDataBinding().toolbar.imgBack.setOnClickListener(v -> onBackPressed());

        getViewModel().getPropertyInfo().observe(this, propertyInfoResponse -> {
            getViewDataBinding().tvProperty.setText(propertyInfoResponse.getFullName());
            getViewDataBinding().tvPropertyType.setText(propertyInfoResponse.getPropertyTypeName());
            getViewDataBinding().tvAddress.setText(propertyInfoResponse.getAddress().concat(", ").concat(propertyInfoResponse.getZipCode()).concat(" ").concat(propertyInfoResponse.getCountry()));
            getViewDataBinding().tvPhone.setText(propertyInfoResponse.getContactNo().isEmpty() ? getString(R.string.na) : propertyInfoResponse.getContactNo());
            getViewDataBinding().tvExt.setText(propertyInfoResponse.getExtensionNo().isEmpty() ? getString(R.string.na) : propertyInfoResponse.getExtensionNo());
            getViewDataBinding().tvEmail.setText(propertyInfoResponse.getEmail().isEmpty() ? getString(R.string.na) : propertyInfoResponse.getEmail());

            if (propertyInfoResponse.getImage().isEmpty()) {
                getViewDataBinding().tvImgLabel.setVisibility(View.VISIBLE);
            } else {
                getViewDataBinding().tvImgLabel.setVisibility(View.GONE);
                Glide.with(this)
                        .load(getViewModel().getDataManager().getImageBaseURL().concat(propertyInfoResponse.getImage()))
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(getViewDataBinding().imageView);
            }
        });
    }

}
