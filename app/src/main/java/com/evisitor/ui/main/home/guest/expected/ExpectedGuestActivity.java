package com.evisitor.ui.main.home.guest.expected;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityExpectedGuestBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.ui.dialog.AlertDialog;
import com.evisitor.ui.main.home.guest.add.AddGuestActivity;
import com.evisitor.ui.main.home.scan.ScanIDActivity;
import com.sharma.mrzreader.MrzRecord;

public class ExpectedGuestActivity extends BaseActivity<ActivityExpectedGuestBinding, GuestViewModel> {

    private final int SCAN_RESULT = 101;

    public static Intent getStartIntent(Context context){
        return new Intent(context,ExpectedGuestActivity.class);
    }
    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_expected_guest;
    }

    @Override
    public GuestViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstance()).get(GuestViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().setNavigator(this);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(R.string.title_expected_guests );
        setUpSearch();
        setUpList();
        getViewDataBinding().fabAdd.setOnClickListener(v -> AlertDialog.newInstance()
                .setNegativeBtnShow(true)
                .setCloseBtnShow(true)
                .setTitle(getString(R.string.check_in))
                .setMsg(getString(R.string.msg_add_guest_option))
                .setNegativeBtnColor(R.color.colorPrimary)
                .setPositiveBtnLabel(getString(R.string.manually))
                .setNegativeBtnLabel(getString(R.string.scan_id))
                .setOnNegativeClickListener(dialog1 -> {
                    dialog1.dismiss();
                    Intent i = ScanIDActivity.getStartIntent(getContext());
                    startActivityForResult(i, SCAN_RESULT);
                })
                .setOnPositiveClickListener(dialog12 -> {
                    dialog12.dismiss();
                    Intent i = AddGuestActivity.getStartIntent(this);
                    startActivity(i);
                }).show(getSupportFragmentManager()));
    }

    private void setUpList() {
        replaceFragment(ExpectedGuestFragment.newInstance(getViewDataBinding().etSearch),R.id.guest_frame);
    }

    private void setUpSearch() {
        ImageView imgSearch = findViewById(R.id.img_search);
        imgSearch.setVisibility(View.VISIBLE);
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(v -> onBackPressed());
        imgSearch.setOnClickListener(v -> {
            hideKeyboard();
            getViewDataBinding().searchBar.setVisibility(getViewDataBinding().searchBar.getVisibility() == View.GONE
                    ? View.VISIBLE : View.GONE);

            if (!getViewDataBinding().etSearch.getText().toString().trim().isEmpty()) {
                getViewDataBinding().etSearch.setText("");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SCAN_RESULT && data != null) {
                MrzRecord mrzRecord = (MrzRecord) data.getSerializableExtra("Record");
                Intent intent = AddGuestActivity.getStartIntent(this);
                intent.putExtra("Record", mrzRecord);
                startActivity(intent);
            }
        }
    }
}
