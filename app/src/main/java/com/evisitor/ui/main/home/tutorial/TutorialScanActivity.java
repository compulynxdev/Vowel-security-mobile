package com.evisitor.ui.main.home.tutorial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityTutorialScanBinding;
import com.evisitor.ui.base.BaseActivity;

public class TutorialScanActivity extends BaseActivity<ActivityTutorialScanBinding, TutorialScanViewModel> implements TutorialScanNavigator {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, TutorialScanActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tutorial_scan;
    }

    @Override
    public TutorialScanViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(TutorialScanViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewDataBinding().header.tvTitle.setText(R.string.title_tutorial);
        getViewDataBinding().header.imgBack.setVisibility(View.VISIBLE);
        getViewDataBinding().header.imgBack.setOnClickListener(v -> onBackPressed());

        getViewDataBinding().btnContinue.setOnClickListener(view -> {

        });
    }


}
