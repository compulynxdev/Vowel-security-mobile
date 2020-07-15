package com.evisitor.eVisitor.ui.main;

import android.os.Bundle;
import com.evisitor.eVisitor.R;
import com.evisitor.eVisitor.databinding.ActivityMainBinding;
import com.evisitor.eVisitor.ui.base.BaseActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding,MainViewModel> {

    @Override
    public int getBindingVariable() {
        return com.evisitor.eVisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainViewModel getViewModel() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
