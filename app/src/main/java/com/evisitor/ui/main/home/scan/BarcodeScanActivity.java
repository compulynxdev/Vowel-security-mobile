package com.evisitor.ui.main.home.scan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.budiyev.android.codescanner.CodeScanner;
import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityScanBarcodeBinding;
import com.evisitor.ui.base.BaseActivity;

public class BarcodeScanActivity extends BaseActivity<ActivityScanBarcodeBinding, BarcodeScanViewModel> {

    private CodeScanner mCodeScanner;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, BarcodeScanActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_barcode;
    }

    @Override
    public BarcodeScanViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(BarcodeScanViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_scan_barcode);
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(v -> onBackPressed());

        setUp();
    }

    private void setUp() {
        mCodeScanner = new CodeScanner(this, getViewDataBinding().scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            Intent intent = getIntent();
            intent.putExtra("data", result.getText());
            setResult(RESULT_OK, intent);
            finish();
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCodeScanner != null)
            mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        if (mCodeScanner != null)
            mCodeScanner.releaseResources();
        super.onPause();
    }
}
